package com.dk.project.bowling.shareData

import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.model.LoginInfoModel
import java.util.*

class ShareData private constructor() {

//    val userMap = HashMap<String, LoginInfoModel>()

    val scoreList = ArrayList<ScoreClubUserModel>()


    companion object {
        @Volatile
        private var instance: ShareData? = null

        @JvmStatic
        fun getInstance(): ShareData =
            instance ?: synchronized(this) {
                instance ?: ShareData().also {
                    instance = it
                }
            }
    }
}