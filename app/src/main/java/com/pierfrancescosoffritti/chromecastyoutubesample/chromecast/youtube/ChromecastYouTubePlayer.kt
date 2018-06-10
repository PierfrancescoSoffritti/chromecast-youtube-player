package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.youtube

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.youtubeplayer.player.*

class ChromecastYouTubePlayer : YouTubePlayer, YouTubePlayerBridge.YouTubePlayerBridgeCallbacks {

    private lateinit var chromecastCommunicationChannel: ChromecastCommunicationChannel
    private lateinit var youTubePlayerInitListener: YouTubePlayerInitListener

    private val inputMessageDispatcher = ChromecastYouTubeMessageDispatcher( YouTubePlayerBridge(this) )

    private val youTubePlayerListeners = HashSet<YouTubePlayerListener>()

    fun initialize(initListener: YouTubePlayerInitListener, communicationChannel: ChromecastCommunicationChannel) {
        youTubePlayerInitListener = initListener
        chromecastCommunicationChannel = communicationChannel

        communicationChannel.addObserver(inputMessageDispatcher)
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
        throw RuntimeException("cueVideo NO-OP")
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

    override fun setVolume(volumePercent: Int) {
        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.SET_VOLUME,
                "volumePercent" to volumePercent
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    override fun seekTo(time: Float) {
        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.SEEK_TO,
                "time" to time
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    override fun addListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.add(listener)

    override fun removeListener(listener: YouTubePlayerListener): Boolean = youTubePlayerListeners.remove(listener)

    override fun getListeners(): MutableCollection<YouTubePlayerListener> = youTubePlayerListeners
}