package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.youtubePlayer

import android.arch.lifecycle.Lifecycle
import android.view.View
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.ui.ChromecastUIController
import com.pierfrancescosoffritti.youtubeplayer.player.*
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker

/**
 * Class responsible for
 */
class YouTubePlayersManager(
        private val lifecycle: Lifecycle, private val localYouTubePlayerListener: LocalYouTubePlayerListener,
        private val youtubePlayerView: YouTubePlayerView, chromecastControls: View) : ChromecastConnectionListener {

    val chromecastUIController = ChromecastUIController(chromecastControls)

    private var youTubePlayer: YouTubePlayer? = null

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()
    private val localPlayerStateTracker = YouTubePlayerStateTracker()

    private var playingOnCastPlayer = false

    init {
        initLocalYouTube(playingOnCastPlayer)
    }

    override fun onChromecastConnecting() {
        youTubePlayer?.pause()
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext)

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

    private fun initLocalYouTube(playingOnCastPlayer: Boolean) {
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

    private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

            chromecastUIController.youtubePlayer = youtubePlayer

            youtubePlayer.addListener(chromecastPlayerStateTracker)
            youtubePlayer.addListener(chromecastUIController)

            youtubePlayer.addListener(YouTubePlayerLogger())

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo(localPlayerStateTracker.videoId, localPlayerStateTracker.currentSecond)
                }
            })
        })
    }

    interface LocalYouTubePlayerListener {
        fun onLocalYouTubePlayerReady()
    }
}