package fi.mkauha.venueapp.network

import fi.mkauha.venueapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val newUrl = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .addQueryParameter("client_secret", BuildConfig.CLIENT_SECRET)
            .addQueryParameter("v", currentDate)
            .build()

        val requestWithClientIdAndSecret = chain.request().newBuilder().url(newUrl).build()

        return chain.proceed(requestWithClientIdAndSecret)
    }
}