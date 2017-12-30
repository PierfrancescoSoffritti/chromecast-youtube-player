package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import android.util.Log
import com.google.android.gms.cast.framework.*

// TODO: pass an interface, not the class
class MySessionManagerListener(private val chromecastManager: ChromecastManager) : SessionManagerListener<CastSession> {
    override fun onSessionSuspended(castSession: CastSession, p1: Int) {}
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) {}
    override fun onSessionResuming(castSession: CastSession, p1: String) {}
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) {}

    override fun onSessionStarting(castSession: CastSession) = chromecastManager.onSessionStarting()
    override fun onSessionEnding(castSession: CastSession) = chromecastManager.onSessionEnding()
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = chromecastManager.onSessionStarted(castSession)

    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) {
        Log.d(javaClass.simpleName, "session resumed")
    }

    override fun onSessionEnded(castSession: CastSession, error: Int) {
        Log.d(javaClass.simpleName, "session ended")
    }
}