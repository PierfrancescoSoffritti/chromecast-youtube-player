package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io

import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.CastDevice

/**
 * Custom channel for full-duplex communication between sender and receiver, on a specific namespace.
 * The channel can be observed with a [ChannelObserver]
 */
internal interface ChromecastCommunicationChannel : Cast.MessageReceivedCallback {
    val namespace: String
    val observers : HashSet<ChannelObserver>

    fun sendMessage(message: String)
    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String)

    fun addObserver(channelObserver: ChannelObserver) = observers.add(channelObserver)
    fun removeObserver(channelObserver: ChannelObserver) = observers.remove(channelObserver)

    /**
     * Implement this interface to observe a [ChromecastCommunicationChannel]
     */
    interface ChannelObserver {
        fun onMessageReceived(messageFromReceiver: MessageFromReceiver)
    }
}