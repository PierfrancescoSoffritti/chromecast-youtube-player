package com.pierfrancescosoffritti.chromecastyoutubesample

import android.util.Log
import com.github.salomonbrys.kotson.jsonObject
import com.google.android.gms.cast.framework.*
import com.google.gson.JsonObject
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.Constants
import com.pierfrancescosoffritti.chromecastyoutubesample.youTube.ChromecastCommunicationChannel

// this class must be refactored
class MySessionManagerListener(private val chromecastCommunicationChannel: ChromecastCommunicationChannel) : SessionManagerListener<CastSession> {
    override fun onSessionSuspended(castSession: CastSession?, p1: Int) {}
    override fun onSessionResumeFailed(castSession: CastSession?, p1: Int) {}

    override fun onSessionStarting(castSession: CastSession) {}
    override fun onSessionEnding(castSession: CastSession?) {}
    override fun onSessionResuming(castSession: CastSession?, p1: String?) {}

    override fun onSessionStartFailed(castSession: CastSession?, p1: Int) {}

    override fun onSessionStarted(castSession: CastSession, sessionId: String) {
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        exchangeCommunicationConstants(chromecastCommunicationChannel)
    }

    private fun exchangeCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants: JsonObject = jsonObject(
                Constants.IFRAME_API_READY to Constants.IFRAME_API_READY,
                Constants.READY to Constants.READY,
                Constants.LOAD to Constants.LOAD
        )

        chromecastCommunicationChannel.sendMessage(communicationConstants.toString())
    }

    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) {
        Log.d(javaClass.simpleName, "session resumed")
    }

    override fun onSessionEnded(castSession: CastSession, error: Int) {
        Log.d(javaClass.simpleName, "session ended")
    }
}