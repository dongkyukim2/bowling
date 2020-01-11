package com.dk.project.post.manager

import androidx.room.Room
import com.dk.project.post.base.BaseApplication

object DataBaseManager {

    val appDatabase: AppDatabase = Room.databaseBuilder(
        BaseApplication.getGlobalApplicationContext(),
        AppDatabase::class.java,
        "youtube-db"
    ).build()

}