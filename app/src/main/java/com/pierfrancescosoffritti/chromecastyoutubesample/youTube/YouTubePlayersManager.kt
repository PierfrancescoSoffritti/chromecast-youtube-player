package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.view.View
import com.pierfrancescosoffritti.chromecastyoutubesample.MainActivity
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecastInfrastructure.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_main.*

class YouTubePlayersManager(private val mainActivity: MainActivity) : ChromecastConnectionListener {
    private val chromeCastYouTubePlayer = ChromecastYouTubePlayer()
    val chromecastUIController = ChromecastUIController(mainActivity.chromecast_controls_root, chromeCastYouTubePlayer)

    private lateinit var initializedLocalYouTubePlayer: YouTubePlayer

    private var currentSecond: Float = 0f

    init {
        initLocalYouTube()
    }

    override fun onApplicationConnecting() {
        initializedLocalYouTubePlayer.pause()
    }

    override fun onApplicationConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        initializeCastPlayer(chromecastCommunicationChannel)

        mainActivity.youtube_player_view.visibility = View.GONE
        mainActivity.chromecast_controls_root.visibility = View.VISIBLE
    }

    override fun onApplicationDisconnected() {

        mainActivity.youtube_player_view.visibility = View.VISIBLE
        mainActivity.chromecast_controls_root.visibility = View.GONE
    }

    private fun initLocalYouTube() {
        mainActivity.lifecycle.addObserver(mainActivity.youtube_player_view)

        mainActivity.youtube_player_view.initialize({ initializedYouTubePlayer ->

            initializedLocalYouTubePlayer = initializedYouTubePlayer

            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    val videoId = "6JYIGclVQdw"
                    initializedYouTubePlayer.loadVideo(videoId, 0f)

                    mainActivity.onLocalPlayerReady()
                }

                override fun onCurrentSecond(second: Float) {
                    super.onCurrentSecond(second)
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
                    super.onReady()
                    chromeCastYouTubePlayer.loadVideo("6JYIGclVQdw", currentSecond)
                }
            })
        })
    }

}