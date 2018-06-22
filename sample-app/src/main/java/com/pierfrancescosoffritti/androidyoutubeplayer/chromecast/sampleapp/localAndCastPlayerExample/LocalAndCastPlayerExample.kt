package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.localAndCastPlayerExample

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.view.View
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.localAndCastPlayerExample.ui.MediaRouteButtonContainer
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouterButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.localAndCastPlayerExample.youtubePlayer.YouTubePlayersManager
import kotlinx.android.synthetic.main.local_and_cast_player_example.*


class LocalAndCastPlayerExample : AppCompatActivity(), YouTubePlayersManager.LocalYouTubePlayerListener, ChromecastConnectionListener {
    private val googlePlayServicesAvailabilityRequestCode = 1

    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouteButton : MediaRouteButton

    private lateinit var notificationManager: NotificationManager

    private lateinit var myBroadcastReceiver: MyBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_and_cast_player_example)

        lifecycle.addObserver(youtube_player_view)

        notificationManager = NotificationManager(this)

        youTubePlayersManager = YouTubePlayersManager(this, youtube_player_view, chromecast_controls_root, notificationManager)
        mediaRouteButton = MediaRouterButtonUtils.initMediaRouteButton(this)

        Log.d(javaClass.simpleName, "on create")

        myBroadcastReceiver = MyBroadcastReceiver(youTubePlayersManager)
        val filter = IntentFilter(MyBroadcastReceiver.TOGGLE_PLAYBACK)
        filter.addAction(MyBroadcastReceiver.STOP_CAST_SESSION)
        applicationContext.registerReceiver(myBroadcastReceiver, filter)

        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) { initChromecast() }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(myBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) {initChromecast()}
    }

    private fun initChromecast() {
        // can't use CastContext until I'm sure the user has GooglePlayServices
        val chromecastYouTubePlayerContext = ChromecastYouTubePlayerContext(
                CastContext.getSharedInstance(this).sessionManager,
                this, myBroadcastReceiver
        )

        lifecycle.addObserver(chromecastYouTubePlayerContext)
    }

    override fun onChromecastConnecting() {
        youTubePlayersManager.onChromecastConnecting()
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        Log.d(javaClass.simpleName, "connected")
        youTubePlayersManager.onChromecastConnected(chromecastYouTubePlayerContext)
        updateUI(true)

        notificationManager.showNotification()
    }

    override fun onChromecastDisconnected() {
        Log.d(javaClass.simpleName, "disconnected")
        youTubePlayersManager.onChromecastDisconnected()
        updateUI(false)

        notificationManager.dismissNotification()
    }

    private fun updateUI(connected: Boolean) {

        val disabledContainer = if(connected) localPlayerUIMediaRouteButtonContainer else chromecastPlayerUIMediaRouteButtonContainer
        val enabledContainer = if(connected) chromecastPlayerUIMediaRouteButtonContainer else localPlayerUIMediaRouteButtonContainer

        // the media route button has a single instance.
        // therefore it has to be moved from the local YouTube player UI to the chromecast YouTube player UI, and vice versa.
        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                disabledContainer, enabledContainer
        )

        youtube_player_view.visibility = if(connected) View.GONE else View.VISIBLE
        chromecast_controls_root.visibility = if(connected) View.VISIBLE else View.GONE
    }

    override fun onLocalYouTubePlayerReady() {
        MediaRouterButtonUtils.addMediaRouteButtonToPlayerUI(
                mediaRouteButton, android.R.color.white,
                null, localPlayerUIMediaRouteButtonContainer
        )
    }

    private val chromecastPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youTubePlayersManager.chromecastUIController.removeView(mediaRouteButton)
    }

    private val localPlayerUIMediaRouteButtonContainer = object: MediaRouteButtonContainer {
        override fun addMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.addView(mediaRouteButton)
        override fun removeMediaRouteButton(mediaRouteButton: MediaRouteButton) = youtube_player_view.playerUIController.removeView(mediaRouteButton)
    }
}