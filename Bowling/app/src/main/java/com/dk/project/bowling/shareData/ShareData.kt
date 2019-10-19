package com.dk.project.bowling.shareData

import com.dk.project.post.bowling.model.UserModel

class ShareData private constructor() {

    val clubUserList = mutableListOf<UserModel>()

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