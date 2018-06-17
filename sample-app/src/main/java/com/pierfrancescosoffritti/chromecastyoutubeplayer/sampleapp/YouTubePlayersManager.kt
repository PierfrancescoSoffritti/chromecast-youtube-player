package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.arch.lifecycle.Lifecycle
import android.view.View
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.*
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker

class YouTubePlayersManager(
        private val lifecycle: Lifecycle, private val localYouTubePlayerListener: LocalYouTubePlayerListener,
        private val youtubePlayerView: YouTubePlayerView, chromecastControls: View) : ChromecastConnectionListener {

    private val chromeCastYouTubePlayer = ChromecastYouTubePlayer()
    val chromecastUIController = ChromecastUIController(chromecastControls, chromeCastYouTubePlayer)

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()

    private lateinit var youTubePlayer: YouTubePlayer

    private var playingOnCastPlayer = false

    private var currentSecond: Float = 0f
    private lateinit var lastVideoId: String

    init {
        chromeCastYouTubePlayer.addListener(chromecastPlayerStateTracker)
        initLocalYouTube()
    }

    override fun onChromecastConnecting() {
        youTubePlayer.pause()
    }

    override fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        initializeCastPlayer(chromecastCommunicationChannel)

        playingOnCastPlayer = true
    }

    override fun onChromecastDisconnected() {
        if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
            youTubePlayer.loadVideo(lastVideoId, currentSecond)
        else
            youTubePlayer.cueVideo(lastVideoId, currentSecond)

        playingOnCastPlayer = false
    }

    private fun initLocalYouTube() {
        lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.initialize({ youtubePlayer ->

            this.youTubePlayer = youtubePlayer

            youtubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    if(!playingOnCastPlayer)
                        youtubePlayer.loadVideo("6JYIGclVQdw", currentSecond)

                    localYouTubePlayerListener.onLocalYouTubePlayerReady()
                }

                override fun onVideoId(videoId: String) {
                    lastVideoId = videoId
                }

                override fun onCurrentSecond(second: Float) {
                    currentSecond = second
                }
            })

        }, true)
    }

    private fun initializeCastPlayer(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        chromeCastYouTubePlayer.initialize( YouTubePlayerInitListener { youtubePlayer ->

            youtubePlayer.addListener(YouTubePlayerLogger())
            youtubePlayer.addListener(chromecastUIController)

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo("6JYIGclVQdw", currentSecond)
                }

                override fun onVideoId(videoId: String) {
                    lastVideoId = videoId
                }

                override fun onCurrentSecond(second: Float) {
                    currentSecond = second
                }
            })
        }, chromecastCommunicationChannel)
    }

    interface LocalYouTubePlayerListener {
        fun onLocalYouTubePlayerReady()
    }
}