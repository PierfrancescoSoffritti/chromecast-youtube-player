package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.util.Log
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener

class MyYouTubePlayerListener(private val youTubePlayer: YouTubePlayer) : YouTubePlayerListener {
    override fun onPlaybackQualityChange(playbackQuality: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoDuration(duration: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCurrentSecond(second: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReady() {
        Log.d("listener", "ready")
        youTubePlayer.loadVideo("6JYIGclVQdw", 0f)
    }

    override fun onPlaybackRateChange(playbackRate: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoId(videoId: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onApiChange() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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