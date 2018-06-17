package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.arch.lifecycle.Lifecycle
import android.view.View
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.*
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker

class YouTubePlayersManager(
        private val lifecycle: Lifecycle, private val localYouTubePlayerListener: LocalYouTubePlayerListener,
        private val youtubePlayerView: YouTubePlayerView, chromecastControls: View) : ChromecastConnectionListener {

    private val chromeCastYouTubePlayer = ChromecastYouTubePlayer()
    val chromecastUIController = ChromecastUIController(chromecastControls, chromeCastYouTubePlayer)

    private var youTubePlayer: YouTubePlayer? = null

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()
    private val localPlayerStateTracker = YouTubePlayerStateTracker()

    private var playingOnCastPlayer = false

    init {
        chromeCastYouTubePlayer.addListener(chromecastPlayerStateTracker)
        initLocalYouTube()
    }

    override fun onChromecastConnecting() {
        youTubePlayer?.pause()
    }

    override fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        initializeCastPlayer(chromecastCommunicationChannel)

        playingOnCastPlayer = true
    }

    override fun onChromecastDisconnected() {
        if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
            youTubePlayer?.loadVideo(chromecastPlayerStateTracker.videoId, chromecastPlayerStateTracker.currentSecond)
        else
            youTubePlayer?.cueVideo(chromecastPlayerStateTracker.videoId, chromecastPlayerStateTracker.currentSecond)

        chromecastUIController.resetUI()

        playingOnCastPlayer = false
    }

    private fun initLocalYouTube() {
        lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.initialize({ youtubePlayer ->

            this.youTubePlayer = youtubePlayer
            youtubePlayer.addListener(localPlayerStateTracker)

            youtubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    if(!playingOnCastPlayer)
                        youtubePlayer.loadVideo("6JYIGclVQdw", chromecastPlayerStateTracker.currentSecond)

                    localYouTubePlayerListener.onLocalYouTubePlayerReady()
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
                    youtubePlayer.loadVideo(localPlayerStateTracker.videoId, localPlayerStateTracker.currentSecond)
                }
            })
        }, chromecastCommunicationChannel)
    }

    interface LocalYouTubePlayerListener {
        fun onLocalYouTubePlayerReady()
    }
}