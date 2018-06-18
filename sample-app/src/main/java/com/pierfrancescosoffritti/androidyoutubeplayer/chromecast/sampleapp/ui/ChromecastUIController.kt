package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.*
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.utils.Utils


class ChromecastUIController(private val controls_view: View) : YouTubePlayerListener, SeekBar.OnSeekBarChangeListener {
    lateinit var youtubePlayer: YouTubePlayer

    private var isPlaying = false

    private val progressBar = controls_view.findViewById<View>(R.id.progress_bar)
    private val playPauseButton = controls_view.findViewById<ImageView>(R.id.play_pause_button)
    private val currentTimeTextView = controls_view.findViewById<TextView>(R.id.current_time_text_view)
    private val totalTimeTextView = controls_view.findViewById<TextView>(R.id.total_time_text_view)
    private val seekBar = controls_view.findViewById<SeekBar>(R.id.seek_bar)
    private val youTubeButton = controls_view.findViewById<ImageView>(R.id.youtube_button)

    private val castButtonContainer = controls_view.findViewById<FrameLayout>(R.id.cast_button_container)

    init {
        seekBar.setOnSeekBarChangeListener(this)
        playPauseButton.setOnClickListener { onPlayButtonPressed() }
    }

    override fun onStateChange(state: Int) {
        newSeekBarProgress = -1

        updateControlsState(state)

        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED || state == PlayerConstants.PlayerState.UNSTARTED) {
            progressBar.visibility = View.INVISIBLE
            playPauseButton.visibility = View.VISIBLE

        } else if(state == PlayerConstants.PlayerState.BUFFERING) {
            progressBar.visibility = View.VISIBLE
            playPauseButton.visibility = View.INVISIBLE
        }

        val playing = state == PlayerConstants.PlayerState.PLAYING
        updatePlayPauseButtonIcon(playing)
    }

    override fun onVideoDuration(duration: Float) {
        totalTimeTextView.text = Utils.formatTime(duration)
        seekBar.max = duration.toInt()
    }

    override fun onCurrentSecond(currentSecond: Float) {
        if (seekBarTouchStarted)
            return

        // ignore if the current time is older than what the user selected with the SeekBar
        if (newSeekBarProgress > 0 && Utils.formatTime(currentSecond) != Utils.formatTime(newSeekBarProgress.toFloat()))
            return

        newSeekBarProgress = -1

        seekBar.progress = currentSecond.toInt()
    }

    override fun onVideoLoadedFraction(loadedFraction: Float) {
        seekBar.secondaryProgress = loadedFraction.toInt()
    }

    override fun onVideoId(videoId: String) {
        youTubeButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
            controls_view.context.startActivity(intent)
        }
    }

    fun addView(view: View) {
        castButtonContainer.addView(view)
    }

    fun removeView(view: View) {
        castButtonContainer.removeView(view)
    }

    private fun updateControlsState(state: Int) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> isPlaying = false
            PlayerConstants.PlayerState.PAUSED -> isPlaying = false
            PlayerConstants.PlayerState.PLAYING -> isPlaying = true
            PlayerConstants.PlayerState.BUFFERING -> isPlaying = false
            PlayerConstants.PlayerState.UNSTARTED -> resetUI()
            else -> {
            }
        }

        updatePlayPauseButtonIcon(!isPlaying)
    }

    fun resetUI() {
        seekBar.progress = 0
        seekBar.max = 0
        playPauseButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        currentTimeTextView.post { currentTimeTextView.text = "" }
        totalTimeTextView.post { totalTimeTextView.text = "" }
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
    override fun onPlaybackQualityChange(playbackQuality: String) { }
    override fun onPlaybackRateChange(playbackRate: String) { }
    override fun onApiChange() { }
    override fun onError(error: Int) { }

    // -- SeekBar, this code will be refactored

    private var seekBarTouchStarted = false
    // I need this variable because onCurrentSecond gets called every 100 mill, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
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

        youtubePlayer.seekTo(seekBar.progress.toFloat())
        seekBarTouchStarted = false
    }
}