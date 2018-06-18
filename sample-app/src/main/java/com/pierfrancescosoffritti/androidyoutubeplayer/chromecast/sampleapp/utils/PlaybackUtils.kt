package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils

import java.util.*

object PlaybackUtils {
    private val videoIds = arrayOf("6JYIGclVQdw", "LvetJ9U_tVY")

    fun getNextVideoId(): String {
        return videoIds[Random().nextInt(videoIds.size)]
    }
}