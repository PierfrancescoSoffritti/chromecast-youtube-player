package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.view.View
import com.google.android.gms.cast.framework.CastButtonFactory
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastManager
import com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.utils.MediaRouterButtonUtils

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), YouTubePlayersManager.LocalYouTubePlayerListener, ChromecastConnectionListener {
    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouteButton : MediaRouteButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youTubePlayersManager = YouTubePlayersManager(lifecycle, this, youtube_player_view, chromecast_controls_root)
        mediaRouteButton = initMediaRouteButton()

        val chromecastManager = ChromecastManager(this, this)
        lifecycle.addObserver(chromecastManager)
    }

    override fun onLocalYouTubePlayerReady() {
        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                null, youtube_player_view.playerUIController
        )
    }

    override fun onChromecastConnecting() {
        Log.d(javaClass.simpleName, "onChromecastConnecting")

        youTubePlayersManager.onChromecastConnecting()
    }

    override fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        Log.d(javaClass.simpleName, "onChromecastConnected")

        youTubePlayersManager.onChromecastConnected(chromecastCommunicationChannel)

        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                youtube_player_view.playerUIController, youTubePlayersManager.chromecastUIController
        )

        youtube_player_view.visibility = View.GONE
        chromecast_controls_root.visibility = View.VISIBLE
    }

    override fun onChromecastDisconnected() {
        Log.d(javaClass.simpleName, "onChromecastDisconnected")

        youTubePlayersManager.onChromecastDisconnected()

        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                youTubePlayersManager.chromecastUIController, youtube_player_view.playerUIController
        )

        youtube_player_view.visibility = View.VISIBLE
        chromecast_controls_root.visibility = View.GONE
    }

    private fun initMediaRouteButton() : MediaRouteButton {
        val mediaRouteButton = MediaRouteButton(this)
        CastButtonFactory.setUpMediaRouteButton(this, mediaRouteButton)

        return mediaRouteButton
    }
}
