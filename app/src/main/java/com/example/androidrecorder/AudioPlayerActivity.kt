package com.example.androidrecorder

import android.content.Intent
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar

import androidx.core.content.res.ResourcesCompat
import com.example.androidrecorder.Gallery.GalleryActivity
import com.google.android.material.chip.Chip
import java.text.DecimalFormat
import java.text.NumberFormat

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var btnPlay: ImageButton
    private lateinit var btnBackward: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var speedChip: Chip
    private lateinit var seekBar: SeekBar
    private lateinit var toolbar: Toolbar
    private lateinit var textView: TextView

    private lateinit var tvTrackProgress: TextView
    private lateinit var tvTrackDuration: TextView

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var delay = 1000L
    private var jumpValue = 1000
    private var playbackSpeed = 1f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        var filepath = intent.getStringExtra("filepath")
        var filename = intent.getStringExtra("filename")

        toolbar = findViewById(R.id.toolBar)
        textView = findViewById(R.id.tvFileName)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {

            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)

        }
        textView.text = filename
        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(filepath)
            prepare()
        }


        btnPlay = findViewById(R.id.btnPlay)
        btnBackward = findViewById(R.id.btnBackward)
        btnForward = findViewById(R.id.btnForward)
        seekBar = findViewById(R.id.seekBar)
        speedChip = findViewById(R.id.chip)
        tvTrackDuration = findViewById(R.id.tvTrackDuration)
        tvTrackProgress = findViewById(R.id.tvTrackProgress)

        tvTrackDuration.text = dateFormat(mediaPlayer.duration)

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            tvTrackProgress.text = dateFormat(mediaPlayer.currentPosition)
            handler.postDelayed(runnable, delay)
        }

        btnPlay.setOnClickListener {
            playPausePlayer()
        }

        btnForward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition + jumpValue)
            seekBar.progress += jumpValue
        }

        btnBackward.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - jumpValue)
            seekBar.progress -= jumpValue
        }



        speedChip.setOnClickListener {
            if (playbackSpeed != 2f) {
                playbackSpeed += 0.5f
            } else {
                playbackSpeed = 0.5f
            }

            mediaPlayer.playbackParams = PlaybackParams().setSpeed(playbackSpeed)
            speedChip.text = "x $playbackSpeed"
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    mediaPlayer.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        playPausePlayer()
        seekBar.max = mediaPlayer.duration

        mediaPlayer.setOnCompletionListener {
            btnPlay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.outline_pause_24, theme)
            handler.removeCallbacks(runnable)
        }
    }

    private fun playPausePlayer() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            btnPlay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.outline_pause_24, theme)
            handler.postDelayed(runnable, 0)
        } else {
            mediaPlayer.pause()
            btnPlay.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_play_circle_24, theme)
            handler.removeCallbacks(runnable)
        }
    }

    private fun dateFormat(duration: Int): String {
        var d = duration / 1000
        var s = d % 60
        var m = (d / 60 % 60)
        var h = ((d-m*60)/360).toInt()

        val f: NumberFormat = DecimalFormat("00")
        var str = "$m:${f.format(s)}"

        if (h>0) {
            str = "$h:$str"
        }

        return str
    }
}