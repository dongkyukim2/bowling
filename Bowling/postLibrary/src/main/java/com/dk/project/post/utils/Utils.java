package com.dk.project.post.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

import com.dk.project.post.service.CommunityService;

import org.apache.commons.lang3.time.FastDateFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.dk.project.post.base.Define.YOUTUBE_PACKAGE_NAME;


public class Utils {

    public static SimpleDateFormat DateFormat_0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat DateFormat_1 = new SimpleDateFormat("yy/MM/dd HH:mm");
    public static SimpleDateFormat DateFormat_2 = new SimpleDateFormat("yy/MM/dd");
    public static SimpleDateFormat DateFormat_3 = new SimpleDateFormat("a hh:mm");
    public static SimpleDateFormat DateFormat_4 = new SimpleDateFormat("yy/MM/dd");
    public static FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault());
    public static FastDateFormat format_2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm", TimeZone.getDefault(), Locale.getDefault());

    public static View getLayout(Context context) {

        LayoutParams params = new LayoutParams(1, 1);
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
                date = format_2.format(new Date(postDate));

                String[] aa = date.split(" ");
                String week = getDAY_OF_WEEK(aa[0]);
                date = aa[0] + " (" + week + ") " + aa[1];
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

    public static String getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.encodeToString(results, Base64.DEFAULT).trim();
    }

    public static String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(Base64.decode(text, 0));
        return new String(results, "UTF-8").trim();
    }

    public static String getDAY_OF_WEEK(String day) {
        try {
            Date date = format.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);   // 요일을 구해온다.
            switch (dayNum) {
                case 1:
                    return "일";

                case 2:
                    return "월";

                case 3:
                    return "화";

                case 4:
                    return "수";

                case 5:
                    return "목";

                case 6:
                    return "금";

                case 7:
                    return "토";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(alpha, r, g, b);
    }

    public static String getRandomString() {

        Random generator = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < 9; i++) {
            char ch = (char) ((Math.random() * 26) + 97);
            int a = generator.nextInt(10);
            stringBuffer.append(ch);
            stringBuffer.append(a);
        }
        return stringBuffer.toString();
    }
}





















