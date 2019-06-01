package com.dk.project.post.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import com.dk.project.post.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;


/**
 * Created by dkkim on 2017-05-11.
 */

public class AlertDialogUtil {

    public static AlertDialog showListAlertDialog(Context context, String title, CharSequence info[], DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setItems(info, onClickListener);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setMessage(message)
                .setPositiveButton("예", onPositiveClickListener)
                .setNegativeButton("아니요", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        return dialog;
    }

    public static void showLoginAlert(final Context context) {
//        showAlertDialog(context, "알림", "로그인 후 이용해주세요", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(context, LoginActivity.class);
//                context.startActivity(intent);
//            }
//        });
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("예", onPositiveClickListener)
                .setNegativeButton("아니요", onNegativeClickListener);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showConnectionFailAlertDialog(final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("알림")
                .setMessage("서버에 연결할 수 없습니다.\n인터넷 연결 상태를 확인하고 다시 시도해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static AlertDialog showLoadingAlertDialog(Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        dialogBuilder.setView(imageView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
//        ApngImageLoader.getInstance()
//                .displayApng("assets://loading.png", imageView,
//                        new ApngImageLoader.ApngConfig(9999, true, true));
        return dialog;
    }


    public static BottomSheetDialog showBottomSheetDialog(Context context, OnClickListener onClickListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.popup_menu, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();
        view.findViewById(R.id.btnModify).setOnClickListener(v -> {
            onClickListener.onClick(v);
            dialog.dismiss();

        });
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            onClickListener.onClick(v);
            dialog.dismiss();
        });
        return dialog;
    }

    public static AlertDialog showEditTextAlertDialog(final Context context, String hint, View.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.edit_text_dialog, null);
        EditText editText = view.findViewById(R.id.dialog_edit_text);
        editText.setHint(hint);

        dialogBuilder.setPositiveButton("확인", (dialog, which) -> {

        });
        dialogBuilder.setNegativeButton("취소", (dialog, which) -> {
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();


        Button posBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posBtn.setOnClickListener(v -> {
            if (editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
            } else {
                onClickListener.onClick(editText);
                alertDialog.dismiss();
            }
        });

        editText.post(() -> TextViewUtil.showKeyBoard(context, editText));
        return alertDialog;
    }
}

