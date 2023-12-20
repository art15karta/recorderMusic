package com.example.androidrecorder.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AudioRecord::class), version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun AppDataBaseDao(): AudioRecordDao
}