package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.serviceExample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.NotificationManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlaybackUtils
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker


class MediaPlayerService: Service(), ChromecastConnectionListener {

    private val iBinder = LocalBinder()

    private lateinit var chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext
    private lateinit var chromecastYouTubePlayer: YouTubePlayer
    private lateinit var notificationManager: NotificationManager

    private val playerStateTracker = YouTubePlayerStateTracker()

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManager(this)
    }

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(javaClass.simpleName, "onStartCommand")

        chromecastYouTubePlayerContext = ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager,this)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        chromecastYouTubePlayerContext.release()

        notificationManager.dismissNotification()
    }

    override fun onChromecastConnecting() {
        Log.d(javaClass.simpleName, "onChromecastConnecting")
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext)
    }

    override fun onChromecastDisconnected() {

    }

    private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

            chromecastYouTubePlayer = youtubePlayer

            youtubePlayer.addListener(playerStateTracker)
            youtubePlayer.addListener(notificationManager)

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f)
                }

                override fun onVideoId(videoId: String) {

                }
            })
        })
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }
}