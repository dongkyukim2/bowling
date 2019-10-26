package com.dk.project.bowling.shareData

import com.dk.project.post.bowling.model.ScoreClubUserModel

class ShareData private constructor() {

    // 클럽 가입한 인원 + 대기중인 인원
    val clubUserList = mutableListOf<ScoreClubUserModel>()

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