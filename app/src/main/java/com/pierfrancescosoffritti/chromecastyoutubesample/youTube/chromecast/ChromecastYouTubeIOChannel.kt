package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast

import android.util.Log
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.framework.SessionManager
import com.google.gson.Gson
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.CastReceiverInputMessage
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel

/**
 * This class represents the communication channel with the CastReceiver.
 * It has two responsibilities: receiving and sending messages.
 */
class ChromecastYouTubeIOChannel(private val sessionManager: SessionManager) : ChromecastCommunicationChannel {
    override val namespace get() = "urn:x-cast:com.pierfrancescosoffritti.chromecastyoutubesample.youtubeplayercommunication"

    private val observers = HashSet<ChannelObserver>()

    override fun sendMessage(message: String) = try {
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

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        val parsedMessage = Gson().fromJson<CastReceiverInputMessage>(message, CastReceiverInputMessage::class.java)

        observers.forEach{ it.onMessageReceived(parsedMessage) }
    }

    fun addObserver(channelObserver: ChannelObserver) = observers.add(channelObserver)
    fun removeObserver(channelObserver: ChannelObserver) = observers.remove(channelObserver)

    interface ChannelObserver {
        fun onMessageReceived(inputMessage: CastReceiverInputMessage)
    }
}