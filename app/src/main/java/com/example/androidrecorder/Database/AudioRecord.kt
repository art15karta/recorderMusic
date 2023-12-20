package com.example.androidrecorder.Database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "audioRecord")
data class AudioRecord(
    //var projectName: String
    var filename: String,
    var filepath: String,
    var timeStamp: Long,
    var dutation: String,
    var ampsPath: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @Ignore
    var isCheked = false
}
