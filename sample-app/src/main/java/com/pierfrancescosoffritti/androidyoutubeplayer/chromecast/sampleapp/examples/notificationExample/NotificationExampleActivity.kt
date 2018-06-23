package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.notificationExample

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.*
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker
import kotlinx.android.synthetic.main.activity_basic_example.*

/**
 * Simple example showing how to build a notification to control the cast player.
 * In a real application both notification and playback should be managed in a service.
 */
class NotificationExampleActivity : AppCompatActivity() {

    private val googlePlayServicesAvailabilityRequestCode = 1
    private val playbackControllerBroadcastReceiver = PlaybackControllerBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_example)

        registerBroadcastReceiver()

        MediaRouteButtonUtils.initMediaRouteButton(media_route_button)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) { initChromecast() }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(playbackControllerBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // can't use CastContext until I'm sure the user has GooglePlayServices
        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) {initChromecast()}
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter(PlaybackControllerBroadcastReceiver.TOGGLE_PLAYBACK)
        filter.addAction(PlaybackControllerBroadcastReceiver.STOP_CAST_SESSION)
        applicationContext.registerReceiver(playbackControllerBroadcastReceiver, filter)
    }

    private fun initChromecast() {
        val chromecastConnectionListener = SimpleChromecastConnectionListener()
        ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager, chromecastConnectionListener, playbackControllerBroadcastReceiver)
    }

    inner class SimpleChromecastConnectionListener: ChromecastConnectionListener {

        private val notificationManager = NotificationManager(applicationContext, NotificationExampleActivity::class.java)

        override fun onChromecastConnecting() {
            Log.d(javaClass.simpleName, "onChromecastConnecting")
        }

        override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            Log.d(javaClass.simpleName, "onChromecastConnected")

            initializeCastPlayer(chromecastYouTubePlayerContext)
            notificationManager.showNotification()
        }

        override fun onChromecastDisconnected() {
            Log.d(javaClass.simpleName, "onChromecastDisconnected")
            notificationManager.dismissNotification()
        }

        private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

                val playerStateTracker = YouTubePlayerStateTracker()

                initBroadcastReceiver(youtubePlayer, playerStateTracker)

                youtubePlayer.addListener(notificationManager)
                youtubePlayer.addListener(playerStateTracker)

                youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f)
                    }
                })
            })
        }

        private fun initBroadcastReceiver(youTubePlayer: YouTubePlayer, playerStateTracker: YouTubePlayerStateTracker) {
            playbackControllerBroadcastReceiver.togglePlayback = {
                if(playerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                    youTubePlayer.pause()
                else
                    youTubePlayer.play()
            }
        }
    }
}
