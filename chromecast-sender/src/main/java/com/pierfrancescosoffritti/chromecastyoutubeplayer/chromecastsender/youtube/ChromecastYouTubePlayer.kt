package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube

import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.utils.JSONUtils
import com.pierfrancescosoffritti.youtubeplayer.player.*

class ChromecastYouTubePlayer internal constructor(private val chromecastCommunicationChannel: ChromecastCommunicationChannel) : YouTubePlayer, YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

    private lateinit var youTubePlayerInitListener: YouTubePlayerInitListener

    private val inputMessageDispatcher = ChromecastYouTubeMessageDispatcher( YouTubePlayerBridge(this) )
    private val youTubePlayerListeners = HashSet<YouTubePlayerListener>()

    fun initialize(initListener: YouTubePlayerInitListener) {
        youTubePlayerListeners.clear()

        youTubePlayerInitListener = initListener

        chromecastCommunicationChannel.addObserver(inputMessageDispatcher)
    }

    override fun onYouTubeIframeAPIReady() {
        youTubePlayerInitListener.onInitSuccess(this)
    }

    override fun loadVideo(videoId: String, startSeconds: Float) {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.LOAD,
                "videoId" to videoId,
                "startSeconds" to startSeconds.toString()
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun cueVideo(videoId: String, startSeconds: Float) {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.CUE,
                "videoId" to videoId,
                "startSeconds" to startSeconds.toString()
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun play() {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.PLAY
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun pause() {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.PAUSE
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun setVolume(volumePercent: Int) {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.SET_VOLUME,
                "volumePercent" to volumePercent.toString()
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun seekTo(time: Float) {
        val message = JSONUtils.buildFlatJson(
                "command" to ChromecastCommunicationConstants.SEEK_TO,
                "time" to time.toString()
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    override fun addListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.add(listener)
    override fun removeListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.remove(listener)
    override fun getListeners(): MutableCollection<YouTubePlayerListener> = youTubePlayerListeners
}