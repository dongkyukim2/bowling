package com.dk.project.bowling;

import android.content.res.Resources;

public class utils {


    public static float getScorePercent(int score) {
        return 1.0f - (score / 300.0f);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
