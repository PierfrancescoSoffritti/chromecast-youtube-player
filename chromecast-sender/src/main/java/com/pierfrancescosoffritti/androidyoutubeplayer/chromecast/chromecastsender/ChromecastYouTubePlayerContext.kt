package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.youtube.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener

class ChromecastYouTubePlayerContext(sessionManager: SessionManager, chromecastConnectionListener: ChromecastConnectionListener) : LifecycleObserver, ChromecastConnectionListener {
    private val chromecastManager = ChromecastManager(this, sessionManager, chromecastConnectionListener)
    private val chromecastYouTubePlayer = ChromecastYouTubePlayer(chromecastManager.chromecastCommunicationChannel)

    private var chromecastConnected = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() = chromecastManager.restoreSession()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = chromecastManager.addSessionManagerListener()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() = chromecastManager.removeSessionManagerListener()

    fun initialize(youTubePlayerInitListener: YouTubePlayerInitListener) {
        if(!chromecastConnected)
            throw RuntimeException("ChromecastYouTubePlayerContext, can't initialize before Chromecast connection is established.")

        chromecastYouTubePlayer.initialize(youTubePlayerInitListener)
    }

    override fun onChromecastConnecting() {
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastConnected = true
    }

    override fun onChromecastDisconnected() {
        chromecastConnected = false
    }
}