package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import com.github.salomonbrys.kotson.addProperty
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject

class CastMessageToReceiver private constructor(val message: JsonObject) {

    override fun toString(): String {
        return message.toString()
    }

    companion object Builder {
        private val COMMAND = "command"

        fun build(command: String): CastMessageToReceiver {
            val message: JsonObject = jsonObject(COMMAND to command)

            return CastMessageToReceiver(message)
        }

        fun build(command: String, vararg arguments: Pair<String, JsonObject>): CastMessageToReceiver {
            val message: JsonObject = jsonObject(COMMAND to command)
            arguments.forEach { message.addProperty(it.first, it.second) }

            return CastMessageToReceiver(message)
        }

        fun build(command: String, vararg arguments: String): CastMessageToReceiver {
            val message: JsonObject = jsonObject(COMMAND to command)
            arguments.forEach { message.addProperty(it, it) }

            return CastMessageToReceiver(message)
        }
    }
}