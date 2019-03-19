package com.dk.project.post.utils;

import static com.dk.project.post.base.Define.YOUTUBE_PACKAGE_NAME;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import com.dk.project.post.service.CommunityService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Utils {

  public static SimpleDateFormat DateFormat_0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static SimpleDateFormat DateFormat_1 = new SimpleDateFormat("yy/MM/dd HH:mm");
  public static SimpleDateFormat DateFormat_2 = new SimpleDateFormat("yy/MM/dd");
  public static SimpleDateFormat DateFormat_3 = new SimpleDateFormat("a hh:mm");
  public static SimpleDateFormat DateFormat_4 = new SimpleDateFormat("yy/MM/dd");

  public static View getLayout(Context context) {

    LayoutParams params = new LayoutParams(1,1);
    AppCompatTextView appCompatTextView = new AppCompatTextView(context);


    appCompatTextView.setLayoutParams(params);

    return null;
  }


  public static String converterDate(String createDate) {
    String date = null;
    try {
      long postDate = Long.parseLong(createDate);
      long currentTime = Calendar.getInstance().getTimeInMillis();
      long milliseconds = currentTime - postDate;
      long minute = milliseconds / (60 * 1000);
      long hour = minute / 60;
      if (hour > 23) {
        date = DateFormat_1.format(new Date(postDate));
      } else if (hour > 0) {
        date = hour + "시간전";
      } else if (minute > 0) {
        date = minute + "분전";
      } else {
        date = "지금막";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static float getScorePercent(int score) {
    return 1.0f - (score / 300.0f);
  }

  public static boolean isYoutubeInstallCheck(Context context) {
    boolean isExist = false;
    PackageManager pkgMgr = context.getPackageManager();
    List<ResolveInfo> mApps;
    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    mApps = pkgMgr.queryIntentActivities(mainIntent, 0);
    try {
      for (int i = 0; i < mApps.size(); i++) {
        if (mApps.get(i).activityInfo.packageName.startsWith(YOUTUBE_PACKAGE_NAME)) {
          isExist = true;
          break;
        }
      }
    } catch (Exception e) {
    }
    return isExist;
  }

  public static boolean isShareIntent(Intent intent) {
    String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
    String url = intent.getStringExtra(Intent.EXTRA_TEXT);
    return (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(url));
  }

  public static boolean isServiceRunning(Context context) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (CommunityService.class.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isMainThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }


}
