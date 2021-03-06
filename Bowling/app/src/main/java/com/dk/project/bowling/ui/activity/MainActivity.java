package com.dk.project.bowling.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityMainBinding;
import com.dk.project.bowling.shareData.ShareData;
import com.dk.project.bowling.ui.adapter.MainViewPagerFragmentAdapter;
import com.dk.project.bowling.ui.fragment.ClubFragment;
import com.dk.project.bowling.ui.fragment.GraphFragment;
import com.dk.project.bowling.ui.fragment.MainInfoFragment;
import com.dk.project.bowling.viewModel.MainViewModel;
import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ScoreModel;
import com.dk.project.post.bowling.retrofit.MutableLiveDataManager;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.RetroClient;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.ui.fragment.ContentsListFragment;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ToastUtil;
import com.dk.project.post.utils.YoutubeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Calendar;

public class MainActivity extends BindActivity<ActivityMainBinding, MainViewModel> implements OnNavigationItemSelectedListener {

    private BottomSheetBehavior bottomSheetBehavior;
    private MainViewPagerFragmentAdapter mainViewPagerFragmentAdapter;

    public boolean requestWriteSore = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(MainViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setViewModel(viewModel);

        viewModel.checkShare();

        GlideApp.with(this)
                .asBitmap()
                .load(R.drawable.side_bg2)
                .centerCrop()
//                .apply(bitmapTransform(new BlurTransformation(5, 1)))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        binding.drawerLayoutRoot.setBackground(drawable);
                        return false;
                    }
                }).submit();


        binding.navigation.setOnNavigationItemSelectedListener(this);
        toolbarRightButton.setVisibility(View.INVISIBLE);
        toolbarRightAniButton.setVisibility(View.VISIBLE);
        toolbarLeftButton.setVisibility(View.VISIBLE);
        toolbarLeftButton.setImageResource(R.drawable.ic_action_menu);

        mainViewPagerFragmentAdapter = new MainViewPagerFragmentAdapter(this);
        mainViewPagerFragmentAdapter.setFragment(MainInfoFragment.newInstance());
        mainViewPagerFragmentAdapter.setFragment(GraphFragment.newInstance());
        mainViewPagerFragmentAdapter.setFragment(ClubFragment.newInstance());
        mainViewPagerFragmentAdapter.setFragment(ContentsListFragment.newInstance());

        binding.mainViewPager.setUserInputEnabled(false);
        binding.mainViewPager.setOffscreenPageLimit(3);
        binding.mainViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.mainViewPager.setAdapter(mainViewPagerFragmentAdapter);
        binding.mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(binding.rlBottomSheet);

        binding.userName.setSelected(true);
        setUserInfoView();

        YoutubeUtil.selectYoutubeDb();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (LoginManager.getInstance().isLogIn()) {
            setUserInfoView();
        }
        viewModel.checkShare();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().stopTimer();
        LoginManager.clear();
        RetroClient.clear();
        com.dk.project.post.bowling.retrofit.RetroClient.clear();
        MutableLiveDataManager.clear();
        BaseActivity.getActivityStack().clear();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                binding.mainViewPager.setCurrentItem(0, true);
                return true;
            case R.id.navigation_dashboard:
                binding.mainViewPager.setCurrentItem(1, true);
                return true;
            case R.id.navigation_club:
                binding.mainViewPager.setCurrentItem(2, true);
                return true;
            case R.id.navigation_community:
                binding.mainViewPager.setCurrentItem(3, true);
                return true;
        }
        return false;
    }

    @Override
    public void onToolbarLeftClick() {
        binding.mainDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onToolbarRightClick() {

        if (!LoginManager.getInstance().isLogIn()) {
            AlertDialogUtil.showLoginAlertDialog(this);
            return;
        }
        if (binding.navigation.getSelectedItemId() == R.id.navigation_club) {
            if (ShareData.getInstance().getSignCLubCount() >= 5) {
                ToastUtil.showToastCenter(this, "최대 5개까지 생성 할 수 있습니다.");
                return;
            }
            startActivity(new Intent(this, CreateClubActivity.class));
        } else if (binding.navigation.getSelectedItemId() == R.id.navigation_community) {
            startActivity(new Intent(this, WriteActivity.class));
        } else {
            if (requestWriteSore) {
                ToastUtil.showWaitToastCenter(this);
                return;
            }
            requestWriteSore = true;

            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_score_input, null);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            AppCompatTextView scoreDate = view.findViewById(R.id.score_date);
            AppCompatTextView scoreTime = view.findViewById(R.id.score_time);
            AppCompatEditText scoreText = view.findViewById(R.id.score_text);
            AppCompatEditText commentText = view.findViewById(R.id.comment_text);

            viewModel.setDate(scoreDate, calendar);
            viewModel.setTime(scoreTime, calendar);

            scoreDate.setOnClickListener(v -> new DatePickerDialog(this,
                    (view1, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        viewModel.setDate(scoreDate, calendar);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show());

            scoreTime.setOnClickListener(v -> new TimePickerDialog(this,
                    (view1, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        viewModel.setTime(scoreTime, calendar);
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show());

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);
            builder.setPositiveButton("확인", (dialog, which) -> {
            });
            builder.setNegativeButton("취소", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.setOnDismissListener(dialog -> {
                requestWriteSore = false;
            });
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (TextUtils.isEmpty(scoreText.getText().toString().trim())) {
                    Toast.makeText(this, "점수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    requestWriteSore = false;
                    return;
                }
                int score;
                try {
                    score = Integer.valueOf(scoreText.getText().toString());
                    if (score < 0 || score > 300) {
                        Toast.makeText(this, "올바른 점수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                        requestWriteSore = false;
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    requestWriteSore = false;
                    return;
                }
                ScoreModel scoreModel = new ScoreModel();
                scoreModel.setScore(score);
                scoreModel.setPlayDateTime(calendar.getTimeInMillis());
                scoreModel.setComment(commentText.getText().toString().trim());
                viewModel.writeScore(scoreModel);
                alertDialog.dismiss();
            });
        }
    }

    @Override
    public void onBackPressed() {
        boolean isExit = false;
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
            isExit = true;
        }
//        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//            isExit = true;
//        }
        if (isExit) {
            return;
        }

        super.onBackPressed();
    }

    public BottomSheetBehavior getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }

    public MainViewPagerFragmentAdapter getMainViewPagerFragmentAdapter() {
        return mainViewPagerFragmentAdapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setUserInfoView() {
        if (LoginManager.getInstance().isLogIn()) {
            LoginInfoModel loginInfoModel = LoginManager.getInstance().getLoginInfoModel();
            binding.userName.setText(loginInfoModel.getUserName());
            if (!TextUtils.isEmpty(loginInfoModel.getUserPhoto())) {
                GlideApp.with(this).applyDefaultRequestOptions(ImageUtil.getGlideRequestOption())
                        .load(Define.IMAGE_URL + loginInfoModel.getUserPhoto())
                        .centerCrop()
                        .into(binding.imageView);
            }
            binding.loginParent.setVisibility(View.GONE);
            binding.logoutParent.setVisibility(View.VISIBLE);
            binding.settingParent.setVisibility(View.VISIBLE);
        } else {
            binding.loginParent.setVisibility(View.VISIBLE);
            binding.logoutParent.setVisibility(View.GONE);
            binding.settingParent.setVisibility(View.GONE);
        }
    }
}
