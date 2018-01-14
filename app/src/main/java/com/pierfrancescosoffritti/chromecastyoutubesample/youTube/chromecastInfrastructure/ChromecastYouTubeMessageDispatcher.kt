package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecastInfrastructure

import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.CastReceiverInputMessage
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

/**
 * Class responsible for dispatching messages received from the CastReceiver
  */
class ChromecastYouTubeMessageDispatcher(private val bridge: YouTubePlayerBridge) : ChromecastCommunicationChannel.ChannelObserver {
    override fun onMessageReceived(message: CastReceiverInputMessage) {
//        Log.d(javaClass.simpleName +" on msg, type ", message.type + " data: " + message.data)

        when (message.type) {
            ChromecastCommunicationConstants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            ChromecastCommunicationConstants.READY -> bridge.sendReady()
            ChromecastCommunicationConstants.STATE_CHANGED -> bridge.sendStateChange(message.data)
            ChromecastCommunicationConstants.PLAYBACK_QUALITY_CHANGED -> bridge.sendPlaybackQualityChange(message.data)
            ChromecastCommunicationConstants.PLAYBACK_RATE_CHANGED -> bridge.sendPlaybackRateChange(message.data)
            ChromecastCommunicationConstants.ERROR -> bridge.sendError(message.data)
            ChromecastCommunicationConstants.API_CHANGED -> bridge.sendApiChange()
            ChromecastCommunicationConstants.VIDEO_CURRENT_TIME -> bridge.sendVideoCurrentTime(message.data)
            ChromecastCommunicationConstants.VIDEO_DURATION -> bridge.sendVideoDuration(message.data)
            ChromecastCommunicationConstants.VIDEO_ID -> bridge.sendVideoId(message.data)
            ChromecastCommunicationConstants.MESSAGE -> bridge.sendMessage(message.data)
        }
    }
}