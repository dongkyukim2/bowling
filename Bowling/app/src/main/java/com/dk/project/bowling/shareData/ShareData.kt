package com.dk.project.bowling.shareData

import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.model.LoginInfoModel
import java.util.*

class ShareData private constructor() {

    val scoreList = ArrayList<ScoreClubUserModel>() // 수정할때 임시저장
    val inviteUserList = ArrayList<ScoreClubUserModel>() // 클럽 게임 유저 초대



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