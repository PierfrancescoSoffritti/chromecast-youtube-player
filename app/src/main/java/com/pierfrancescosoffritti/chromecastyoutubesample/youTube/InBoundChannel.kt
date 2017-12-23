package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

class InBoundChannel(private val bridge: YouTubePlayerBridge) : ChromecastCustomChannel.ChannelObserver {
    override fun onMessageReceived(message: String) {
        Log.d("on msg", message)

        when (message) {
            Constants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            Constants.READY -> bridge.sendReady()
        }
    }
}