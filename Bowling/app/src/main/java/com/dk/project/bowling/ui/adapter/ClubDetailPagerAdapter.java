package com.dk.project.bowling.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dk.project.bowling.ui.fragment.ClubDetailFragment;
import com.dk.project.bowling.ui.fragment.ClubGameListFragment;
import com.dk.project.bowling.viewModel.ClubDetailViewModel;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.ui.fragment.ContentsListFragment;

import java.util.ArrayList;

public class ClubDetailPagerAdapter extends FragmentStateAdapter {

    private ArrayList<BindFragment> fragmentList = new ArrayList<>();
    private ClubDetailViewModel viewModel;


    public ClubDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, ClubDetailViewModel viewModel) {
        super(fragmentActivity);

        this.viewModel = viewModel;

        fragmentList.add(ClubDetailFragment.newInstance(0, viewModel.getClubModel(), viewModel.isSign()));
        fragmentList.add(ClubGameListFragment.newInstance(1, viewModel.getClubModel()));
        fragmentList.add(ContentsListFragment.newInstance(viewModel.getClubModel().getClubId()));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }


}
