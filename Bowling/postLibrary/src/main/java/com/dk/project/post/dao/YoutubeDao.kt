package com.dk.project.post.dao

import androidx.room.*
import com.dk.project.post.model.YoutubeData
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface YoutubeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYoutube(vararg youtube: YoutubeData)


    @Query("select * from youtube")
    fun selectAllYoutube(): Maybe<List<YoutubeData>>

    @Query("DELETE FROM youtube WHERE insertDate < :writeDate")
    fun deleteInsertTime(writeDate : Long) : Completable


    @Query("select * from youtube where insertDate < :writeDate")
    fun selectYoutube(writeDate: Long): Maybe<List<YoutubeData>>

}