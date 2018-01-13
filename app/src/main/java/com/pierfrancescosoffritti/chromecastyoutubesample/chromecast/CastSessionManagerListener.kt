package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import android.util.Log
import com.google.android.gms.cast.framework.*

// TODO: consider passing an interface instead of the class ChromecastManager
class CastSessionManagerListener(private val chromecastManager: ChromecastManager) : SessionManagerListener<CastSession> {
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { Log.d(javaClass.simpleName, "session suspended") }
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) {}
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) {}


    override fun onSessionStarting(castSession: CastSession) = chromecastManager.onSessionStarting()
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = chromecastManager.onSessionStarted(castSession)
    override fun onSessionResuming(castSession: CastSession, p1: String) = chromecastManager.onSessionResuming(castSession)
    override fun onSessionEnding(castSession: CastSession) = chromecastManager.onSessionEnding()

//    override fun onSessionStarting(castSession: CastSession) { Log.d(javaClass.simpleName, "session starting") }
//    override fun onSessionStarted(castSession: CastSession, sessionId: String) { Log.d(javaClass.simpleName, "session started") }
//    override fun onSessionResuming(castSession: CastSession, p1: String) { Log.d(javaClass.simpleName, "session resuming") }
//    override fun onSessionEnding(castSession: CastSession) { Log.d(javaClass.simpleName, "session ending") }

    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) {
        Log.d(javaClass.simpleName, "session resumed")
    }

    override fun onSessionEnded(castSession: CastSession, error: Int) {
        Log.d(javaClass.simpleName, "session ended")
    }
}