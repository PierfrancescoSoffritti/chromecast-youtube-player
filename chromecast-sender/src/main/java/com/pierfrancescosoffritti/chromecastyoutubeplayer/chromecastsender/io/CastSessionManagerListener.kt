package com.pierfrancescosoffritti.chromecastyoutubeplayer.chromecastsender.io

import android.util.Log
import com.google.android.gms.cast.framework.*

internal class CastSessionManagerListener(private val castSessionListener: CastSessionListener) : SessionManagerListener<CastSession> {

    override fun onSessionEnding(castSession: CastSession) { Log.d(javaClass.simpleName, "session ending, NO-OP") }
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { Log.d(javaClass.simpleName, "session suspended, NO-OP") }

    override fun onSessionStarting(castSession: CastSession) = castSessionListener.onCastSessionConnecting()
    override fun onSessionResuming(castSession: CastSession, p1: String) = castSessionListener.onCastSessionConnecting()

    override fun onSessionEnded(castSession: CastSession, error: Int) = castSessionListener.onCastSessionDisconnected(castSession)
    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) = castSessionListener.onCastSessionConnected(castSession)
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) = castSessionListener.onCastSessionDisconnected(castSession)
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = castSessionListener.onCastSessionConnected(castSession)
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) = castSessionListener.onCastSessionConnected(castSession)

}