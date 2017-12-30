package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecast

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject

/**
 * Set of constants used for sender-receiver communication
 */
object ChromecastCommunicationConstants {
    // receiver to sender
    val IFRAME_API_READY = "IFRAME_API_READY"
    val READY = "READY"

    // sender to receiver
    val LOAD = "LOAD"

    fun asJson() : JsonObject {
        return jsonObject(
                IFRAME_API_READY to IFRAME_API_READY,
                READY to READY,
                LOAD to LOAD
        )
    }
}