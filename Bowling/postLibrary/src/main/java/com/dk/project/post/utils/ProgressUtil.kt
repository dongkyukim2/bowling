package com.dk.project.post.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatDialog
import com.dk.project.post.R

object ProgressUtil {

    private var progressDialog: AppCompatDialog? = null

    fun showProgress(activity: Activity) {
        if (activity.isFinishing || activity.isDestroyed) {
            return
        }
        hideProgress()
        progressDialog = AppCompatDialog(activity, R.style.Theme_CustomDialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        progressDialog!!.setContentView(R.layout.progress_loading)
        progressDialog!!.show()
    }

    fun hideProgress() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        progressDialog = null
    }
}