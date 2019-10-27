package com.dk.project.post.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.dk.project.post.base.Define;

/**
 * Created by dkkim on 2017-10-05.
 */

public class ScreenUtil {

    public static int[] getDeviceScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return new int[]{width, height};
    }

    public static int dpToPixel(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

//    숫자가 작으면 작을수록 가로보다 세로가 긴 디바이스
    public static float screenRatio(Context context) {
        int[] widthHeight = getDeviceScreenSize(context);
        float ratio = (float) (((float) widthHeight[0] / (float) widthHeight[1]) * 100.0);
        return ratio;
    }


    public static int getDensity(Activity activity) {
        int margin = Define.mdpi;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.densityDpi <= 160) { // mdpi
            margin = Define.mdpi;
        } else if (metrics.densityDpi <= 240) { // hdpi
            margin = Define.hdpi;
        } else if (metrics.densityDpi <= 320) { // xhdpi
            margin = Define.xhdpi;
        } else if (metrics.densityDpi <= 480) { // xxhdpi
            margin = Define.xxhdpi;
        } else if (metrics.densityDpi <= 640) { // xxxhdpi
            margin = Define.xxxhdpi;
        }
        return margin;
    }
}
