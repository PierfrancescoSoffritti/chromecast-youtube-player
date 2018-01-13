package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastYouTubeIOChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast.ChromecastCommunicationConstants

class ChromecastManager(context: Context, private val chromecastContainer: ChromecastContainer) : LifecycleObserver {
    private val sessionManager: SessionManager = CastContext.getSharedInstance(context).sessionManager
    private val chromecastCommunicationChannel: ChromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener: CastSessionManagerListener = CastSessionManagerListener(this)

    fun onSessionStarting() = chromecastContainer.onSessionStarting()
    fun onSessionEnding() = chromecastContainer.onSessionEnding()
    fun onSessionStarted(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)
        chromecastContainer.setCommunicationChannel(chromecastCommunicationChannel)
    }

    fun onSessionResuming(castSession: CastSession) {
        chromecastContainer.onSessionResuming()
    }

    private fun sendCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants = ChromecastCommunicationConstants.asJson()
        chromecastCommunicationChannel.sendMessage(communicationConstants.toString())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addSessionManagerListener() {
        sessionManager.addSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeSessionManagerListener() {
        sessionManager.removeSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    }
}