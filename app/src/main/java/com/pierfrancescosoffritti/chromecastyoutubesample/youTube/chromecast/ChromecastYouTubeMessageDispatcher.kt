package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast

import android.util.Log
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.CastReceiverInputMessage
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

/**
 * Class responsible for dispatching messages received from the CastReceiver
  */
class ChromecastYouTubeMessageDispatcher(private val bridge: YouTubePlayerBridge) : ChromecastCommunicationChannel.ChannelObserver {
    override fun onMessageReceived(message: CastReceiverInputMessage) {
        Log.d(javaClass.simpleName +" on msg, type ", message.type + " data: " + message.data)

        when (message.type) {
            ChromecastCommunicationConstants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            ChromecastCommunicationConstants.READY -> bridge.sendReady()
            ChromecastCommunicationConstants.STATE_CHANGED -> bridge.sendStateChange(message.data)
        }
    }
}