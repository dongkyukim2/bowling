package com.dk.project.post.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dkkim on 2017-10-03.
 */

public class BaseActivity extends AppCompatActivity implements Define {

    private static List<BaseActivity> activityStack = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStack.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityStack.remove(this);
    }


    public static boolean isLoadMainActivity() {
        for (BaseActivity baseActivity : activityStack) {
            if (baseActivity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
                return true;
            }
        }
        return false;
    }

    public static BaseActivity getLastActivity() {

        if (!activityStack.isEmpty()) {
            for (int i = 0; i < activityStack.size(); i++) {
                System.out.println("=============      " + activityStack.get(i).getClass().getSimpleName() + "    " + i);
            }
        }
        if (activityStack.size() == 1) {
            return activityStack.get(0);
        } else {
            BaseActivity baseActivity = activityStack.get(activityStack.size() - 1);
            if (baseActivity.getClass().getSimpleName().equals("IntroActivity")) {
                baseActivity = activityStack.get(activityStack.size() - 2);
//                System.out.println("=============       return   " + baseActivity.getClass().getSimpleName());
                return baseActivity;
            } else {
//                System.out.println("=============       return   " + baseActivity.getClass().getSimpleName());
                return baseActivity;
            }
        }
    }

    public static List<BaseActivity> getActivityStack() {
        return activityStack;
    }

    public static boolean isPrevActivity(String activityName) {
        if (activityStack.size() < 2) {
            return false;
        } else {
            return activityStack.get(activityStack.size() - 2).getClass().getSimpleName().equalsIgnoreCase(activityName);
        }
    }
}
