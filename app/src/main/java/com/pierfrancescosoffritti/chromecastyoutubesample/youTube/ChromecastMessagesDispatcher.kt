package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

/**
 * class responsible for dispatching messages received from the CastReceiver
  */
class ChromecastMessagesDispatcher(private val bridge: YouTubePlayerBridge) : ChromecastCommunicationChannel.ChannelObserver {
    override fun onMessageReceived(message: YouTubeMessage) {
        Log.d(javaClass.simpleName +" on msg, type: ", message.type + " data: " +message.data)

        when (message.type) {
            Constants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            Constants.READY -> bridge.sendReady()
        }
    }
}