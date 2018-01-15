package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer

class LocalPlaybackBehaviour(private val youTubePlayer: YouTubePlayer) {

    fun onLocalYouTubePlayerReady(playingRemotely: Boolean, currentSecond: Float) {
        if(!playingRemotely)
            youTubePlayer.loadVideo("6JYIGclVQdw", currentSecond)
    }

    fun onApplicationConnecting() {
        youTubePlayer.pause()
    }

    fun onApplicationDisconnected(lastRemoteState: Int, lastVideoId: String, currentSecond: Float) {
        when (lastRemoteState) {
            PlayerConstants.PlayerState.PLAYING -> youTubePlayer.loadVideo(lastVideoId, currentSecond)
            PlayerConstants.PlayerState.PAUSED -> youTubePlayer.cueVideo(lastVideoId, currentSecond)
            PlayerConstants.PlayerState.ENDED -> youTubePlayer.cueVideo(lastVideoId, currentSecond)
        }
    }
}