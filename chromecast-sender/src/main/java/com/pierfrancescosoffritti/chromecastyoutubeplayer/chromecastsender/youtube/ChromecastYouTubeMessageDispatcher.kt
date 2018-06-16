package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube

import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.MessageFromReceiver
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerBridge

/**
 * Class responsible for dispatching messages received from the CastReceiver
  */
class ChromecastYouTubeMessageDispatcher(private val bridge: YouTubePlayerBridge) : ChromecastCommunicationChannel.ChannelObserver {
    override fun onMessageReceived(messageFromReceiver: MessageFromReceiver) {
        when (messageFromReceiver.type) {
            ChromecastCommunicationConstants.IFRAME_API_READY -> bridge.sendYouTubeIframeAPIReady()
            ChromecastCommunicationConstants.READY -> bridge.sendReady()
            ChromecastCommunicationConstants.STATE_CHANGED -> bridge.sendStateChange(messageFromReceiver.data)
            ChromecastCommunicationConstants.PLAYBACK_QUALITY_CHANGED -> bridge.sendPlaybackQualityChange(messageFromReceiver.data)
            ChromecastCommunicationConstants.PLAYBACK_RATE_CHANGED -> bridge.sendPlaybackRateChange(messageFromReceiver.data)
            ChromecastCommunicationConstants.ERROR -> bridge.sendError(messageFromReceiver.data)
            ChromecastCommunicationConstants.API_CHANGED -> bridge.sendApiChange()
            ChromecastCommunicationConstants.VIDEO_CURRENT_TIME -> bridge.sendVideoCurrentTime(messageFromReceiver.data)
            ChromecastCommunicationConstants.VIDEO_DURATION -> bridge.sendVideoDuration(messageFromReceiver.data)
            ChromecastCommunicationConstants.VIDEO_ID -> bridge.sendVideoId(messageFromReceiver.data)
        }
    }
}