package com.pierfrancescosoffritti.chromecastyoutubesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.cast.framework.*
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastContainer
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastManager
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.MyYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ChromecastContainer {

    private lateinit var chromeCastYouTubePlayer : ChromecastYouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chromeCastYouTubePlayer = ChromecastYouTubePlayer()

        initChromecast()

        initUI()
    }

    private fun initUI() {
        play_button.setOnClickListener({chromeCastYouTubePlayer.play()})
        pause_button.setOnClickListener({chromeCastYouTubePlayer.pause()})
    }

    override fun onSessionStarting() {
        // update UI
    }

    override fun onSessionEnding() {
        // update UI
    }

    override fun setCommunicationChannel(communicationChannelChromecast: ChromecastCommunicationChannel) {
        chromeCastYouTubePlayer.initialize(communicationChannelChromecast, YouTubePlayerInitListener { it.addListener(MyYouTubePlayerListener(it)) })
    }

    private fun initChromecast() {
        val chromecastManager = ChromecastManager(this, this)
        lifecycle.addObserver(chromecastManager)

        CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)
    }
}
