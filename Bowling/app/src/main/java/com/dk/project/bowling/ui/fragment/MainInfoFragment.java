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
import androidx.recyclerview.widget.RecyclerView;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.FragmentMainInfoBinding;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.bowling.ui.adapter.RecentScoresAdapter;
import com.dk.project.bowling.viewModel.GraphViewModel;
import com.dk.project.bowling.viewModel.MainInfoViewModel;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.bowling.retrofit.MutableLiveDataManager;

public class MainInfoFragment extends BindFragment<FragmentMainInfoBinding, MainInfoViewModel> {

    private RecentScoresAdapter recentScoresAdapter;
    private GridLayoutManager gridLayoutManager;
    private GraphViewModel graphViewModel;

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
        viewModel.getWriteScoreLiveData().observe(this, scoreModel -> {

        });

        // 월평균 목록(일별)
        MutableLiveDataManager.getInstance().getScoreMonthAvgList().observe(this, scoreAvgModel -> {
            if (scoreAvgModel.getMonthAvg() == null) {
                binding.scoreLeft.setText("0");
                binding.scoreRight.setText("0");
                binding.scoreGraph.setMax(300);
                binding.scoreGraph.setProgress(0);
            } else {
                binding.scoreLeft.setText(String.valueOf(scoreAvgModel.getMonthAvg().getMaxScore()));
                binding.scoreRight.setText(String.valueOf(scoreAvgModel.getMonthAvg().getMinScore()));
                binding.scoreGraph.setMax(300);
                binding.scoreGraph.setProgress(scoreAvgModel.getMonthAvg().getAvgScore());
            }
            recentScoresAdapter.setRecentScoreList(scoreAvgModel.getMonthAvgList(), true);

            graphViewModel = ((GraphFragment) ((MainActivity) mContext).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel();
            if (graphViewModel == null) {
                binding.scoreGraphDate.postDelayed(() -> {
                    graphViewModel = ((GraphFragment) ((MainActivity) mContext).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel();
                    binding.scoreGraphDate.setText(graphViewModel.getYearMonth());
                }, 300);
            } else {
                binding.scoreGraphDate.setText(graphViewModel.getYearMonth());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recentScoresAdapter = new RecentScoresAdapter(mContext);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
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
                    return true;
                }
                return super.onInterceptTouchEvent(rv, e);
            }
        });
        binding.setViewModel(viewModel);
        super.onViewCreated(view, savedInstanceState);

    }
}
