package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener

class ChromecastYouTubePlayerContext(sessionManager: SessionManager, chromecastConnectionListener: ChromecastConnectionListener) : LifecycleObserver {
    private val chromecastManager = ChromecastManager(this, sessionManager, chromecastConnectionListener)
    private val chromecastYouTubePlayer = ChromecastYouTubePlayer(chromecastManager.chromecastCommunicationChannel)

    var chromecastConnected = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() = chromecastManager.restoreSession()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = chromecastManager.addSessionManagerListener()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() = chromecastManager.removeSessionManagerListener()

    fun initialize(youTubePlayerInitListener: YouTubePlayerInitListener) {
        if(!chromecastConnected)
            throw RuntimeException("ChromecastYouTubePlayerContext, can't initialize before chromecast connection is established.")

        chromecastYouTubePlayer.initialize(youTubePlayerInitListener)
    }
}