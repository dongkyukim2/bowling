package com.dk.project.post.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "youtube")
data class YoutubeData(@PrimaryKey val youtubeId: String, val insertDate: Timestamp)