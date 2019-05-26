package com.dk.project.bowling.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.bowling.ui.fragment.ClubDetailFragment;

import java.util.ArrayList;

public class ClubDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tabList = new ArrayList<>();
    private ClubModel clubModel;


    public ClubDetailPagerAdapter(@NonNull FragmentManager fm, int behavior, ClubModel clubModel) {
        super(fm, behavior);
        this.clubModel = clubModel;
        tabList.add("홈");
        tabList.add("기록");
        tabList.add("게시판");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ClubDetailFragment.newInstance(position, clubModel);
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
