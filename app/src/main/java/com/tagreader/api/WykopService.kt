package com.tagreader.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class WykopService {

    companion object {
        const val API_KEY = "aNd401dAPp"
        const val END_POINT = "http://a.wykop.pl"
    }

    lateinit var wykopService: WykopEndpoints
        private set

    fun setup() {
        val client = buildClient()
        val retrofit = buildRetrofit(client)
        wykopService = retrofit.create(WykopEndpoints::class.java)
    }

    private fun buildRetrofit(client: OkHttpClient) =
            Retrofit.Builder()
                    .baseUrl(END_POINT)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()

    private fun buildClient(): OkHttpClient {
        val httpLoggingInterceptor = createLoggingInterceptor()
        return OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    private fun createLoggingInterceptor() =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
}