package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.CastDevice

/**
 * Custom channel for full-duplex communication between sender and receiver, on a specific namespace.
 * The channel can be observed.
 */
interface ChromecastCommunicationChannel : Cast.MessageReceivedCallback {
    val namespace: String

    fun sendMessage(message: String)
    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String)

    val observers : HashSet<ChannelObserver>
    fun addObserver(channelObserver: ChannelObserver) = observers.add(channelObserver)
    fun removeObserver(channelObserver: ChannelObserver) = observers.remove(channelObserver)

    interface ChannelObserver {
        fun onMessageReceived(message: CastReceiverInputMessage)
    }
}