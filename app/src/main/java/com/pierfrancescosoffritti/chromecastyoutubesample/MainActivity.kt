package com.pierfrancescosoffritti.chromecastyoutubesample

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import com.google.android.gms.cast.framework.CastButtonFactory
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.ChromecastManager
import com.pierfrancescosoffritti.chromecastyoutubesample.app.YouTubePlayersManager
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ChromecastConnectionListener {
    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouterButton : MediaRouteButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youTubePlayersManager = YouTubePlayersManager(this)

        initChromecast()
    }

    fun onLocalPlayerReady() {
        if(mediaRouterButton.parent != null)
            return

        setMediaRouterButtonTint(mediaRouterButton, android.R.color.white)
        youtube_player_view.playerUIController.addView(mediaRouterButton)
    }

    override fun onApplicationConnecting() {
        youTubePlayersManager.onApplicationConnecting()
    }

    override fun onApplicationConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        Log.d(javaClass.simpleName, "onApplicationConnected to Chromecast")

        youTubePlayersManager.onApplicationConnected(chromecastCommunicationChannel)

        addMediaRouterButtonToActivePlayerUI(mediaRouterButton, android.R.color.white, youtube_player_view.playerUIController, youTubePlayersManager.chromecastUIController)

        youtube_player_view.visibility = View.GONE
        chromecast_controls_root.visibility = View.VISIBLE
    }

    override fun onApplicationDisconnected() {
        Log.d(javaClass.simpleName, "onApplicationDisconnected from Chromecast")

        youTubePlayersManager.onApplicationDisconnected()

        addMediaRouterButtonToActivePlayerUI(mediaRouterButton, android.R.color.white, youTubePlayersManager.chromecastUIController, youtube_player_view.playerUIController)

        youtube_player_view.visibility = View.VISIBLE
        chromecast_controls_root.visibility = View.GONE
    }

    private fun addMediaRouterButtonToActivePlayerUI(mediaRouterButton: MediaRouteButton, tintColor: Int, playerUIControllerDisabled: PlayerUIController, playerUIControllerActivated: PlayerUIController) {
        setMediaRouterButtonTint(mediaRouterButton, tintColor)
        playerUIControllerDisabled.removeView(mediaRouterButton)
        playerUIControllerActivated.addView(mediaRouterButton)
    }

    private fun initChromecast() {
        val chromecastManager = ChromecastManager(this, this)
        lifecycle.addObserver(chromecastManager)

        initMediaRouterButton()
    }

    private fun initMediaRouterButton() {
        mediaRouterButton = MediaRouteButton(this)
        CastButtonFactory.setUpMediaRouteButton(this, mediaRouterButton)
    }

    private fun setMediaRouterButtonTint(mediaRouterButton: MediaRouteButton, color: Int) {
        val castContext = ContextThemeWrapper(this, android.support.v7.mediarouter.R.style.Theme_MediaRouter)
        val styledAttributes = castContext.obtainStyledAttributes(null, android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0)
        val drawable = styledAttributes.getDrawable(android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable)

        styledAttributes.recycle()
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, color))

        mediaRouterButton.setRemoteIndicatorDrawable(drawable)
    }
}
