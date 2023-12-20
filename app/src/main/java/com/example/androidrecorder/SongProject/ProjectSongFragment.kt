package com.example.androidrecorder.SongProject

import android.media.MediaRecorder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidrecorder.Database.AppDataBase
import com.example.androidrecorder.R
import com.example.androidrecorder.ViewModel.RecorderViewModel
import com.example.androidrecorder.databinding.ActivityMainBinding

class ProjectSongFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_song, container, false)
    }


}