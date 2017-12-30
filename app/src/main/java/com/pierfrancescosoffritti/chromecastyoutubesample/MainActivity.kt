package com.pierfrancescosoffritti.chromecastyoutubesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.cast.framework.*
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.CastListener
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastManager
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.MyYouTubePlayerListener
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubeIOChannel
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CastListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chromecastManager = ChromecastManager(this, this)
        lifecycle.addObserver(chromecastManager)

        CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)
    }

    override fun onSessionStarting() {

    }

    override fun onSessionEnding() {

    }

    override fun setCommunicationChannel(communicationChannelChromecast: ChromecastYouTubeIOChannel) {
        // this should already exist. TODO: defer setChannel from creation
        val chromeCastYouTubePlayer = ChromecastYouTubePlayer(communicationChannelChromecast, YouTubePlayerInitListener { it.addListener(MyYouTubePlayerListener(it)) })
    }
}
