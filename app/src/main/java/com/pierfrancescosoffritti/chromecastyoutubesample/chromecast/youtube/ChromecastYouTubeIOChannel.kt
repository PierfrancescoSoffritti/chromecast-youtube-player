package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.youtube

import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.framework.SessionManager
import com.google.gson.Gson
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.MessageFromReceiver
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel

class ChromecastYouTubeIOChannel(private val sessionManager: SessionManager) : ChromecastCommunicationChannel {
    override val namespace get() = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication"

    override val observers = HashSet<ChromecastCommunicationChannel.ChannelObserver>()

    override fun sendMessage(message: String) {
        try {
            sessionManager.currentCastSession
                    .sendMessage(namespace, message)
                    .setResultCallback {
                        if(it.isSuccess)
                            Log.d(this.javaClass.simpleName, "message sent")
                        else
                            Log.e(this.javaClass.simpleName, "failed, can't send message")
                    }

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        val parsedMessage = Gson().fromJson<MessageFromReceiver>(message, MessageFromReceiver::class.java)
        observers.forEach{ it.onMessageReceived(parsedMessage) }
    }
}