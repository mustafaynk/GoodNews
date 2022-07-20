package com.ynk.goodnews.di

import com.ynk.goodnews.restapi.NewsApi
import com.ynk.goodnews.utils.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Provides
    @Singleton
    fun providesNewsApi(): NewsApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Util.API_BASE_URL)
        .client(getUnsafeOkHttpClient().build())
        .build()
        .create(NewsApi::class.java)


    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}