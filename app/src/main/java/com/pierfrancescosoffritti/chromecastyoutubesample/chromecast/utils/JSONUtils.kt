package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.utils

import com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.MessageFromReceiver

object JSONUtils {
    fun buildFlatJson(vararg args: Pair<String, String>) : String {
        val jsonBuilder = StringBuilder("{")
        args.forEach { jsonBuilder.append("\"${it.first}\": \"${it.second}\",") }
        jsonBuilder.deleteCharAt(jsonBuilder.length-1)
        jsonBuilder.append("}")

        return jsonBuilder.toString()
    }

    fun buildCommunicationConstantsJson(command: Pair<String, String>, communicationConstants: Pair<String, String>) : String {
        val jsonBuilder = StringBuilder("{")
        jsonBuilder.append("\"${command.first}\": \"${command.second}\",")
        jsonBuilder.append("\"${communicationConstants.first}\": ${communicationConstants.second}")
        jsonBuilder.append("}")

        return jsonBuilder.toString()
    }

    fun parseMessageFromReceiverJson(json: String) : MessageFromReceiver {
        val strings = json.split(",")
        val values = strings.map { it.split(":")[1].trim().replace("\"", "") }

        return MessageFromReceiver(values[0], values[1])
    }
}