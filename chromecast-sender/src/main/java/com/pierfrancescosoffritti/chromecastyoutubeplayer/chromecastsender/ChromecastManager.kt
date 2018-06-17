package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.CastSessionManagerListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastCommunicationChannel
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO.ChromecastConnectionListener
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.utils.JSONUtils
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastCommunicationConstants
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubeIOChannel

internal class ChromecastManager(
        private val chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext,
        private val sessionManager: SessionManager,
        private val chromecastConnectionListener: ChromecastConnectionListener) {

    val chromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener = CastSessionManagerListener(this)

    fun onCastSessionConnecting() {
        chromecastConnectionListener.onChromecastConnecting()
    }

    fun onCastSessionConnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)

        chromecastYouTubePlayerContext.chromecastConnected = true
        chromecastConnectionListener.onChromecastConnected(chromecastYouTubePlayerContext)
    }

    fun onCastSessionDisconnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)

        chromecastYouTubePlayerContext.chromecastConnected = false
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

    fun restoreSession() {
        val currentCastSessions = sessionManager.currentCastSession
        if(currentCastSessions != null)
            onCastSessionConnected(currentCastSessions)
    }

    fun addSessionManagerListener() {
        sessionManager.addSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    }

    fun removeSessionManagerListener() {
        sessionManager.removeSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    }
}