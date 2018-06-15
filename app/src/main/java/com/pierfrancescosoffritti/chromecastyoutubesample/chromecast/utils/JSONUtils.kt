package com.pierfrancescosoffritti.chromecastyoutubesample.chromecast.utils

object JSONUtils {
    fun buildFlatJson(vararg args: Pair<String, String>) : String {
        val jsonBuilder = StringBuilder("{")
        args.forEach { jsonBuilder.append("\"${it.first}\": \"${it.second}\",") }
        jsonBuilder.deleteCharAt(jsonBuilder.length-1)
        jsonBuilder.append("}")

        return jsonBuilder.toString()
    }
}