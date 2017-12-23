package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.youtubeplayer.player.*

// can't have an init method as in YouTubePlayerView, because communication is initialized by cast framework
class ChromecastYouTubePlayer(private val chromecastCommunicationChannel: ChromecastCommunicationChannel, private val youTubePlayerInitListener: YouTubePlayerInitListener) : YouTubePlayer, YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

    private val youTubePlayerListeners = HashSet<YouTubePlayerListener>()
    private val playerStateTracker = PlayerStateTracker()

    init {
        youTubePlayerListeners.clear()
        youTubePlayerListeners.add(playerStateTracker)

        chromecastCommunicationChannel.addObserver(ChromecastMessagesDispatcher(YouTubePlayerBridge(this)))
    }

    override fun onYouTubeIframeAPIReady() {
        youTubePlayerInitListener.onInitSuccess(this)
    }

    override fun loadVideo(videoId: String, startSeconds: Float) {
        val message: JsonObject = jsonObject(
                "command" to Constants.LOAD,
                "videoId" to videoId,
                "startSeconds" to startSeconds
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    override fun cueVideo(videoId: String, startSeconds: Float) {
    }

    override fun play() {
    }

    override fun pause() {
    }

    override fun mute() {
    }

    override fun unMute() {
    }

    override fun setVolume(volumePercent: Int) {
    }

    override fun seekTo(time: Int) {
    }

    override fun getCurrentState(): Int = playerStateTracker.currentState

    override fun addListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.add(listener)

    override fun removeListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.remove(listener)

    override fun getListeners(): MutableCollection<YouTubePlayerListener> = youTubePlayerListeners

    private inner class PlayerStateTracker : AbstractYouTubePlayerListener() {
        @PlayerConstants.PlayerState.State internal var currentState: Int = PlayerConstants.PlayerState.UNKNOWN

        override fun onStateChange(@PlayerConstants.PlayerState.State state: Int) {
            this.currentState = state
        }
    }
}