package com.dk.project.post.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.dk.project.post.R;
import com.dk.project.post.manager.LoginManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;


/**
 * Created by dkkim on 2017-05-11.
 */

public class AlertDialogUtil {

    public static AlertDialog showLoginAlertDialog(Context context) {
        try {
            throw new Exception("++++++++++");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showLoginAlertDialog(context, (dialog, which) -> {
        });
    }

    public static AlertDialog showLoginAlertDialog(Context context, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("알림")
                .setMessage("로그인 후 이용하실 수 있습니다.")
                .setPositiveButton("확인", onPositiveClickListener);
        AlertDialog dialog = dialogBuilder.create();
        setTextColor(dialog);
        dialog.show();
        return dialog;
    }

    public static AlertDialog showListAlertDialog(Context context, String title, CharSequence info[], DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setItems(info, onClickListener);
        AlertDialog dialog = dialogBuilder.create();
        setTextColor(dialog);
        dialog.show();
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", onPositiveClickListener)
                .setNegativeButton("취소", onNegativeClickListener);
        AlertDialog dialog = dialogBuilder.create();
        setTextColor(dialog);
        dialog.show();
        return dialog;
    }

    public static void showBottomSheetDialog(Context context, OnClickListener onClickListener) {
        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            showLoginAlertDialog(context);
            return;
        }
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
        return;
    }

    public static AlertDialog showEditTextAlertDialog(final Context context, String title, String hint, View.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.edit_text_dialog, null);
        if (!TextUtils.isEmpty(title)) {
            ((AppCompatTextView) view.findViewById(R.id.alert_title)).setText(title);
        }
        EditText editText = view.findViewById(R.id.dialog_edit_text);
        editText.setHint(hint);

        dialogBuilder.setPositiveButton("확인", (dialog, which) -> {

        });
        dialogBuilder.setNegativeButton("취소", (dialog, which) -> {
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        setTextColor(alertDialog);
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

    public static AlertDialog showScoreEditTextAlertDialog(final Context context, String hint, View.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.edit_text_dialog, null);
        EditText editText = view.findViewById(R.id.dialog_edit_text);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(3);
        editText.setFilters(FilterArray);


        editText.setHint(hint);

        dialogBuilder.setPositiveButton("확인", (dialog, which) -> {

        });
        dialogBuilder.setNegativeButton("취소", (dialog, which) -> {
        });

        AlertDialog alertDialog = dialogBuilder.create();

        setTextColor(alertDialog);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();


        Button posBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posBtn.setOnClickListener(v -> {
            if (editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
            } else {


                try {
                    int score = Integer.valueOf(editText.getText().toString().trim());
                    if (score < 0 || score > 300) {
                        Toast.makeText(context, "올바른 점수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "올바른 점수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickListener.onClick(editText);
                alertDialog.dismiss();
            }
        });

        editText.postDelayed(() -> TextViewUtil.showKeyBoard(context, editText), 300);
        return alertDialog;
    }

    public static void setTextColor(AlertDialog dialog) {
        dialog.setOnShowListener(arg0 -> {
            Context context = dialog.getButton(AlertDialog.BUTTON_NEGATIVE).getContext();
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.startColor));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.startColor));
        });
    }

    public static void showProgressDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}

