package com.dk.project.post.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.retrofit.PostApi;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ReplyMoreViewModel extends BaseViewModel {

  public ReplyMoreViewModel(@NonNull Application application) {
    super(application);
  }

  @Override
  public void onThrottleClick(View view) {

  }
}
