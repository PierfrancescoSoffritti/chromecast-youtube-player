package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast

import com.github.salomonbrys.kotson.addProperty
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject

class CastMessageToReceiver(private val command: String, private vararg val arguments: Pair<String, *>) {
    private val COMMAND = "command"

    constructor (command: String, vararg arguments: String) : this(command, *(arguments.map { Pair<String, String>(it, it) }.toTypedArray()) )

    constructor (command: String) : this(command, *emptyArray<Pair<String, String>>())

    fun toJsonString(): String {
        val message: JsonObject = jsonObject(COMMAND to command)
        arguments.forEach { message.addProperty(it.first, it.second.toString()) }

        return message.toString()
    }
}