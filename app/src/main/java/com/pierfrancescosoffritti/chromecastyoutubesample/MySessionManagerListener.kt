package com.pierfrancescosoffritti.chromecastyoutubesample

import android.util.Log
import com.google.android.gms.cast.framework.Session
import com.google.android.gms.cast.framework.SessionManagerListener

class MySessionManagerListener(val mainActivity: MainActivity) : SessionManagerListener<Session> {
    override fun onSessionSuspended(p0: Session?, p1: Int) {}
    override fun onSessionResumeFailed(p0: Session?, p1: Int) {}
    override fun onSessionStarting(p0: Session?) { }
    override fun onSessionEnding(p0: Session?) {}
    override fun onSessionResuming(p0: Session?, p1: String?) {}
    override fun onSessionStartFailed(p0: Session?, p1: Int) {}

    override fun onSessionStarted(session: Session, sessionId: String) {
        mainActivity.sendMessage("{ \"text\": \"session started!!!!!\" }")
    }

    override fun onSessionResumed(session: Session, wasSuspended: Boolean) {
        Log.d(javaClass.simpleName, "session resumed");
    }

    override fun onSessionEnded(session: Session, error: Int) {
        Log.d(javaClass.simpleName, "session ended");
    }
}