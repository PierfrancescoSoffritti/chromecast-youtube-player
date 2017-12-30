package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.github.salomonbrys.kotson.jsonObject
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubeIOChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastCommunicationConstants

class ChromecastManager(context: Context, private val castListener: CastListener) : LifecycleObserver {
    private val sessionManager: SessionManager = CastContext.getSharedInstance(context).sessionManager
    private val chromecastYouTubeIOChannel: ChromecastYouTubeIOChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val sessionManagerListener : MySessionManagerListener = MySessionManagerListener(this)

    fun onSessionStarting() = castListener.onSessionStarting()
    fun onSessionEnding() = castListener.onSessionEnding()
    fun onSessionStarted(castSession: CastSession) {
        castSession.setMessageReceivedCallbacks(chromecastYouTubeIOChannel.namespace, chromecastYouTubeIOChannel)

        exchangeCommunicationConstants(chromecastYouTubeIOChannel)
        castListener.setCommunicationChannel(chromecastYouTubeIOChannel)
    }

    private fun exchangeCommunicationConstants(chromecastYouTubeIOChannel: ChromecastYouTubeIOChannel) {
        val communicationConstants: JsonObject = jsonObject(
                ChromecastCommunicationConstants.IFRAME_API_READY to ChromecastCommunicationConstants.IFRAME_API_READY,
                ChromecastCommunicationConstants.READY to ChromecastCommunicationConstants.READY,
                ChromecastCommunicationConstants.LOAD to ChromecastCommunicationConstants.LOAD
        )

        chromecastYouTubeIOChannel.sendMessage(communicationConstants.toString())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addSessionManagerListener() {
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeSessionManagerListener() {
        sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }
}