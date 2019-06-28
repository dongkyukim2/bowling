package com.dk.project.bowling.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.dk.project.post.base.BindFragment;

import java.util.ArrayList;

public class MainViewPagerFragmentAdapter extends FragmentStateAdapter {

    private ArrayList<BindFragment> fragmentArrayList = new ArrayList<>();

    public MainViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void setFragment(BindFragment bindFragment) {
        fragmentArrayList.add(bindFragment);
    }
}
