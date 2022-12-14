package com.kamui.phonesyncer

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi

data class CallData (
    val name: String?,
    val number: String
)


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
        }
        val response = CallResponse.Builder()
        respondToCall(call, response.build())
    }
}