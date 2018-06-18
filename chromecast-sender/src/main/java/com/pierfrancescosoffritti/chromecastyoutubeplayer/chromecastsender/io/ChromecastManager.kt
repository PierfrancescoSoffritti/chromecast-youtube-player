package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.utils.JSONUtils
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastCommunicationConstants
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.youtube.ChromecastYouTubeIOChannel

internal class ChromecastManager(
        private val chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext,
        private val sessionManager: SessionManager,
        private val chromecastConnectionListener: ChromecastConnectionListener) : CastSessionListener {

    val chromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener = CastSessionManagerListener(this)

    override fun onCastSessionConnecting() {
        chromecastConnectionListener.onChromecastConnecting()
    }

    override fun onCastSessionConnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)

        chromecastYouTubePlayerContext.onChromecastConnected(chromecastYouTubePlayerContext)
        chromecastConnectionListener.onChromecastConnected(chromecastYouTubePlayerContext)
    }

    override fun onCastSessionDisconnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)

        chromecastYouTubePlayerContext.onChromecastDisconnected()
        chromecastConnectionListener.onChromecastDisconnected()
    }

    fun restoreSession() {
        val currentCastSessions = sessionManager.currentCastSession
        if(currentCastSessions != null)
            onCastSessionConnected(currentCastSessions)
    }

    fun addSessionManagerListener() = sessionManager.addSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    fun removeSessionManagerListener() = sessionManager.removeSessionManagerListener(castSessionManagerListener, CastSession::class.java)

    private fun sendCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants = ChromecastCommunicationConstants.asJson()

        val message = JSONUtils.buildCommunicationConstantsJson(
                "command" to ChromecastCommunicationConstants.INIT_COMMUNICATION_CONSTANTS,
                "communicationConstants" to communicationConstants
        )

        chromecastCommunicationChannel.sendMessage(message)
    }
}