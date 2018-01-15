package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import com.pierfrancescosoffritti.chromecastyoutubesample.MainActivity
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecastInfrastructure.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_main.*

class YouTubePlayersManager(private val mainActivity: MainActivity) : ChromecastConnectionListener {
    private val chromeCastYouTubePlayer = ChromecastYouTubePlayer()
    val chromecastUIController = ChromecastUIController(mainActivity.chromecast_controls_root, chromeCastYouTubePlayer)

    private var currentSecond: Float = 0f
    private lateinit var lastVideoId: String

    private var playingRemotely = false

    private val localYouTubePlayerBehaviour = LocalPlaybackBehaviour()

    init {
        initLocalYouTube()
    }

    override fun onApplicationConnecting() {
        localYouTubePlayerBehaviour.onApplicationConnecting()
    }

    override fun onApplicationConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        initializeCastPlayer(chromecastCommunicationChannel)

        playingRemotely = true
    }

    override fun onApplicationDisconnected() {
        localYouTubePlayerBehaviour.onApplicationDisconnected(chromeCastYouTubePlayer.currentState, lastVideoId, currentSecond)

        playingRemotely = false
    }

    private fun initLocalYouTube() {
        mainActivity.lifecycle.addObserver(mainActivity.youtube_player_view)

        mainActivity.youtube_player_view.initialize({ initializedYouTubePlayer ->

            localYouTubePlayerBehaviour.youTubePlayer = initializedYouTubePlayer

            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    localYouTubePlayerBehaviour.onLocalYouTubePlayerReady(playingRemotely, currentSecond)

                    mainActivity.onLocalPlayerReady()
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
        chromeCastYouTubePlayer.initialize(chromecastCommunicationChannel, YouTubePlayerInitListener {

            chromeCastYouTubePlayer.addListener(LoggerYouTubePlayerListener())
            chromeCastYouTubePlayer.addListener(chromecastUIController)

            chromeCastYouTubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    chromeCastYouTubePlayer.loadVideo("6JYIGclVQdw", currentSecond)
                }

                override fun onVideoId(videoId: String) {
                    lastVideoId = videoId
                }

                override fun onCurrentSecond(second: Float) {
                    currentSecond = second
                }
            })
        })
    }

}