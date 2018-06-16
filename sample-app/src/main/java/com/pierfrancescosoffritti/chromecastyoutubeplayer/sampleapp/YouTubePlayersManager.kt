package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker
import kotlinx.android.synthetic.main.activity_main.*

class YouTubePlayersManager(private val mainActivity: MainActivity) : ChromecastConnectionListener {
    private val chromeCastYouTubePlayer = ChromecastYouTubePlayer()
    val chromecastUIController = ChromecastUIController(mainActivity.chromecast_controls_root, chromeCastYouTubePlayer)

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()

    private val localYouTubePlayerManager = LocalPlaybackManager()

    private var playingOnCastPlayer = false

    private var currentSecond: Float = 0f
    private lateinit var lastVideoId: String

    init {
        chromeCastYouTubePlayer.addListener(chromecastPlayerStateTracker)
        initLocalYouTube()
    }

    override fun onChromecastConnecting() {
        localYouTubePlayerManager.pause()
    }

    override fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        initializeCastPlayer(chromecastCommunicationChannel)

        playingOnCastPlayer = true
    }

    override fun onChromecastDisconnected() {
        localYouTubePlayerManager.resume(chromecastPlayerStateTracker.currentState, lastVideoId, currentSecond)

        playingOnCastPlayer = false
    }

    private fun initLocalYouTube() {
        mainActivity.lifecycle.addObserver(mainActivity.youtube_player_view)

        mainActivity.youtube_player_view.initialize({ youtubePlayer ->

            localYouTubePlayerManager.youTubePlayer = youtubePlayer

            youtubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    if(!playingOnCastPlayer)
                        youtubePlayer.loadVideo("6JYIGclVQdw", currentSecond)

                    mainActivity.onLocalYouTubePlayerReady()
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

}