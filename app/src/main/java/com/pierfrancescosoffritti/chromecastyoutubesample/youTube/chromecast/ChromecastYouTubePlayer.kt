package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.youtubeplayer.player.*

class ChromecastYouTubePlayer() : YouTubePlayer, YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

    private lateinit var chromecastCommunicationChannel: ChromecastCommunicationChannel
    private lateinit var youTubePlayerInitListener: YouTubePlayerInitListener

    private val messageDispatcher = ChromecastYouTubeMessageDispatcher(YouTubePlayerBridge(this))

    private val youTubePlayerListeners = HashSet<YouTubePlayerListener>()
    private val playerStateTracker = PlayerStateTracker()

    fun initialize(chromecastCommunicationChannel: ChromecastCommunicationChannel, youTubePlayerInitListener: YouTubePlayerInitListener) {
        youTubePlayerListeners.clear()
        youTubePlayerListeners.add(playerStateTracker)

        this.youTubePlayerInitListener = youTubePlayerInitListener
        this.chromecastCommunicationChannel = chromecastCommunicationChannel

        chromecastCommunicationChannel.addObserver(messageDispatcher)
    }

    override fun onYouTubeIframeAPIReady() {
        youTubePlayerInitListener.onInitSuccess(this)
    }

    override fun loadVideo(videoId: String, startSeconds: Float) {
        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.LOAD,
                "videoId" to videoId,
                "startSeconds" to startSeconds
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    override fun cueVideo(videoId: String, startSeconds: Float) {
    }

    override fun play() {
        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.PLAY
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    override fun pause() {
        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.PAUSE
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
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