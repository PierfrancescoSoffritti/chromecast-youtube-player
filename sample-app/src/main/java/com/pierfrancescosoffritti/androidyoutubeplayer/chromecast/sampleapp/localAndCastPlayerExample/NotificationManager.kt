package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.localAndCastPlayerExample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.YouTubeDataEndpoint
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NotificationManager(private val context: Context) : LifecycleObserver, AbstractYouTubePlayerListener() {
    private val notificationId = 1
    private val channelId = "CHANNEL_ID"

    private val notificationBuilder: NotificationCompat.Builder

    init {
        notificationBuilder = initNotificationBuilder()
    }

    private fun initNotificationBuilder() : NotificationCompat.Builder {
        val openActivityExplicitIntent = Intent(context.applicationContext, LocalAndCastPlayerExample::class.java)
        openActivityExplicitIntent.action = Intent.ACTION_MAIN
        openActivityExplicitIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val openActivityPendingIntent = PendingIntent.getActivity(context.applicationContext, 0, openActivityExplicitIntent, 0)

        val togglePlaybackImplicitIntent = Intent(MyBroadcastReceiver.TOGGLE_PLAYBACK)
        val togglePlaybackPendingIntent = PendingIntent.getBroadcast(context, 0, togglePlaybackImplicitIntent, 0)

        val stopCastSessionImplicitIntent = Intent(MyBroadcastReceiver.STOP_CAST_SESSION)
        val stopCastSessionPendingIntent = PendingIntent.getBroadcast(context, 0, stopCastSessionImplicitIntent, 0)

        return NotificationCompat.Builder(context, channelId)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_cast_connected_24dp)
                .setContentIntent(openActivityPendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_play_arrow_24dp, "Toggle Playback", togglePlaybackPendingIntent)
                .addAction(R.drawable.ic_cast_connected_24dp, "Disconnect from chromecast", stopCastSessionPendingIntent)
                .setStyle(MediaStyle().setShowActionsInCompactView(0, 1))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "chromecast-youtube-player", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "sample-app"
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun dismissNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
    }

    override fun onStateChange(state: Int) {
        when(state) {
            PlayerConstants.PlayerState.PLAYING -> notificationBuilder.mActions[0].icon = R.drawable.ic_pause_24dp
            else -> notificationBuilder.mActions[0].icon = R.drawable.ic_play_arrow_24dp
        }

        showNotification()
    }

    override fun onVideoId(videoId: String) {
        val observable = YouTubeDataEndpoint.getVideoTitleFromYouTubeDataAPIs(videoId)

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { showNotification() }
                .subscribe(
                        {
                            notificationBuilder.setContentTitle(it.first.first)
                            notificationBuilder.setContentText(it.first.second)
                            notificationBuilder.setLargeIcon(it?.second)
                        },
                        { Log.e(javaClass.simpleName, "Can't retrieve video title, are you connected to the internet?") }
                )
    }
}