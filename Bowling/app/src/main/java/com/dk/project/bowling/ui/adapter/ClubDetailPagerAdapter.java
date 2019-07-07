package com.dk.project.bowling.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.dk.project.bowling.ui.fragment.ClubDetailFragment;
import com.dk.project.bowling.ui.fragment.ClubGameListFragment;
import com.dk.project.bowling.viewModel.ClubDetailViewModel;
import com.dk.project.post.ui.fragment.ContentsListFragment;

import java.util.ArrayList;

public class ClubDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tabList = new ArrayList<>();
    private ClubDetailViewModel viewModel;


    public ClubDetailPagerAdapter(@NonNull FragmentManager fm, int behavior, ClubDetailViewModel viewModel) {
        super(fm, behavior);
        this.viewModel = viewModel;
        tabList.add("홈");
        tabList.add("기록");
        tabList.add("게시판");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ClubDetailFragment.newInstance(position, viewModel.getClubModel(), viewModel.isSign());
            case 1:
                return ClubGameListFragment.newInstance(position, viewModel.getClubModel());
            default:
                return ContentsListFragment.newInstance();
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }
}
