package com.example.androidrecorder.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioRecordDao {

    @Query("SELECT * FROM audioRecord")
    fun getAll(): List<AudioRecord>

    @Query("SELECT * FROM audioRecord WHERE filename LIKE :query")
    fun searchDatabase(query: String): List<AudioRecord>

//    @Query("SELECT * FROM audioRecord WHERE projectName:=projectName")
//    fun getOneSong(projectName: String): List<AudioRecord>

    @Insert
    fun insert(vararg audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecords: Array<AudioRecord>)

    @Update
    fun update(audioRecord: AudioRecord)
}