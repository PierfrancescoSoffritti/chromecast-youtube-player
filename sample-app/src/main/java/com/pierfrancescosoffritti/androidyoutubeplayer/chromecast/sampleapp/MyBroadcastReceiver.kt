package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.youtubePlayer.YouTubePlayersManager

class MyBroadcastReceiver(private val youTubePlayersManager: YouTubePlayersManager) : BroadcastReceiver() {
    companion object {
        const val TOGGLE_PLAYBACK = "TOGGLE_PLAYBACK"
    }
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(javaClass.simpleName, intent.action)
        when(intent.action) {
            TOGGLE_PLAYBACK -> youTubePlayersManager.togglePlayback()
        }
    }
}