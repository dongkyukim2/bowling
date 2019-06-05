package com.dk.project.post.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewClickListener {


    public static void setItemClickListener(RecyclerView recyclerView, ItemClickListener itemClickListener) {
        GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if (gestureDetectorCompat.onTouchEvent(e) && childView != null) {

                    int currentPosition = rv.getChildAdapterPosition(childView);

                    itemClickListener.onItemClick(childView, currentPosition);

                    return false;
                }
                return false;

            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public static void setClickListener(RecyclerView recyclerView, GestureDetectorCompat gestureDetectorCompat, View view, ItemClickListener itemClickListener) {


        view.setOnTouchListener((v, event) -> {

            View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());

            if (gestureDetectorCompat.onTouchEvent(event) && childView != null) {
                int currentPosition = recyclerView.getChildAdapterPosition(childView);
                itemClickListener.onItemClick(childView, currentPosition);
                return true;
            }

            return false;
        });

    }

    public static GestureDetectorCompat getGestureDetectorCompat(RecyclerView recyclerView) {
        return new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
}
