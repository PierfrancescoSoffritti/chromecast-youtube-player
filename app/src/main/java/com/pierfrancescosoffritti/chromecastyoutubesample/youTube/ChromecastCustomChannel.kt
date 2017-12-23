package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.framework.SessionManager

class ChromecastCustomChannel(private val sessionManager: SessionManager) : Cast.MessageReceivedCallback {
    val namespace
        get() = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication"
    private val observers = HashSet<ChannelObserver>()

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        val m = message.replace("\"", "")
        observers.forEach{ it.onMessageReceived(m)}
    }

    fun sendMessage(message: String) = try {
        sessionManager.currentCastSession
                .sendMessage(namespace, message)
                .setResultCallback {
                    if(it.isSuccess)
                        Log.d(this.javaClass.simpleName, "message sent")
                }
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

    fun addObserver(channelObserver: ChannelObserver) = observers.add(channelObserver)

    fun removeObserver(channelObserver: ChannelObserver) = observers.remove(channelObserver)

    interface ChannelObserver {
        fun onMessageReceived(message: String)
    }
}