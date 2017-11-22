package com.pierfrancescosoffritti.chromecastyoutubesample

import android.util.Log
import com.github.salomonbrys.kotson.jsonObject
import com.google.android.gms.cast.framework.*
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.Constants
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.ChromeCastCustomChannel

class MySessionManagerListener(private val chromeCastCustomChannel: ChromeCastCustomChannel) : SessionManagerListener<CastSession> {
    override fun onSessionSuspended(castSession: CastSession?, p1: Int) {}
    override fun onSessionResumeFailed(castSession: CastSession?, p1: Int) {}

    override fun onSessionStarting(castSession: CastSession) {

    }
    override fun onSessionEnding(castSession: CastSession?) {}
    override fun onSessionResuming(castSession: CastSession?, p1: String?) {}

    override fun onSessionStartFailed(castSession: CastSession?, p1: Int) {}

    override fun onSessionStarted(castSession: CastSession, sessionId: String) {
        castSession.setMessageReceivedCallbacks(chromeCastCustomChannel.namespace, chromeCastCustomChannel)

        val obj: JsonObject = jsonObject(
                "IframeAPIReady" to Constants.IFRAME_API_READY,
                "Ready" to Constants.READY
        )

        chromeCastCustomChannel.sendMessage(obj.toString())

//        chromeCastCustomChannel.sendMessage("{ \"text\": \"loadYouTubePlayer\" }")
    }

    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) {
        Log.d(javaClass.simpleName, "session resumed")
    }

    override fun onSessionEnded(castSession: CastSession, error: Int) {
        Log.d(javaClass.simpleName, "session ended")
    }
}