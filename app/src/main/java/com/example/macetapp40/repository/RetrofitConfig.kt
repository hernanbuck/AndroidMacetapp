package com.example.macetapp40.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
                            )
        .build()
}

fun provideHttpclient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()
}

internal inline fun <T : Any> executeRetrofitRequest(request: () -> Response<T>): MacetappResult<T> {
    return try {
        val response = request.invoke()
        return if (response.isSuccessful && response.body() != null) {
            val body = response.body()
            if (body != null) {
                MacetappResult.Success(body)
            } else {
                MacetappResult.Error("Empty body found in this request")
            }
        } else {
            val errorBody = response.errorBody()
            val errorText = errorBody?.string() ?: "Error body null"
            MacetappResult.Error(errorText)
        }
    } catch (exception: UnknownHostException) {
        MacetappResult.Error(exception.toString())
    }
}