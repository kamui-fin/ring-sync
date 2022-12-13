package com.kamui.phonesyncer

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class CallData (
    val name: String?,
    val number: String
)


interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("incoming")
    fun notifyIncoming(@Body callData: CallData): retrofit2.Call<String>
}

class RestApiService(address: String) {
    private var retrofit: Retrofit? = null

    init {
        val client = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder()
            .baseUrl(address)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun addUser(callData: CallData){
        val retrofit = retrofit!!.create(RestApi::class.java)
        retrofit.notifyIncoming(callData).enqueue(
            object : Callback<String> {
                override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                    throw t
                }
                override fun onResponse( call: retrofit2.Call<String>, response: Response<String>) {
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
class ScreeningService : CallScreeningService() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onScreenCall(call: Call.Details) {
        if (call.callDirection == Call.Details.DIRECTION_INCOMING) {
            val name = call.callerDisplayName
            val number = call.handle.schemeSpecificPart

            val db = AppDatabase.getInstance(applicationContext)
            db.serverDao().getAll().forEach { server ->
                val ip = server.address
                val addr = "http://$ip:8080/"

                val apiService = RestApiService(addr)
                val callData = CallData(name, number)
                apiService.addUser(callData)
            }
            println("INCOMING CALL FROM $name $number")
        }
        val response = CallResponse.Builder()
        respondToCall(call, response.build())
    }
}