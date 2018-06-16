package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender

import android.util.Log
import com.google.android.gms.cast.framework.*

// TODO: consider passing an interface instead of the class ChromecastManager
class CastSessionManagerListener(private val chromecastManager: ChromecastManager) : SessionManagerListener<CastSession> {

    override fun onSessionEnding(castSession: CastSession) { Log.d(javaClass.simpleName, "session ending, NO-OP") }
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { Log.d(javaClass.simpleName, "session suspended, NO-OP") }

    override fun onSessionStarting(castSession: CastSession) { chromecastManager.onApplicationConnecting() }
    override fun onSessionResuming(castSession: CastSession, p1: String) { chromecastManager.onApplicationConnecting() }

    override fun onSessionEnded(castSession: CastSession, error: Int) = chromecastManager.onApplicationDisconnected(castSession)
    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) = chromecastManager.onApplicationConnected(castSession)
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) = chromecastManager.onApplicationDisconnected(castSession)
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = chromecastManager.onApplicationConnected(castSession)
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) = chromecastManager.onApplicationConnected(castSession)

}