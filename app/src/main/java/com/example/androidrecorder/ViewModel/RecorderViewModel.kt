package com.example.androidrecorder.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidrecorder.Database.AudioRecord

class RecorderViewModel: ViewModel() {

    var filenameDefault = MutableLiveData<String>()
    var filename = MutableLiveData<String>()

    var projectName = MutableLiveData<String>()

    var amplitudes = MutableLiveData<ArrayList<Float>>()
    var duration = MutableLiveData<String>()
    var dirPath = MutableLiveData<String>()
    var audio = MutableLiveData<AudioRecord>()

    init {
        filename.value = ""
        filenameDefault.value = ""
    }

    fun setProjectName(s: String) {
        projectName.value = s
    }

    fun setData(s: String) {
        filename.value = s
    }

    fun setAmplitudes(amp: ArrayList<Float>) {
        amplitudes.value = amp
    }

    fun setDuration(i: String) {
        duration.value = i
    }

    fun setDirPath(dir: String) {
        dirPath.value = dir
    }

    fun setAudioRecord(aud: AudioRecord){
        audio.value = aud
    }


}