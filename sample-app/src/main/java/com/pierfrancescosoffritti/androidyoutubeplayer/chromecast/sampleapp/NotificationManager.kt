package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.youtubePlayer.YouTubePlayersManager


class NotificationManager(private val context: Context, youTubePlayersManager: YouTubePlayersManager) : LifecycleObserver {
    private val notificationId = 1
    private val channelId = "CHANNEL_ID"

    val myBroadcastReceiver = MyBroadcastReceiver(youTubePlayersManager)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "chromecast-youtube-player", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "sample-app"
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, thumbnail: Bitmap? = null) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val togglePlaybackIntent = Intent(context, MyBroadcastReceiver::class.java)
        togglePlaybackIntent.action = MyBroadcastReceiver.TOGGLE_PLAYBACK
//        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0)
        val togglePlaybackPendingIntent = PendingIntent.getBroadcast(context, 0, togglePlaybackIntent, 0)

        val notification = NotificationCompat.Builder(context, channelId)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_audiotrack_dark)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_play_arrow_24dp, "Toggle Playback", togglePlaybackPendingIntent) // #0
                .addAction(R.drawable.ic_cast_connected_24dp, "Disconnect from chromecast", pendingIntent)  // #1
                .setStyle(MediaStyle().setShowActionsInCompactView(0, 1))
                .setContentTitle(title)
                .setContentText("My Awesome Band")
                .setAutoCancel(true)
                .setOngoing(true)
//                .setLargeIcon(thumbnail)
                .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notification)

        val filter = IntentFilter()
        filter.addAction(MyBroadcastReceiver.TOGGLE_PLAYBACK)

        try {
            context.registerReceiver(myBroadcastReceiver, filter)
        } catch (e: IllegalArgumentException) {
            Log.e(javaClass.simpleName, e.message)
        }
    }

    fun dismissNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)

        try {
            context.unregisterReceiver(myBroadcastReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(javaClass.simpleName, e.message)
        }
    }
}