package com.dk.project.post.manager

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dk.project.post.dao.YoutubeDao
import com.dk.project.post.model.YoutubeData
import com.dk.project.post.utils.DateConverter

@Database(entities = [YoutubeData::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun youtubeDao(): YoutubeDao
}