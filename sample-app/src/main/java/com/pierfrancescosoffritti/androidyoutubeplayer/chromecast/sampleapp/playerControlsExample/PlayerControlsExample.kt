package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.playerControlsExample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouterButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlayServicesUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlaybackUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.SimpleChromecastUIController
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import kotlinx.android.synthetic.main.activity_player_controls_example.*

class PlayerControlsExample : AppCompatActivity() {

    private val googlePlayServicesAvailabilityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_controls_example)

        MediaRouterButtonUtils.initMediaRouteButton(media_route_button)

        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) { initChromecast() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == googlePlayServicesAvailabilityRequestCode)
            PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) {initChromecast()}
    }

    private fun initChromecast() {
        // can't use CastContext until I'm sure the user has GooglePlayServices
        val chromecastYouTubePlayerContext =
                ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager, SimpleChromecastConnectionListener(chromecast_controls_root))
        lifecycle.addObserver(chromecastYouTubePlayerContext)
    }

    private class SimpleChromecastConnectionListener(private val chromecast_controls_root: View) : ChromecastConnectionListener {

        val chromecastUIController = SimpleChromecastUIController(chromecast_controls_root)
        val connectionLabel = chromecast_controls_root.findViewById<TextView>(R.id.connection_status)!!

        override fun onChromecastConnecting() {
            Log.d(javaClass.simpleName, "onChromecastConnecting")
            connectionLabel.text = "connecting to chromecast..."
        }

        override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            Log.d(javaClass.simpleName, "onChromecastConnected")
            connectionLabel.text = "connected to chromecast"

            initializeCastPlayer(chromecastYouTubePlayerContext)
        }

        override fun onChromecastDisconnected() {
            Log.d(javaClass.simpleName, "onChromecastDisconnected")
            connectionLabel.text = "not connected to chromecast"
            chromecastUIController.resetUI()
        }

        private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
            chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

                chromecastUIController.youTubePlayer = youtubePlayer

                chromecast_controls_root
                        .findViewById<Button>(R.id.next_video_button)
                        .setOnClickListener { youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f) }

                youtubePlayer.addListener(chromecastUIController)

                youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f)
                    }
                })

            })
        }
    }
}
