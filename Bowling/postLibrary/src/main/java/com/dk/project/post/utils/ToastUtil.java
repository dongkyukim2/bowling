package com.dk.project.post.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.dk.project.post.R;

/**
 * Created by dkkim on 2018-01-20.
 */

public class ToastUtil {

    public static void showUploadSuccessToast(Context context) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setView(LayoutInflater.from(context).inflate(R.layout.upload_toast, null, false));
        toast.setGravity(Gravity.TOP, 0, (int) (ScreenUtil.getDeviceScreenSize(context)[1] * 0.2));
        toast.show();
    }

    public static void showToastCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, (int) (ScreenUtil.getDeviceScreenSize(context)[1] * 0.3));
        toast.show();
    }

    public static void showWaitToastCenter(Context context) {
        try {
            throw new Exception("++++++++++++++       ing...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(context, "처리중입니다. 잠시만 기다려주세요...", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, (int) (ScreenUtil.getDeviceScreenSize(context)[1] * 0.3));
        toast.show();
    }
}
