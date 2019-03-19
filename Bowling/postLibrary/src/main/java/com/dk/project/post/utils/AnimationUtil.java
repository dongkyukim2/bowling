package com.dk.project.post.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

/**
 * Created by dkkim on 2017-11-24.
 */

public class AnimationUtil {

    public static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    public static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    public static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public static void setVisible(View view, boolean visible) {
        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.setDuration(300);
        if (visible) {
            ObjectAnimator scaleOnX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
            ObjectAnimator scaleOnY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
            scaleSet.playTogether(
                    scaleOnX,
                    scaleOnY
            );
            scaleSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
        } else {
            ObjectAnimator scaleOnX = ObjectAnimator.ofFloat(view, "scaleX", 0f);
            ObjectAnimator scaleOnY = ObjectAnimator.ofFloat(view, "scaleY", 0f);
            scaleSet.playTogether(
                    scaleOnX,
                    scaleOnY
            );
            scaleSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
        }
        scaleSet.start();
    }

    public static AnimatorSet getLikeAnimation(AnimatorListenerAdapter animatorListenerAdapter, View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(animatorListenerAdapter);

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
        return animatorSet;
    }


    public static void viewAnimation(View view, boolean isAnimation, int isShow, int delay) {
        if (isAnimation) {
            view.animate().scaleX(isShow).scaleY(isShow).setStartDelay(delay);
        } else {
            view.setScaleX(isShow);
            view.setScaleY(isShow);
        }
    }

    public static void viewTranslationAnimation(View view, boolean isAnimation, boolean isShow, int duration) {
        if (isShow) {
            view.animate().translationY(0).setDuration(duration).setStartDelay(2000).start();
        } else {
            view.animate().translationY(1000).setDuration(duration).start();
        }
//        if (isAnimation) {
//            view.animate().translationY(view.getHeight()).setDuration(duration).start();
//        } else {
//
//        }
    }


    public static void setZoomAnimation(TextView view, String text) {
        int duration = 50;
        AnimatorSet scaleSet = new AnimatorSet();
        ObjectAnimator zoomInX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.5f);
        zoomInX.setDuration(duration);
        ObjectAnimator zoomInY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.5f);
        zoomInY.setDuration(duration);


        scaleSet.playTogether(zoomInX, zoomInY);
        scaleSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setText(text);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AnimatorSet scaleSet = new AnimatorSet();
                ObjectAnimator zoomOutX = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1.0f);
                zoomOutX.setDuration(duration);
                ObjectAnimator zoomOutY = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1.0f);
                zoomOutY.setDuration(duration);
                scaleSet.playTogether(zoomOutX, zoomOutY);
                scaleSet.start();
            }
        });
        scaleSet.start();
    }
}



