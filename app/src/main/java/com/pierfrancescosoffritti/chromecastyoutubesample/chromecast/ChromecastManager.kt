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
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.youtube.ChromecastYouTubeIOChannel
import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.youtube.ChromecastCommunicationConstants

class ChromecastManager(private val context: Context, private val chromecastConnectionListener: ChromecastConnectionListener) : LifecycleObserver {
    private val sessionManager: SessionManager = CastContext.getSharedInstance(context).sessionManager
    private val chromecastCommunicationChannel: ChromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener: CastSessionManagerListener = CastSessionManagerListener(this)

    fun onApplicationConnecting() {
        chromecastConnectionListener.onApplicationConnecting()
    }

    fun onApplicationConnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)

        chromecastConnectionListener.onApplicationConnected(chromecastCommunicationChannel)
    }

    fun onApplicationDisconnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)

        chromecastConnectionListener.onApplicationDisconnected()
    }

    private fun sendCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants = ChromecastCommunicationConstants.asJson()

        val message: JsonObject = jsonObject(
                "command" to ChromecastCommunicationConstants.INIT_COMMUNICATION_CONSTANTS,
                "communicationConstants" to communicationConstants
        )

        chromecastCommunicationChannel.sendMessage(message.toString())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun restoreSession() {
        val currentCastSessions = CastContext.getSharedInstance(context).sessionManager.currentCastSession
        if(currentCastSessions != null)
            onApplicationConnected(currentCastSessions)
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