package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.framework.SessionManager
import com.google.gson.Gson

/**
 * This class represents the communication channel with the CastReceiver.
 * It has two responsibilities: receiving and sending messages.
 */
class ChromecastCommunicationChannel(private val sessionManager: SessionManager) : Cast.MessageReceivedCallback {
    val namespace get() = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication"
    private val observers = HashSet<ChannelObserver>()

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        val parsedMessage = Gson().fromJson<YouTubeMessage>(message, YouTubeMessage::class.java)

        observers.forEach{ it.onMessageReceived(parsedMessage) }
    }

    fun sendMessage(message: String) = try {
        sessionManager.currentCastSession
                .sendMessage(namespace, message)
                .setResultCallback {
                    if(it.isSuccess)
                        Log.d(this.javaClass.simpleName, "message sent")
                    else
                        Log.d(this.javaClass.simpleName, "failed, can't send message")
                }

    } catch (e: Exception) {
        throw RuntimeException(e)
    }

    fun addObserver(channelObserver: ChannelObserver) = observers.add(channelObserver)

    fun removeObserver(channelObserver: ChannelObserver) = observers.remove(channelObserver)

    interface ChannelObserver {
        fun onMessageReceived(message: YouTubeMessage)
    }
}