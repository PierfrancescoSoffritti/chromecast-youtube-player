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
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.utils.JSONUtils
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ChromecastConnectionListener {
    private lateinit var youTubePlayersManager: YouTubePlayersManager
    private lateinit var mediaRouteButton : MediaRouteButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youTubePlayersManager = YouTubePlayersManager(this)

        initChromecastManager()
        initMediaRouteButton()

        val testJson = JSONUtils.buildFlatJson(Pair("par1", "val1"), Pair("par2", "val2"))
        val messageFromReceiver = JSONUtils.parseMessageFromReceiverJson(testJson)

//        Log.d(javaClass.simpleName, testJson)
        Log.d(javaClass.simpleName, messageFromReceiver.toString())
    }

    fun onLocalYouTubePlayerReady() {
        if(mediaRouteButton.parent != null)
            return

        setMediaRouterButtonTint(mediaRouteButton, android.R.color.white)
        youtube_player_view.playerUIController.addView(mediaRouteButton)
    }

    override fun onChromecastConnecting() {
        youTubePlayersManager.onChromecastConnecting()
    }

    override fun onChromecastConnected(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        Log.d(javaClass.simpleName, "onChromecastConnected to Chromecast")

        youTubePlayersManager.onChromecastConnected(chromecastCommunicationChannel)

        addMediaRouteButtonToPlayerUI(mediaRouteButton, android.R.color.white, youtube_player_view.playerUIController, youTubePlayersManager.chromecastUIController)

        youtube_player_view.visibility = View.GONE
        chromecast_controls_root.visibility = View.VISIBLE
    }

    override fun onChromecastDisconnected() {
        Log.d(javaClass.simpleName, "resume from Chromecast")

        youTubePlayersManager.onChromecastDisconnected()

        addMediaRouteButtonToPlayerUI(mediaRouteButton, android.R.color.white, youTubePlayersManager.chromecastUIController, youtube_player_view.playerUIController)

        youtube_player_view.visibility = View.VISIBLE
        chromecast_controls_root.visibility = View.GONE
    }

    private fun addMediaRouteButtonToPlayerUI(mediaRouteButton: MediaRouteButton, tintColor: Int, playerUIControllerDisabled: PlayerUIController, playerUIControllerActivated: PlayerUIController) {
        setMediaRouterButtonTint(mediaRouteButton, tintColor)
        playerUIControllerDisabled.removeView(mediaRouteButton)
        playerUIControllerActivated.addView(mediaRouteButton)
    }

    private fun initChromecastManager() {
        val chromecastManager = ChromecastManager(this, this)
        lifecycle.addObserver(chromecastManager)
    }

    private fun initMediaRouteButton() {
        mediaRouteButton = MediaRouteButton(this)
        CastButtonFactory.setUpMediaRouteButton(this, mediaRouteButton)
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
