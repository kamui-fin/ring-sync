package com.kamui.phonesyncer

import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class Result (
    val msg: String
)

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("incoming")
    fun notifyIncoming(@Body callData: CallData): retrofit2.Call<Result>
}

class RestApiService(address: String) {
    private var retrofit: Retrofit? = null

    init {
        val client = OkHttpClient.Builder().connectTimeout(500, TimeUnit.MILLISECONDS).build()
        retrofit = Retrofit.Builder()
            .baseUrl(address)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun addUser(callData: CallData){
        val retrofit = retrofit!!.create(RestApi::class.java)
        retrofit.notifyIncoming(callData).enqueue(
            object : Callback<Result> {
                override fun onFailure(call: retrofit2.Call<Result>, t: Throwable) {
                    throw t
                }
                override fun onResponse( call: retrofit2.Call<Result>, response: Response<Result>) {
                }
            }
        )
    }
}
