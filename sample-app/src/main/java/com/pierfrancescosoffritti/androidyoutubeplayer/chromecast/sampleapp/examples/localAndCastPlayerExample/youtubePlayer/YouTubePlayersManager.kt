package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localAndCastPlayerExample.youtubePlayer

import android.view.View
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localAndCastPlayerExample.ui.ChromecastUIController
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlaybackUtils
import com.pierfrancescosoffritti.youtubeplayer.player.*
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker

class YouTubePlayersManager(
        private val localYouTubePlayerListener: LocalYouTubePlayerListener,
        private val youtubePlayerView: YouTubePlayerView, chromecastControls: View,
        private val chromecastPlayerListener: YouTubePlayerListener) : ChromecastConnectionListener {

    private val nextVideoButton = chromecastControls.findViewById<Button>(R.id.next_video_button)

    val chromecastUIController = ChromecastUIController(chromecastControls)

    private var localYouTubePlayer: YouTubePlayer? = null
    private var chromecastYouTubePlayer: YouTubePlayer? = null

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()
    private val localPlayerStateTracker = YouTubePlayerStateTracker()

    private var playingOnCastPlayer = false

    init {
        initLocalYouTube()
        nextVideoButton.setOnClickListener { chromecastYouTubePlayer?.loadVideo(PlaybackUtils.getNextVideoId(), 0f) }
    }

    override fun onChromecastConnecting() {
        localYouTubePlayer?.pause()
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext)

        playingOnCastPlayer = true
    }

    override fun onChromecastDisconnected() {
        if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
            localYouTubePlayer?.loadVideo(chromecastPlayerStateTracker.videoId, chromecastPlayerStateTracker.currentSecond)
        else
            localYouTubePlayer?.cueVideo(chromecastPlayerStateTracker.videoId, chromecastPlayerStateTracker.currentSecond)

        chromecastUIController.resetUI()

        playingOnCastPlayer = false
    }

    private fun initLocalYouTube() {
        youtubePlayerView.initialize({ youtubePlayer ->

            this.localYouTubePlayer = youtubePlayer
            youtubePlayer.addListener(localPlayerStateTracker)

            youtubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    if(!playingOnCastPlayer)
                        youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), chromecastPlayerStateTracker.currentSecond)

                    localYouTubePlayerListener.onLocalYouTubePlayerReady()
                }

                override fun onCurrentSecond(second: Float) {
                    if(playingOnCastPlayer && localPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                        youtubePlayer.pause()
                }
            })

        }, true)
    }

    private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

            chromecastYouTubePlayer = youtubePlayer

            chromecastUIController.youtubePlayer = youtubePlayer

            youtubePlayer.addListener(chromecastPlayerListener)
            youtubePlayer.addListener(chromecastPlayerStateTracker)
            youtubePlayer.addListener(chromecastUIController)

//            youtubePlayer.addListener(YouTubePlayerLogger())

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo(localPlayerStateTracker.videoId, localPlayerStateTracker.currentSecond)
                }
            })
        })
    }

    fun togglePlayback() {
        if(playingOnCastPlayer)
            if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                chromecastYouTubePlayer?.pause()
            else
                chromecastYouTubePlayer?.play()
        else
            if(localPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer?.pause()
            else
                localYouTubePlayer?.play()
    }

    interface LocalYouTubePlayerListener {
        fun onLocalYouTubePlayerReady()
    }
}