package com.example.androidrecorder

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.androidrecorder.Database.AppDataBase
import com.example.androidrecorder.Database.AudioRecord
import com.example.androidrecorder.Gallery.GalleryActivity
import com.example.androidrecorder.ViewModel.RecorderViewModel
import com.example.androidrecorder.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val REQUEST_CODE = 200
class MainActivity : AppCompatActivity(), Timer.onTimeTickListener {

    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private lateinit var recorder: MediaRecorder
    private lateinit var amplitudes: ArrayList<Float>
    private var dirPath = ""
    private var duration = ""
    private var audioFilename = ""
    private var isRecording: Boolean = false
    private var isPausing = false
    private lateinit var timer: com.example.androidrecorder.Timer
    private lateinit var binding: ActivityMainBinding
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var db: AppDataBase


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted)
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)

        db = Room.databaseBuilder(this, AppDataBase::class.java, "audioRecord").build()

        timer = Timer(this)
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        recorderViewModel = ViewModelProvider(this).get(RecorderViewModel::class.java)

        binding.btnRecord.setOnClickListener {
            if (!isRecording && !isPausing) {
                startRecording()
                isRecording = true
                vib.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else if(!isRecording && isPausing) {
                onResumeRecodring()
            }
            else {
                pauseRecording()
                timer.pause()
                isRecording = false
                isPausing = true
            }

        }

        binding.btnPause.setOnClickListener {

            if (isRecording) {
                pauseRecording()
            } else
            {
                onResumeRecodring()
            }

        }

        binding.btnList.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        binding.btnDelete.setOnClickListener {
            stopRecording()
            recorderViewModel.filename.observe(this, {
                audioFilename = it
            })
            File("$dirPath$audioFilename.mp3")
            Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show()
        }

        binding.btnSave.setOnClickListener {
            var track = AudioRecord("", "", 0, "", "")
            stopRecording()
            timer.stop()
            SheetFragment().show(supportFragmentManager, "SheetFragmentTag")

            recorderViewModel.audio.observe(this) {
                track = it
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    @Suppress("DEPRECATION")
    private fun startRecording(){
        if(!permissionGranted){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }

        if (!isRecording) {
            // start recording
            recorder = MediaRecorder()
            dirPath = "${externalCacheDir?.absolutePath}/"
            Log.d("MAINDIR", "dir path $dirPath")
            recorderViewModel.setDirPath(dirPath)

            var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
            var date = simpleDateFormat.format(Date())


            audioFilename = "Audio $date"
            recorderViewModel.filenameDefault.value = audioFilename

            recorder.apply{
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile("$dirPath$audioFilename.mp3")

                try {
                    prepare()
                }catch (e: IOException){}

                start()
            }

            isRecording = true
            isPausing = false
            timer.start()
        }
    }

    private fun stopRecording() {

        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            timer.stop()
            isPausing = false
            isRecording = false

            binding.timer.text = "00:00:00"
            amplitudes = binding.waveformView.clear()
            recorderViewModel.setAmplitudes(amplitudes)
        }

    }
    private fun onResumeRecodring() {
        recorder.resume()
        isRecording = true
        isPausing = false
        timer.start()
        binding.btnPause.setImageResource(R.drawable.outline_pause_24)
    }

    private fun pauseRecording() {

            recorder.pause()
            timer.pause()
            isRecording = false
            isPausing = true

            binding.btnPause.setImageResource(R.drawable.outline_play_circle_outline_24)

    }

    override fun onTimerTick(duration: String) {
        super.onTimerTick(duration)
        binding.timer.text = duration
        this.duration = duration.dropLast(3)
        recorderViewModel.setDuration(this.duration)
        binding.waveformView.addAmplitude(recorder.maxAmplitude.toFloat())

    }

//    fun insertAudio(track: AudioRecord) {
//
//        GlobalScope.launch {
//                Log.d("MAIN5", "track is $track")
//               db.AppDataBaseDao().insert(track)
//            }
//    }

//    fun saveFile(new: String?, old: String): AudioRecord {
//
//
//        Log.d("MAIN3", "saveData var is oldAudio $old, newAudio $new, audioFilename $audioFilename")
//        if (!new.isNullOrEmpty() && old.isNotEmpty()) {
//            Log.d("MAIN3", "SaveFile is start and $new, $old")
//
//            if (new != old) {
//                var newFile = File("$dirPath$new.mp3")
//                File("$dirPath$old.mp3").renameTo(newFile)
//            }
//
//
//            var filePath = "$dirPath$new.mp3"
//            var tameStamp = Date().time
//            var ampsPath = "$dirPath$new"
//
//            try {
//                var fos = FileOutputStream(ampsPath)
//                var out = ObjectOutputStream(fos)
//                out.writeObject(amplitudes)
//                fos.close()
//                out.close()
//            } catch (e: IOException) {}
//
//            var audioRecord = AudioRecord(new, filePath, tameStamp, duration, ampsPath)
//
//
//            GlobalScope.launch {
//                Log.d("MAIN2", "Insert db $audioRecord")
//                db.AppDataBaseDao().insert(audioRecord)
//            }
//        }
//
//
//    }

}