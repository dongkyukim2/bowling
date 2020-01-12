package com.dk.project.post.ui.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.databinding.ViewDataBinding;

import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ToastUtil;

/**
 * Created by dkkim on 2018-01-29.
 */

public class BaseContentsViewHolder<B extends ViewDataBinding, T> extends BindViewHolder<B, T> {

    private static boolean requestDeletePost = false;

    public BaseContentsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(T item, int position) {

    }

    public void startModify(Context context, PostModel postModel) {
        Intent intent = new Intent(context, WriteActivity.class);
        intent.putExtra(POST_MODEL, postModel);
        context.startActivity(intent);
    }

    public void startDelete(Context context, PostModel postModel) {
        if (requestDeletePost) {
            ToastUtil.showWaitToastCenter(context);
            return;
        }
        requestDeletePost = true;
        AlertDialogUtil.showAlertDialog(context, null, "삭제 하시겠습니까?", (dialog, which) ->
                PostApi.getInstance().deletePost(postModel.getPostId(), receivedData -> {
                    if (receivedData.isSuccess()) {
                        RxBus.getInstance().eventPost(new Pair(EVENT_DELETE_POST, receivedData.getData()));
                    } else {
                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                    requestDeletePost = false;
                }, errorData -> {
                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                    requestDeletePost = false;
                }), (dialog, which) -> {
        });
    }

    public void refreshWriteDate() {
    }

}
