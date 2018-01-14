package com.pierfrancescosoffritti.chromecastyoutubesample.youTube

import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.pierfrancescosoffritti.chromecastyoutubesample.R
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.utils.Utils

class ChromecastUIController(private val chromecast_controls: ConstraintLayout, private val youtubePlayer: YouTubePlayer) : YouTubePlayerListener, SeekBar.OnSeekBarChangeListener, DumbPlayerUI() {
    private var isPlaying = false
    private var showPlayPauseButton = true

    private val progressBar = chromecast_controls.findViewById<View>(R.id.progress_bar)
    private val playPauseButton = chromecast_controls.findViewById<ImageView>(R.id.play_pause_button)
    private val currentTimeTextView = chromecast_controls.findViewById<TextView>(R.id.current_time_text_view)
    private val totalTimeTextView = chromecast_controls.findViewById<TextView>(R.id.total_time_text_view)
    private val seekBar = chromecast_controls.findViewById<SeekBar>(R.id.seek_bar)
    private val youTubeButton = chromecast_controls.findViewById<ImageView>(R.id.youtube_button)

    private val castButtonContainer = chromecast_controls.findViewById<FrameLayout>(R.id.cast_button_container)

    init {
        seekBar.setOnSeekBarChangeListener(this)
        playPauseButton.setOnClickListener({ onPlayButtonPressed() })
    }

    override fun onStateChange(state: Int) {
        newSeekBarProgress = -1

        updateControlsState(state)

        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            progressBar.visibility = View.INVISIBLE

            if (showPlayPauseButton) playPauseButton.visibility = View.VISIBLE

            val playing = state == PlayerConstants.PlayerState.PLAYING
            updatePlayPauseButtonIcon(playing)

        } else {
            updatePlayPauseButtonIcon(false)

            if (state == PlayerConstants.PlayerState.BUFFERING) {
                if (showPlayPauseButton) playPauseButton.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }

            if (state == PlayerConstants.PlayerState.UNSTARTED) {
                progressBar.visibility = View.INVISIBLE
                if (showPlayPauseButton) playPauseButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onVideoDuration(duration: Float) {
        totalTimeTextView.text = Utils.formatTime(duration)
        seekBar.max = duration.toInt()
    }

    override fun onCurrentSecond(second: Float) {
        // ignore if the user is currently moving the SeekBar
        if (seekBarTouchStarted)
            return
        // ignore if the current time is older than what the user selected with the SeekBar
        if (newSeekBarProgress > 0 && Utils.formatTime(second) != Utils.formatTime(newSeekBarProgress.toFloat()))
            return

        newSeekBarProgress = -1

        seekBar.progress = second.toInt()
    }

    override fun onVideoId(videoId: String?) {
        youTubeButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId))
            chromecast_controls.context.startActivity(intent)
        }
    }

    override fun addView(view: View) {
        castButtonContainer.addView(view)
    }

    override fun removeView(view: View) {
        castButtonContainer.removeView(view)
    }

    // SeekBar callbacks

    private var seekBarTouchStarted = false
    // I need this variable because onCurrentSecond gets called every 100 mils, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
    private var newSeekBarProgress = -1

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        currentTimeTextView.text = Utils.formatTime(i.toFloat())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        seekBarTouchStarted = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (isPlaying)
            newSeekBarProgress = seekBar.progress

        youtubePlayer.seekTo(seekBar.progress)
        seekBarTouchStarted = false
    }

    private fun updateControlsState(state: Int) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> isPlaying = false
            PlayerConstants.PlayerState.PAUSED -> isPlaying = false
            PlayerConstants.PlayerState.PLAYING -> isPlaying = true
            PlayerConstants.PlayerState.UNSTARTED -> resetUI()
            else -> {
            }
        }

        updatePlayPauseButtonIcon(!isPlaying)
    }

    private fun resetUI() {
        seekBar.progress = 0
        seekBar.max = 0
        totalTimeTextView.post({ totalTimeTextView.text = "" })
        youTubeButton.setOnClickListener(null)
    }

    private fun updatePlayPauseButtonIcon(playing: Boolean) {
        val img = if (playing) com.pierfrancescosoffritti.youtubeplayer.R.drawable.ic_pause_36dp else com.pierfrancescosoffritti.youtubeplayer.R.drawable.ic_play_36dp
        playPauseButton.setImageResource(img)
    }

    private fun onPlayButtonPressed() {
        if (isPlaying)
            youtubePlayer.pause()
        else
            youtubePlayer.play()
    }

    override fun onReady() { }
    override fun onPlaybackQualityChange(playbackQuality: String?) { }
    override fun onPlaybackRateChange(playbackRate: String?) { }
    override fun onApiChange() { }
    override fun onMessage(log: String?) { }
    override fun onError(error: Int) { }
}