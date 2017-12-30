package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.CastDevice

interface ChromecastCommunicationChannel : Cast.MessageReceivedCallback {
    val namespace: String

    fun sendMessage(message: String)
    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String)
}