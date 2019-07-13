package com.dk.project.post.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.TextViewCompat;
import com.dk.project.post.R;

/**
 * Created by dkkim on 2017-11-11.
 */

public class TextViewUtil {

    public static final int INPUT_TEXT_SIZE = 22;

    public static AppCompatEditText getEditText(Context context) {
        int padding = ScreenUtil.dpToPixel(8);
        AppCompatEditText editText = new AppCompatEditText(context) {
            @Override
            public boolean onKeyPreIme(int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (context instanceof OnKeyBoardBackKey) {
                            ((OnKeyBoardBackKey) context).onEditTextBackPress();
                        }
                    }
                }
                return super.onKeyPreIme(keyCode, event);
            }
        };
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setBackgroundResource(R.color.main_background_color);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, INPUT_TEXT_SIZE);
        editText.setTextColor(Color.parseColor("#4a5158"));
        editText.setPadding(padding + padding / 2, padding, padding + padding / 2, padding);
        editText.setOnFocusChangeListener((View.OnFocusChangeListener) context);
        return editText;
    }

    public static AppCompatEditText getEditText(Context context,
                                                View.OnClickListener onClickListener) {
        AppCompatEditText appCompatEditText = getEditText(context);
        appCompatEditText.setOnClickListener(onClickListener);
        return appCompatEditText;
    }

    public static AppCompatEditText getEditText(Context context, String inputText,
                                                View.OnClickListener onClickListener) {
        AppCompatEditText appCompatEditText = getEditText(context, inputText);
        appCompatEditText.setOnClickListener(onClickListener);
        return appCompatEditText;
    }

    public static AppCompatEditText getEditText(Context context, String inputText) {
        AppCompatEditText appCompatEditText = getEditText(context);
        appCompatEditText.setText(inputText);
        return appCompatEditText;
    }


    public static TextView getTextView(Context context) {

        int padding = ScreenUtil.dpToPixel(8);
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundResource(R.color.main_background_color);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, INPUT_TEXT_SIZE);
        textView.setTextColor(Color.parseColor("#4a5158"));
        textView.setPadding(padding + padding / 2, padding, padding + padding / 2, padding);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "content.ttf"));
        return textView;
    }

    public static TextView getTextView(Context context, String inputText) {
        TextView textView = getTextView(context);
        textView.setText(inputText);
        return textView;
    }


    public static void showKeyBoard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyBoard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public interface OnKeyBoardBackKey {

        void onEditTextBackPress();
    }
}
