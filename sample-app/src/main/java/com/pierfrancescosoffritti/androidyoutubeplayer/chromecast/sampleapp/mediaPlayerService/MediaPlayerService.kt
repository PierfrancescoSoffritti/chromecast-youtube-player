package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.mediaPlayerService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlaybackUtils
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.MainActivity
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.media.session.MediaSessionManager
import android.support.v4.media.MediaMetadataCompat
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.YouTubeDataEndpoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.app.PendingIntent
import android.os.RemoteException
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants


class MediaPlayerService: Service(), ChromecastConnectionListener {

    private var videoId: String? = null

    val ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY"
    val ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE"
    val ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS"
    val ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT"
    val ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP"

    //MediaSession
    private var mediaSessionManager: MediaSessionManager? = null
    private lateinit var mediaSession: MediaSessionCompat
    private var transportControls: MediaControllerCompat.TransportControls? = null

    //AudioPlayer notification ID
    private val NOTIFICATION_ID = 101

    private fun initMediaSession() {
        if (mediaSessionManager != null) return  //mediaSessionManager exists

        // Create a new MediaSession
        mediaSession = MediaSessionCompat(applicationContext, "AudioPlayer")
        //Get MediaSessions transport controls
        transportControls = mediaSession.controller.transportControls
        //set MediaSession -> ready to receive media commands
        mediaSession.isActive = true
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        //Set mediaSession's MetaData
        updateMetaData()

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            // Implement callbacks
            override fun onPlay() {
                super.onPlay()
                chromecastYouTubePlayer.play()
                buildNotification(PlayerConstants.PlayerState.PLAYING)
            }

            override fun onPause() {
                super.onPause()
                chromecastYouTubePlayer.pause()
                buildNotification(PlayerConstants.PlayerState.PAUSED)
            }

            override fun onStop() {
                super.onStop()
                removeNotification()
                stopSelf()
            }
        })
    }

    private fun updateMetaData() {
        if(videoId == null) return

        val observable = YouTubeDataEndpoint.getVideoTitleFromYouTubeDataAPIs(videoId!!)

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            mediaSession.setMetadata(MediaMetadataCompat.Builder()
                                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, it.second)
                                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.first.first)
                                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it.first.second)
                                    .build())
                        },
                        { Log.e(javaClass.simpleName, "Can't retrieve video title, are you connected to the internet?") }
                )
    }

    private fun buildNotification(playerState: Int) {

        var notificationAction = android.R.drawable.ic_media_pause//needs to be initialized
        var play_pauseAction: PendingIntent? = null

        //Build a new notification according to the current state of the MediaPlayer
        if (playerState == PlayerConstants.PlayerState.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause
            play_pauseAction = playbackAction(1)
        } else {
            notificationAction = android.R.drawable.ic_media_play
            //create the play action
            play_pauseAction = playbackAction(0)
        }

        // Create a new Notification
        val notificationBuilder = NotificationCompat.Builder(this, "")
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.sessionToken)
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(resources.getColor(R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText("text")
                .setContentTitle("title")
                .setContentInfo("info")
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2)) as NotificationCompat.Builder

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun removeNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun playbackAction(actionNumber: Int): PendingIntent? {
        val playbackAction = Intent(this, MediaPlayerService::class.java)
        when (actionNumber) {
            0 -> {
                // Play
                playbackAction.action = ACTION_PLAY
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            1 -> {
                // Pause
                playbackAction.action = ACTION_PAUSE
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            2 -> {
                // Next track
                playbackAction.action = ACTION_NEXT
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            3 -> {
                // Previous track
                playbackAction.action = ACTION_PREVIOUS
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            else -> {
            }
        }
        return null
    }

    private fun handleIncomingActions(playbackAction: Intent?) {
        if (playbackAction == null || playbackAction.action == null) return

        val actionString = playbackAction.action
        if (actionString!!.equals(ACTION_PLAY, ignoreCase = true)) {
            transportControls?.play()
        } else if (actionString.equals(ACTION_PAUSE, ignoreCase = true)) {
            transportControls?.pause()
        } else if (actionString.equals(ACTION_NEXT, ignoreCase = true)) {
            transportControls?.skipToNext()
        } else if (actionString.equals(ACTION_PREVIOUS, ignoreCase = true)) {
            transportControls?.skipToPrevious()
        } else if (actionString.equals(ACTION_STOP, ignoreCase = true)) {
            transportControls?.stop()
        }
    }

    private val iBinder = LocalBinder()

    private lateinit var chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext
    private lateinit var chromecastYouTubePlayer: YouTubePlayer
    private val playerStateTracker = YouTubePlayerStateTracker()

    override fun onCreate() {
        super.onCreate()
//        register_playNewAudio()
    }

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(javaClass.simpleName, "onStartCommand")

        chromecastYouTubePlayerContext = ChromecastYouTubePlayerContext(CastContext.getSharedInstance(this).sessionManager,this)

        if (mediaSessionManager == null) {
            try {
                initMediaSession()
            } catch (e: RemoteException) {
                e.printStackTrace()
                stopSelf()
            }

            buildNotification(PlayerConstants.PlayerState.PLAYING)
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        chromecastYouTubePlayerContext.release()

        removeNotification()
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

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), 0f)
                }

                override fun onVideoId(videoId: String) {
                    this@MediaPlayerService.videoId = videoId
                }
            })
        })
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    private val playNewAudio = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateMetaData()
            buildNotification(PlayerConstants.PlayerState.PLAYING)
        }
    }

//    private fun register_playNewAudio() {
//        //Register playNewMedia receiver
//        val filter = IntentFilter(MainActivity.Broadcast_PLAY_NEW_AUDIO)
//        registerReceiver(playNewAudio, filter)
//    }
}