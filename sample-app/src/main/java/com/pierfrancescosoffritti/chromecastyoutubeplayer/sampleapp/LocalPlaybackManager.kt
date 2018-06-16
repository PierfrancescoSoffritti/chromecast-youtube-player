package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer

class LocalPlaybackManager {

    var youTubePlayer: YouTubePlayer? = null

    fun pause() {
        youTubePlayer?.pause()
    }

    fun resume(previousState: Int, lastVideoId: String, currentSecond: Float) {
        when (previousState) {
            PlayerConstants.PlayerState.PLAYING -> youTubePlayer?.loadVideo(lastVideoId, currentSecond)
            PlayerConstants.PlayerState.PAUSED -> youTubePlayer?.cueVideo(lastVideoId, currentSecond)
            PlayerConstants.PlayerState.ENDED -> youTubePlayer?.cueVideo(lastVideoId, currentSecond)
            else -> Log.d(javaClass.simpleName, "unknown remote state :/")
        }
    }
}