package com.example.androidrecorder.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.androidrecorder.Database.AppDataBase
import com.example.androidrecorder.Database.AudioRecord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Date

class SaveDataClass(context: Context) {

    var db: AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, "audioRecord").build()


    fun renameFile(new: String, old: String, dirPath: String) {
        var newFile = File("$dirPath$new.mp3")
        File("$dirPath$old.mp3").renameTo(newFile)
    }

        fun saveFile(
            new: String,
            dirPath: String,
            amplitudes: ArrayList<Float>,
            duration: String
        ): AudioRecord {


            var filePath = "$dirPath$new.mp3"
            var tameStamp = Date().time
            var ampsPath = "$dirPath$new"

            try {
                var fos = FileOutputStream(ampsPath)
                var out = ObjectOutputStream(fos)
                out.writeObject(amplitudes)
                fos.close()
                out.close()
            } catch (e: IOException) {
            }



            var audioRecord = AudioRecord(new, filePath, tameStamp, duration, ampsPath)

            insertAudio(audioRecord)

            return audioRecord

        }

    fun insertAudio(track: AudioRecord) {

        GlobalScope.launch {
            Log.d("MAIN5", "track is $track")
            db.AppDataBaseDao().insert(track)
        }
    }

    }


