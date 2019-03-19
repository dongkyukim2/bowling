package com.dk.project.post.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

/**
 * Created by dkkim on 2017-11-25.
 */

public class ContentViewHolderLinearLayout extends LinearLayout {

    private GestureDetectorCompat gestureDetector;

    public ContentViewHolderLinearLayout(Context context) {
        super(context);
    }

    public ContentViewHolderLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentViewHolderLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setGestureDetector(GestureDetectorCompat gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}
