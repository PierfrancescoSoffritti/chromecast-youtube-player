package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.CastSessionManagerListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.utils.JSONUtils
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubeIOChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastCommunicationConstants

class ChromecastManager(private val sessionManager: SessionManager, private val chromecastConnectionListener: ChromecastConnectionListener) : LifecycleObserver  {
    private val chromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener = CastSessionManagerListener(this)

    fun onCastSessionConnecting() {
        chromecastConnectionListener.onChromecastConnecting()
    }

    fun onCastSessionConnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)

        chromecastConnectionListener.onChromecastConnected(chromecastCommunicationChannel)
    }

    fun onCastSessionDisconnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)

        chromecastConnectionListener.onChromecastDisconnected()
    }

    private fun sendCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants = ChromecastCommunicationConstants.asJson()

        val message = JSONUtils.buildCommunicationConstantsJson(
                "command" to ChromecastCommunicationConstants.INIT_COMMUNICATION_CONSTANTS,
                "communicationConstants" to communicationConstants
        )

        chromecastCommunicationChannel.sendMessage(message)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun restoreSession() {
        val currentCastSessions = sessionManager.currentCastSession
        if(currentCastSessions != null)
            onCastSessionConnected(currentCastSessions)
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