package com.dk.project.bowling.ui.fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.RecyclerView;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.FragmentMainInfoBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.bowling.ui.adapter.RecentScoresAdapter;
import com.dk.project.bowling.viewModel.MainInfoViewModel;
import com.dk.project.post.base.BindFragment;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class MainInfoFragment extends BindFragment<FragmentMainInfoBinding, MainInfoViewModel> {


    private RecentScoresAdapter recentScoresAdapter;
    //  private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private PublishSubject<Boolean> nextDataSubject = PublishSubject.create();

    private boolean isLoading = true;


    public static MainInfoFragment newInstance() {
        MainInfoFragment youtubeFragment = new MainInfoFragment();
        return youtubeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_info;
    }


    @Override
    protected MainInfoViewModel createViewModel() {
        return ViewModelProviders.of(this).get(MainInfoViewModel.class);
    }

    @Override
    protected void registerLiveData() {
        viewModel.getRecentAvgLiveData().observe(this, responseModel -> { // 최근 10게임 평균
            ScoreModel scoreModel = responseModel.getData();
//      binding.recentAvgText.setText(String.valueOf(scoreModel.getAvgScore()));
//      binding.recentMaxText.setText(String.valueOf(scoreModel.getMaxScore()));
//      binding.recentMinText.setText(String.valueOf(scoreModel.getMinScore()));

        });

        viewModel.getMonthAvgLiveData().observe(this, responseModel -> { // 이번달 평균
            ScoreModel scoreModel = responseModel.getData();
            recentScoresAdapter.setMonthAvg(scoreModel);
//      binding.monthAvgText.setText(String.valueOf(scoreModel.getAvgScore()));
//      binding.monthMaxText.setText(String.valueOf(scoreModel.getMaxScore()));
//      binding.monthMinText.setText(String.valueOf(scoreModel.getMinScore()));

        });

        viewModel.getAvgListLiveData().observe(this, responseModel -> { // 일평균 목록
            isLoading = false;
            recentScoresAdapter.setRecentScoreList(responseModel.first.getData(), responseModel.second);
        });

        //점수 등록했을 때 갱신
        ((MainActivity) viewModel.getContext()).getViewModel().getScoreListLiveData().observe(this, scoreModel -> {
            isLoading = true;
            viewModel.getMainInfoData();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // 일평균 점수 페이징
        nextDataSubject.throttleFirst(100, TimeUnit.MILLISECONDS)
                .filter(aBoolean -> aBoolean)
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(aBoolean -> {
                    isLoading = true;
                    viewModel.getRecentAvgLive(recentScoresAdapter.getItemCount() - 3);
                });

        binding.recentScoresRecycler.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                        nextDataSubject.onNext(!isLoading &&
                                (recentScoresAdapter.getItemCount() - 3) % 20 == 0 &&
                                gridLayoutManager.findLastVisibleItemPosition() == recentScoresAdapter.getItemCount() - 1));

        recentScoresAdapter = new RecentScoresAdapter();
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else if (position < 3) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        binding.recentScoresRecycler.setLayoutManager(gridLayoutManager);
        binding.recentScoresRecycler.setItemAnimator(new DefaultItemAnimator());
        binding.recentScoresRecycler.setAdapter(recentScoresAdapter);


        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        binding.recentScoresRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (gestureDetector.onTouchEvent(e)) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    int position = rv.getChildAdapterPosition(child);
                    viewModel.getScoreDayList(recentScoresAdapter.getItem(position));


                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {

                        }
                    });
                }
                return super.onInterceptTouchEvent(rv, e);
            }
        });

        viewModel.getMainInfoData();

        super.onViewCreated(view, savedInstanceState);

    }
}
