package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples.localAndCastPlayerExample.youtubePlayer

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener

class YouTubePlayerLogger : YouTubePlayerListener {
    override fun onVideoLoadedFraction(loadedFraction: Float) {
        Log.d(javaClass.simpleName, "onVideoLoadedFraction: $loadedFraction")
    }

    override fun onPlaybackQualityChange(playbackQuality: String) {
        Log.d(javaClass.simpleName, "onPlaybackQualityChange: $playbackQuality")
    }

    override fun onVideoDuration(duration: Float) {
        Log.d(javaClass.simpleName, "onVideoDuration: $duration")
    }

    override fun onCurrentSecond(second: Float) {
//        Log.d(javaClass.simpleName, "onCurrentSecond: $second")
    }

    override fun onReady() {
        Log.d(javaClass.simpleName, "onReady")
    }

    override fun onPlaybackRateChange(playbackRate: String) {
        Log.d(javaClass.simpleName, "onPlaybackRateChange: $playbackRate")
    }

    override fun onVideoId(videoId: String) {
        Log.d(javaClass.simpleName, "onVideoId: $videoId")
    }

    override fun onApiChange() {
        Log.d(javaClass.simpleName, "onApiChange")
    }

    override fun onError(error: Int) {
        Log.d(javaClass.simpleName, "onError: $error")
    }

    override fun onStateChange(state: Int) {
        Log.d(javaClass.simpleName, "onStateChange: $state")
    }
}