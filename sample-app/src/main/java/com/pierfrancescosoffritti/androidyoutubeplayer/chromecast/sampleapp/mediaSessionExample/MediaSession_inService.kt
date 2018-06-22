package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.mediaSessionExample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.content.ContextCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import android.text.TextUtils
import android.content.Intent
import android.content.ComponentName
import android.app.PendingIntent
import android.support.v4.app.NotificationManagerCompat
import java.io.IOException
import android.support.v4.media.MediaMetadataCompat
import android.graphics.BitmapFactory
import android.media.MediaPlayer






class MediaSession_inService: MediaBrowserServiceCompat() {
    private val MY_MEDIA_ROOT_ID = "media_root_id"
    private val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

    lateinit var mMediaSession: MediaSessionCompat
    lateinit var mStateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()

        initMediaSession()
    }

    private fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(applicationContext, MediaButtonReceiver::class.java)
        mMediaSession = MediaSessionCompat(this, javaClass.simpleName, mediaButtonReceiver, null)

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(MySessionCallback(this, mMediaSession))
        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags( MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0)
        mMediaSession.setMediaButtonReceiver(pendingIntent)

        // Set the session's token so that client activities can communicate with it.
        sessionToken = mMediaSession.sessionToken

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mMediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
//        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID, parentId)) {
            result.sendResult(null)
//            return
//        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        // Clients can connect, but this BrowserRoot is an empty hierachy so onLoadChildren returns nothing. This disables the ability to browse for content.
        return if (TextUtils.equals(clientPackageName, packageName)) {
            MediaBrowserServiceCompat.BrowserRoot(getString(R.string.app_name), null)
        } else null
    }

    override fun onDestroy() {
        super.onDestroy();
        mMediaSession.release();
        NotificationManagerCompat.from(this).cancel(101);
    }

    private class MySessionCallback(private val context: Context, private val mMediaSession: MediaSessionCompat) : MediaSessionCompat.Callback() {
        val notificationHelper = NotificationHelper(context)

        override fun onPlay() {
            super.onPlay()
            // startService

            mMediaSession.isActive = true
            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)

            showPlayingNotification();
//            mMediaPlayer.start();
        }

        override fun onPause() {
            super.onPause()

//            if( mMediaPlayer.isPlaying() ) {
//                mMediaPlayer.pause();
                setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED);
                showPausedNotification();
//            }
        }

        override fun onStop() {
            super.onStop()
            // stopService
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)

//            try {
//                val afd = getResources().openRawResourceFd(Integer.valueOf(mediaId)) ?: return
//
//                try {
//                    mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength())
//
//                } catch (e: IllegalStateException) {
//                    mMediaPlayer.release()
//                    initMediaPlayer()
//                    mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength())
//                }
//
//                afd.close()
                initMediaSessionMetadata()

//            } catch (e: IOException) {
//                return
//            }


//            try {
//                mMediaPlayer.prepare()
//            } catch (e: IOException) {
//            }

        }

        fun onCompletion(mediaPlayer: MediaPlayer) {
//            if (mMediaPlayer != null) {
//                mMediaPlayer.release()
//            }
        }

        private fun initMediaSessionMetadata() {
            val metadataBuilder = MediaMetadataCompat.Builder()
            //Notification icon in card
//            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))

            //lock screen icon for pre lollipop
//            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "Display Title")
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, "Display Subtitle")
            metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 1)
            metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 1)

            mMediaSession.setMetadata(metadataBuilder.build())
        }

        private fun showPausedNotification() {
            val builder = notificationHelper.buildNotificationFromMediaSession(context, mMediaSession)

            builder.addAction(NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
            builder.setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mMediaSession.sessionToken))
            builder.setSmallIcon(R.mipmap.ic_launcher)
            NotificationManagerCompat.from(context).notify(1, builder.build())
        }

        private fun showPlayingNotification() {
            val builder = notificationHelper.buildNotificationFromMediaSession(context, mMediaSession)

            builder.addAction(NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
            builder.setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mMediaSession.sessionToken))
            builder.setSmallIcon(R.mipmap.ic_launcher)
            NotificationManagerCompat.from(context).notify(1, builder.build())
        }

        private fun setMediaPlaybackState(state: Int) {
            val playbackStateBuilder = PlaybackStateCompat.Builder()
            if (state == PlaybackStateCompat.STATE_PLAYING) {
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE)
            } else {
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY)
            }
            playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
            mMediaSession.setPlaybackState(playbackStateBuilder.build())
        }
    }


    // --- notifications

    class NotificationHelper(context: Context) {

        private val notificationId = 101
        private val notificationChannelId = "CHANNEL_ID"

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(notificationChannelId, "chromecast-youtube-player", NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "sample-app"
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun buildNotificationFromMediaSession(context: Context, mediaSession: MediaSessionCompat): NotificationCompat.Builder {
            val controller = mediaSession.controller
            val mediaMetadata = controller.metadata
            val description = mediaMetadata.description

            val builder = NotificationCompat.Builder(context, notificationChannelId)
            builder
                    // Add the metadata for the currently playing track
                    .setContentTitle(description.title)
                    .setContentText(description.subtitle)
                    .setSubText(description.description)
                    .setLargeIcon(description.iconBitmap)

                    // Enable launching the player by clicking the notification
                    .setContentIntent(controller.sessionActivity)

                    // Stop the service when the notification is swiped away
                    .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))

                    // Make the transport controls visible on the lockscreen
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                    // Add an app icon and set its accent color
                    // Be careful about the color
                    .setSmallIcon(R.drawable.ic_cast_connected_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))

                    // Add a pause button
                    .addAction(
                            NotificationCompat.Action(
                                    R.drawable.ic_pause_24dp, "pause",
                                    MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY_PAUSE)
                            )
                    )

                    // Take advantage of MediaStyle features
                    .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSession.sessionToken)
                            .setShowActionsInCompactView(0)

                            // Add a cancel button
                            .setShowCancelButton(true)
                            .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP)))

            // Display the notification and place the service in the foreground
            //        startForeground(notificationId, builder.build())
            return builder
        }
    }
}