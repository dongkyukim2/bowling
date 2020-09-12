package com.dk.project.post.utils

import android.app.Activity
import android.content.Context
import com.dk.project.post.manager.LoginManager
import com.dk.project.post.model.LoginInfoModel
import com.dk.project.post.retrofit.PostApi
import com.dk.project.post.retrofit.SuccessCallback
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.common.model.ApiError
import com.kakao.sdk.common.model.ApiErrorCause
import com.kakao.sdk.user.UserApiClient


class KakaoLoginUtils {

    companion object {
        @JvmStatic
        val instance by lazy { KakaoLoginUtils() }
    }

    fun login(context: Context, callback: SuccessCallback<Pair<Long, LoginInfoModel?>>) {
        println("++++++++++    login")
        // 카카오톡으로 로그인
        LoginClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {

            } else if (token != null) {
                getUserInfo(context, callback)
            }
        }
    }

    fun getUserInfo(context: Context, callback: SuccessCallback<Pair<Long, LoginInfoModel?>>) {
        println("++++++++++    getUserInfo")
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                if (error is ApiError && error.reason == ApiErrorCause.InvalidToken) {
                    println("++++++++++    getUserInfo 0")
                    login(context, callback)
                } else {
                    println("++++++++++    getUserInfo 1")
                    callback.onSuccess(Pair(0, null))
                }
            } else {
                user?.let {
                    // 회원가입되어있나 로그아웃한것인가 확인
                    PostApi.getInstance().getUserInfo(user.id.toString(), {
                        println("++++++++++    getUserInfo 2")
                        callback.onSuccess(Pair(user.id, it.data))
                    }, {
                        println("++++++++++    getUserInfo 3")
                        callback.onSuccess(Pair(0, null))

                    })
                } ?: {
                    println("++++++++++    getUserInfo 4")
                    login(context, callback)
                }()
            }
        }
    }

    fun logout(activity: Activity) {
        println("++++++++++    logout")
        UserApiClient.instance.logout { error ->
            LoginManager.getInstance().loginInfoModel = null
            activity.finishAffinity()
        }
    }
}