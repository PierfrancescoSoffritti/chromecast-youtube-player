package com.pierfrancescosoffritti.chromecastyoutubesample

import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.Cast


class CustomChannel : Cast.MessageReceivedCallback {
    val namespace: String
        get() = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.customchannel"

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        Log.d(javaClass.simpleName, "onMessageReceived: " + message)
    }
}