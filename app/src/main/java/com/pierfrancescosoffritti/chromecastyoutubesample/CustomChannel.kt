package com.pierfrancescosoffritti.chromecastyoutubesample

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.Cast


internal class CustomChannel : Cast.MessageReceivedCallback {
    val namespace: String
        get() = "urn:x-cast:com.example.custom"

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        Log.d(TAG, "onMessageReceived: " + message)
    }
}