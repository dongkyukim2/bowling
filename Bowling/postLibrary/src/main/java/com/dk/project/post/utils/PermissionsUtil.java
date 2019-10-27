package com.dk.project.post.utils;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.impl.PermissionCallBack;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by dkkim on 2017-11-26.
 */

public class PermissionsUtil implements Define {

    public static void isPermission(BaseActivity context, PermissionCallBack permissionCallBack) {
        RxPermissions permissions = new RxPermissions(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            AlertDialogUtil.showAlertDialog(context, "알림", "사진을 첨부하기 위하여 저장 접근 허용이 필요합니다", (dialogInterface, i) -> {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
                Preference<Boolean> username = rxPreferences.getBoolean("to_setting_storage", false);

                if (username.get() && !ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivityForResult(intent, PERMISSION_REQUEST_STORAGE);
                } else {
                    permissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(permission -> {
                                if (permission.granted) {
                                    permissionCallBack.onPermissionGranted(true);
                                } else if (permission.shouldShowRequestPermissionRationale) { // 거부

                                } else { // 다시 묻지 않기
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("to_setting_storage", true);
                                    editor.commit();
                                }
                            });
                }
            }, (dialog, which) -> {
            });
        } else {
            permissionCallBack.onPermissionGranted(true);
        }
    }
}
