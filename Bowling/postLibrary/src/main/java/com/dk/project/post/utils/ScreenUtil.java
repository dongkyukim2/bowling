package com.dk.project.post.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
}
