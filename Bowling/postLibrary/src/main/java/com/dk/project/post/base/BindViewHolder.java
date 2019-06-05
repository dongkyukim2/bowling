package com.dk.project.post.base;

import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by dkkim on 2017-04-12.
 */

public abstract class BindViewHolder<B extends ViewDataBinding, T> extends BaseViewHolder {

    protected B binding;
    private int index;

    public BindViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void onBindView(T item, int position) {
        index = position;
    }

    public B getBinding() {
        return binding;
    }

    public int getIndex() {
        return index;
    }
}
