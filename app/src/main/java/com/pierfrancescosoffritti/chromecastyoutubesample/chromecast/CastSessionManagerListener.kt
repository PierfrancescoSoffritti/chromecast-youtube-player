package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import android.util.Log
import com.google.android.gms.cast.framework.*

// TODO: consider passing an interface instead of the class ChromecastManager
class CastSessionManagerListener(private val chromecastManager: ChromecastManager) : SessionManagerListener<CastSession> {

    override fun onSessionStarting(p0: CastSession?) { Log.d(javaClass.simpleName, "session starting, NOP") }
    override fun onSessionEnding(castSession: CastSession) { Log.d(javaClass.simpleName, "session ending, NOP") }
    override fun onSessionResuming(castSession: CastSession, p1: String) { Log.d(javaClass.simpleName, "session resuming, NOP") }
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { Log.d(javaClass.simpleName, "session suspended, NOP") }

    override fun onSessionEnded(castSession: CastSession, error: Int) = chromecastManager.onApplicationDisconnected(castSession)
    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) = chromecastManager.onApplicationConnected(castSession)
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) = chromecastManager.onApplicationDisconnected(castSession)
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = chromecastManager.onApplicationConnected(castSession)
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) = chromecastManager.onApplicationConnected(castSession)

}