package com.dk.project.bowling.shareData

import com.dk.project.post.bowling.model.ScoreClubUserModel
import java.util.*

class ShareData private constructor() {

    // 클럽 게임 수정하기 임시 저장
    val userList = ArrayList<ScoreClubUserModel>()

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