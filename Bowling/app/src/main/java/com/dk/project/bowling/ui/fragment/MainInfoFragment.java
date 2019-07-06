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
import com.dk.project.bowling.retrofit.MutableLiveDataManager;
import com.dk.project.bowling.ui.adapter.RecentScoresAdapter;
import com.dk.project.bowling.viewModel.MainInfoViewModel;
import com.dk.project.post.base.BindFragment;

public class MainInfoFragment extends BindFragment<FragmentMainInfoBinding, MainInfoViewModel> {

    private RecentScoresAdapter recentScoresAdapter;
    private GridLayoutManager gridLayoutManager;

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

        //점수 등록했을 때 갱신
        MutableLiveDataManager.getInstance().getWriteScoreLiveData().observe(this, scoreModel -> {

        });

        MutableLiveDataManager.getInstance().getScoreMonthAvgList().observe(this, recentScoresAdapter::setScoreAvgModel);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recentScoresAdapter = new RecentScoresAdapter(mContext);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                        return 2;
                    default:
                        return 1;
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
                    if (position > 2) {
                        viewModel.getScoreDayList(recentScoresAdapter.getItem(position));
                    }
                }
                return super.onInterceptTouchEvent(rv, e);
            }
        });


        super.onViewCreated(view, savedInstanceState);

    }
}
