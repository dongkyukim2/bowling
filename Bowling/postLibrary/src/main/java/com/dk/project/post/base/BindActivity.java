package com.dk.project.post.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.airbnb.lottie.LottieAnimationView;
import com.dk.project.post.R;
import com.dk.project.post.utils.Utils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by dkkim on 2017-10-03.
 */

public abstract class BindActivity<B extends ViewDataBinding, T extends BaseViewModel> extends BaseActivity {

    protected B binding;
    protected T viewModel;

    protected Toolbar toolbar;
    protected ImageView toolbarLeftButton;
    protected ImageView toolbarRightButton;
    protected LottieAnimationView toolbarRightAniButton;
    protected EditText toolbarRightSearch;
    protected TextView toolbarTitle;
    protected AppBarLayout appBarLayout;

    protected abstract int getLayoutId();

    protected abstract T getViewModel();

    protected abstract void subscribeToModel();

    public B getBinding() {
        return binding;
    }

    private PublishSubject<MotionEvent> touchSubject = PublishSubject.create();

    private PublishSubject<Integer> toolbarClickSubject = PublishSubject.create();

    private AdView bottomAdView;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        touchSubject.onNext(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayoutId());

        toolbar = binding.getRoot().findViewById(R.id.toolbar);
        if (toolbar != null) {
            appBarLayout = (AppBarLayout) toolbar.getParent();
        }
        if (toolbar != null) {
            toolbar.setTitleTextAppearance(this, R.style.ActionBarTitle);
            toolbarLeftButton = toolbar.findViewById(R.id.toolbar_left_button);
            toolbarRightButton = toolbar.findViewById(R.id.toolbar_right_button);
            toolbarRightSearch = toolbar.findViewById(R.id.toolbar_right_Search);
            toolbarRightAniButton = toolbar.findViewById(R.id.toolbar_right_ani_button);
            toolbarRightAniButton.setAnimation("lottie/pencil_write.json");

            toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
            toolbarLeftButton.setOnClickListener(view -> toolbarClickSubject.onNext(0));
            toolbarRightButton.setOnClickListener(view -> toolbarClickSubject.onNext(1));
            toolbarRightAniButton.setOnClickListener(view -> toolbarClickSubject.onNext(1));
            toolbarLeftButton.setVisibility(View.INVISIBLE);
            toolbarRightButton.setVisibility(View.INVISIBLE);
        }
        bottomAdView = binding.getRoot().findViewById(R.id.ad_view_bottom);
        Utils.loadAdView(bottomAdView);

        viewModel = getViewModel();
        viewModel.setContext(this);

//    viewModel.executeRx(touchSubject.
//        throttleFirst(300, TimeUnit.MILLISECONDS)
//        .observeOn(AndroidSchedulers.mainThread()).
//            subscribe(o -> ContentsContextMenuManager.getInstance().hideContextMenu()));

        viewModel.executeRx(toolbarClickSubject.
                throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(this::onToolbarClick));


        viewModel.onCreate();

        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onCreate");
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        subscribeToModel();
        viewModel.onCreated();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onPostCreate");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onNewIntent");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomAdView != null) {
            bottomAdView.resume();
        }
        viewModel.onResume();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onResume");
        }
    }

    @Override
    protected void onPause() {
        if (bottomAdView != null) {
            bottomAdView.pause();
        }
        super.onPause();
        viewModel.onPause();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onPause");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onDestroy");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    }

    private void onToolbarClick(int value) {
        if (value == 0) {
            onToolbarLeftClick();
        } else {
            onToolbarRightClick();
        }
    }

    public void onToolbarLeftClick() {
    }

    public void onToolbarRightClick() {
    }

}
