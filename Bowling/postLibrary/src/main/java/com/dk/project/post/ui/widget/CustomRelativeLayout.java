package com.dk.project.post.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by dkkim on 2017-12-24.
 */

public class CustomRelativeLayout extends RelativeLayout {

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println("eeeeeeeeeeeeee         0     " + getHeight());
//        super.dispatchTouchEvent(ev);
        return true;
    }
}
