package com.dk.project.bowling.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityMainBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.fragment.GraphFragment;
import com.dk.project.bowling.ui.fragment.MainInfoFragment;
import com.dk.project.bowling.viewModel.MainViewModel;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.controller.LoginController;
import com.dk.project.post.ui.activity.LoginActivity;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.ui.fragment.ContentsListFragment;
import com.dk.project.post.utils.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import java.util.Calendar;

public class MainActivity extends BindActivity<ActivityMainBinding, MainViewModel> implements OnNavigationItemSelectedListener {


    private FragmentManager fragmentManager;
    private MainInfoFragment mainInfoFragment;
    private GraphFragment graphFragment;
    private ContentsListFragment contentsListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.checkShare();

        binding.navigation.setOnNavigationItemSelectedListener(this);
        toolbarRightButton.setVisibility(View.GONE);
        toolbarRightAniButton.setVisibility(View.VISIBLE);

        mainInfoFragment = MainInfoFragment.newInstance();
        graphFragment = GraphFragment.newInstance();
        contentsListFragment = ContentsListFragment.newInstance();

        setFragment(mainInfoFragment, "info");

        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        viewModel.checkShare();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                setFragment(mainInfoFragment, "info");
                return true;
            case R.id.navigation_dashboard:
                setFragment(graphFragment, "graph");
                return true;
            case R.id.navigation_community:
                setFragment(contentsListFragment, "content");
                return true;
        }
        return false;
    }

    @Override
    public void onToolbarRightClick() {
        if(!LoginController.getInstance().isLogin()){
            Toast.makeText(this,"로그인 후 이용해주세요",Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.navigation.getSelectedItemId() == R.id.navigation_community) {
            startActivity(new Intent(this, WriteActivity.class));
        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_score_input, null);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            AppCompatTextView scoreDate = view.findViewById(R.id.score_date);
            AppCompatTextView scoreTime = view.findViewById(R.id.score_time);
            AppCompatEditText scoreText = view.findViewById(R.id.score_text);
            AppCompatEditText commentText = view.findViewById(R.id.comment_text);

            setDate(scoreDate, calendar);
            setTime(scoreTime, calendar);

            scoreDate.setOnClickListener(v -> new DatePickerDialog(this,
                    (view1, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        setDate(scoreDate, calendar);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show());

            scoreTime.setOnClickListener(v -> new TimePickerDialog(this,
                    (view1, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(scoreTime, calendar);
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show());

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);
            builder.setPositiveButton("예", (dialog, which) -> {
            });
            builder.setNegativeButton("아니오", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (TextUtils.isEmpty(scoreText.getText().toString().trim())) {
                    Toast.makeText(this, "점수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                int score;
                try {
                    score = Integer.valueOf(scoreText.getText().toString());
                    if (score < 0 || score > 300) {
                        Toast.makeText(this, "올바른 점수를 입력해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
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

    private void setDate(AppCompatTextView textView, Calendar calendar) {
        String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
        textView.setText(calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String
                .format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    private void setTime(AppCompatTextView textView, Calendar calendar) {
        textView.setText((calendar.get(Calendar.AM_PM) == 0 ? "오전 " : "오후 ") +
                String.format("%02d", calendar.get(Calendar.HOUR)) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE)));
    }

    private void setFragment(BindFragment fragment, String tag) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment alReadyFragment = fragmentManager.findFragmentByTag(tag);

        if (alReadyFragment == null) {
            fragmentTransaction.add(R.id.main_layout, fragment, tag);
        } else {
            fragmentTransaction.show(alReadyFragment);
        }

        for (Fragment tempFragment : fragmentManager.getFragments()) {
            if (alReadyFragment != tempFragment) {
                fragmentTransaction.hide(tempFragment);
            }
        }
        fragmentTransaction.commit();
    }
}
