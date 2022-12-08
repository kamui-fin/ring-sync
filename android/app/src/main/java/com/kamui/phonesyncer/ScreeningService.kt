package com.kamui.phonesyncer

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class ScreeningService : CallScreeningService() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onScreenCall(call: Call.Details) {
        if (call.callDirection == Call.Details.DIRECTION_INCOMING) {
            val name = call.callerDisplayName
            val number = call.handle.schemeSpecificPart
            println("INCOMING CALL FROM $name $number")
        }
        val response = CallResponse.Builder()
        respondToCall(call, response.build())
    }
}