package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener

class LoggerYouTubePlayerListener : YouTubePlayerListener {

    override fun onPlaybackQualityChange(playbackQuality: String) {
        Log.d(javaClass.simpleName, "onPlaybackQualityChange: " +playbackQuality)
    }

    override fun onVideoDuration(duration: Float) {
        Log.d(javaClass.simpleName, "onVideoDuration: " +duration)
    }

    override fun onCurrentSecond(second: Float) {
//        Log.d(javaClass.simpleName, "current second: " +second)
    }

    override fun onReady() {
        Log.d("listener", "ready")
    }

    override fun onPlaybackRateChange(playbackRate: String) {
        Log.d(javaClass.simpleName, "onPlaybackRateChange: " +playbackRate)
    }

    override fun onVideoId(videoId: String) {
        Log.d(javaClass.simpleName, "video Id: " +videoId)
    }

    override fun onApiChange() {
        Log.d(javaClass.simpleName, "onApiChange")
    }

    override fun onMessage(log: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(error: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStateChange(state: Int) {
        Log.d(javaClass.simpleName, "state changed: " +state)
    }
}