package fi.mkauha.venueapp.di
import android.content.Context
import android.location.LocationManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fi.mkauha.venueapp.data.LocationRepository
import fi.mkauha.venueapp.data.VenuesRepository
import fi.mkauha.venueapp.data.interfaces.ILocationRepository
import fi.mkauha.venueapp.data.interfaces.IVenuesRepository
import fi.mkauha.venueapp.network.AuthInterceptor
import fi.mkauha.venueapp.network.VenuesApi
import fi.mkauha.venueapp.network.VenuesApi.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Modules {

    @Provides
    @Singleton
    fun provideVenuesApi(retrofit: Retrofit): VenuesApi {
        return retrofit.create(VenuesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVenuesRepository(api: VenuesApi): IVenuesRepository {
        return VenuesRepository(api)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationManager: LocationManager): ILocationRepository {
        return LocationRepository(locationManager)
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext appContext: Context): LocationManager {
        return appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}