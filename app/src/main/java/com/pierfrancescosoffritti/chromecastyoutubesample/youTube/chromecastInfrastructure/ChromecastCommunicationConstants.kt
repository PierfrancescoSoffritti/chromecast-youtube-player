package com.pierfrancescosoffritti.chromecastyoutubesample.youTube.chromecastInfrastructure

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject

/**
 * Set of constants used for sender-receiver communication
 */
object ChromecastCommunicationConstants {
    // receiver to sender
    val INIT_COMMUNICATION_CONSTANTS = "INIT_COMMUNICATION_CONSTANTS"

    val IFRAME_API_READY = "IFRAME_API_READY"
    val READY = "READY"
    val STATE_CHANGED = "STATE_CHANGED"
    val PLAYBACK_QUALITY_CHANGED = "PLAYBACK_QUALITY_CHANGED"
    val PLAYBACK_RATE_CHANGED = "PLAYBACK_RATE_CHANGED"
    val ERROR = "ERROR"
    val API_CHANGED = "API_CHANGED"
    val VIDEO_CURRENT_TIME = "VIDEO_CURRENT_TIME"
    val VIDEO_DURATION = "VIDEO_DURATION"
    val VIDEO_ID = "VIDEO_ID"
    val MESSAGE = "MESSAGE"

    // sender to receiver
    val LOAD = "LOAD"
    val PLAY = "PLAY"
    val PAUSE = "PAUSE"
    val MUTE = "MUTE"
    val UNMUTE = "UNMUTE"
    val SET_VOLUME = "SET_VOLUME"
    val SEEK_TO = "SEEK_TO"

    fun asJson() : JsonObject {
        return jsonObject(
                IFRAME_API_READY to IFRAME_API_READY,
                READY to READY,
                STATE_CHANGED to STATE_CHANGED,
                PLAYBACK_QUALITY_CHANGED to PLAYBACK_QUALITY_CHANGED,
                PLAYBACK_RATE_CHANGED to PLAYBACK_RATE_CHANGED,
                ERROR to ERROR,
                API_CHANGED to API_CHANGED,
                VIDEO_CURRENT_TIME to VIDEO_CURRENT_TIME,
                VIDEO_DURATION to VIDEO_DURATION,
                VIDEO_ID to VIDEO_ID,
                MESSAGE to MESSAGE,

                LOAD to LOAD,
                PLAY to PLAY,
                PAUSE to PAUSE,
                MUTE to MUTE,
                UNMUTE to UNMUTE,
                SET_VOLUME to SET_VOLUME,
                SEEK_TO to SEEK_TO
        )
    }
}