package com.dk.project.post.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by dkkim on 2017-05-15.
 */

public abstract class BindFragment<B extends ViewDataBinding, T extends BaseViewModel> extends BaseFragment {

    protected B binding;
    protected T viewModel;

    protected View view;

    protected abstract int getLayoutId();

    protected abstract T createViewModel();

    protected abstract void registerLiveData();

    public T getViewModel() {
        return viewModel;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onAttach");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        viewModel = createViewModel();
        viewModel.setContext((BindActivity) getActivity());
        viewModel.setBindFragment(this);
        view = binding.getRoot();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onCreateView");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerLiveData();
        viewModel.onCreated();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onViewCreated");
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onAttachFragment");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onActivityCreated");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onPause");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onDestroy();
        if (ON_CREATE_LOG) {
            System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onDestroyView");
        }
    }
}
