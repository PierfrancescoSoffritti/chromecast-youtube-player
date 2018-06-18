package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.view.View
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.common.ConnectionResult.SUCCESS
import com.google.android.gms.common.GoogleApiAvailability
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.ui.MediaRouteButtonContainer
import com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.utils.MediaRouterButtonUtils
import com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.youtubePlayer.YouTubePlayersManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), YouTubePlayersManager.LocalYouTubePlayerListener, ChromecastConnectionListener {
    private val requestCodeGooglePlayServicesAvailability = 1

    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouteButton : MediaRouteButton

    private val chromecastPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.removeView(mediaRouteButton)
    }

    private val localPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.removeView(mediaRouteButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youTubePlayersManager = YouTubePlayersManager(lifecycle, this, youtube_player_view, chromecast_controls_root)
        mediaRouteButton = MediaRouterButtonUtils.initMediaRouteButton(this)
    }

    override fun onResume() {
        super.onResume()
        checkGooglePlayServicesAvailability()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == requestCodeGooglePlayServicesAvailability)
            checkGooglePlayServicesAvailability()
    }

    private fun checkGooglePlayServicesAvailability() {
        val googlePlayServicesAvailabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if(googlePlayServicesAvailabilityResult == SUCCESS)
            initChromecastYouTubePlayer()
        else
            GoogleApiAvailability.getInstance().getErrorDialog(this, googlePlayServicesAvailabilityResult, requestCodeGooglePlayServicesAvailability, null).show()
    }

    private fun initChromecastYouTubePlayer() {
        val chromecastYouTubePlayerContext = ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager, this)
        lifecycle.addObserver(chromecastYouTubePlayerContext)
    }

    override fun onChromecastConnecting() {
        Log.d(javaClass.simpleName, "onChromecastConnecting")

        youTubePlayersManager.onChromecastConnecting()
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        Log.d(javaClass.simpleName, "onChromecastConnected")

        youTubePlayersManager.onChromecastConnected(chromecastYouTubePlayerContext)

        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                localPlayerUIMediaRouteButtonContainer, chromecastPlayerUIMediaRouteButtonContainer
        )

        youtube_player_view.visibility = View.GONE
        chromecast_controls_root.visibility = View.VISIBLE
    }

    override fun onChromecastDisconnected() {
        Log.d(javaClass.simpleName, "onChromecastDisconnected")

        youTubePlayersManager.onChromecastDisconnected()

        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                chromecastPlayerUIMediaRouteButtonContainer, localPlayerUIMediaRouteButtonContainer
        )

        youtube_player_view.visibility = View.VISIBLE
        chromecast_controls_root.visibility = View.GONE
    }

    override fun onLocalYouTubePlayerReady() {
        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                null, localPlayerUIMediaRouteButtonContainer
        )
    }
}