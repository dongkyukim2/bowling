package com.dk.project.bowling;

import android.content.res.Resources;

import java.util.Random;

public class utils {

    private static int[] imageArray = {R.drawable.team_default_1,
            R.drawable.team_default_2,
            R.drawable.team_default_3,
            R.drawable.team_default_4,
            R.drawable.team_default_5,
            R.drawable.team_default_6};

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

    public static int getDefaultImageIndex() {
        return new Random().nextInt(6);
    }

    public static int getDefaultImage() {
        return imageArray[getDefaultImageIndex()];
    }

    public static int getDefaultImage(int index) {
        return imageArray[index];
    }

}
