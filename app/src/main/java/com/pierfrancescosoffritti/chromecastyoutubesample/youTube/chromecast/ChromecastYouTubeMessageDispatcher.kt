package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast

import android.util.Log
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.CastReceiverInputMessage
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

/**
 * class responsible for dispatching messages received from the CastReceiver
  */
class ChromecastYouTubeMessageDispatcher(private val bridge: YouTubePlayerBridge) : ChromecastYouTubeIOChannel.ChannelObserver {
    override fun onMessageReceived(inputMessage: CastReceiverInputMessage) {
        Log.d(javaClass.simpleName +" on msg, type: ", inputMessage.type + " data: " + inputMessage.data)

        when (inputMessage.type) {
            ChromecastCommunicationConstants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            ChromecastCommunicationConstants.READY -> bridge.sendReady()
        }
    }
}