package com.pierfrancescosoffritti.chromecastyoutubesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.cast.framework.*
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.ChromeCastYouTubePlayer
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.MyYouTubePlayerListener
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.ChromeCastCustomChannel
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var sessionManagerListener: SessionManagerListener<CastSession>
    private lateinit var chromeCastCustomChannel: ChromeCastCustomChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = CastContext.getSharedInstance(this).sessionManager

        chromeCastCustomChannel = ChromeCastCustomChannel(sessionManager)
        sessionManagerListener = MySessionManagerListener(chromeCastCustomChannel)

        val chromeCastYouTubePlayer = ChromeCastYouTubePlayer(chromeCastCustomChannel, YouTubePlayerInitListener { it.addListener(MyYouTubePlayerListener()) })

        CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)
    }

    override fun onResume() {
        super.onResume()
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    override fun onPause() {
        super.onPause()
        sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }
}
