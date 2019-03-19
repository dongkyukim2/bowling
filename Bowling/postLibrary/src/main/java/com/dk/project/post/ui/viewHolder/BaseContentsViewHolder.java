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

/**
 * Created by dkkim on 2018-01-29.
 */

public class BaseContentsViewHolder<B extends ViewDataBinding, T> extends BindViewHolder<B, T> {

  public BaseContentsViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  public void onBindView(T item) {

  }

  public void startModify(Context context, PostModel postModel) {
    Intent intent = new Intent(context, WriteActivity.class);
    intent.putExtra(POST_MODEL, postModel);
    context.startActivity(intent);
  }

  public void startDelete(Context context, PostModel postModel) {
    AlertDialogUtil.showAlertDialog(context, null, "삭제 하시겠습니까?", (dialog, which) ->
        PostApi.getInstance().deletePost(postModel.getPostId(), receivedData -> {
          if (receivedData.isSuccess()) {
            RxBus.getInstance().eventPost(new Pair(EVENT_DELETE_POST, receivedData.getData()));
          } else {
            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
          }
        }, errorData -> Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()));
  }

  public void refreshWriteDate() {
  }

}
