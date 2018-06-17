package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.castIO

import android.util.Log
import com.google.android.gms.cast.framework.*
import com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.ChromecastManager

internal class CastSessionManagerListener(private val chromecastManager: ChromecastManager) : SessionManagerListener<CastSession> {

    override fun onSessionEnding(castSession: CastSession) { Log.d(javaClass.simpleName, "session ending, NO-OP") }
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { Log.d(javaClass.simpleName, "session suspended, NO-OP") }

    override fun onSessionStarting(castSession: CastSession) = chromecastManager.onCastSessionConnecting()
    override fun onSessionResuming(castSession: CastSession, p1: String) = chromecastManager.onCastSessionConnecting()

    override fun onSessionEnded(castSession: CastSession, error: Int) = chromecastManager.onCastSessionDisconnected(castSession)
    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) = chromecastManager.onCastSessionConnected(castSession)
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) = chromecastManager.onCastSessionDisconnected(castSession)
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = chromecastManager.onCastSessionConnected(castSession)
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) = chromecastManager.onCastSessionConnected(castSession)

}