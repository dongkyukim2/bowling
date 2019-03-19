package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.utils.ImageUtil;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

/**
 * Created by dkkim on 2017-10-04.
 */

public class IntroViewModel extends BaseViewModel {

  public IntroViewModel(@NonNull Application application) {
    super(application);
  }

  @Override
  protected void onCreated() {
    super.onCreated();
    startMainActivity();
    ImageUtil.getImagePath(mContext);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onThrottleClick(View view) {

  }

  private void startMainActivity() {

    if (mContext.getIntent().hasExtra(Define.INTRO_DELAY)) {
      Intent intent = new Intent(mContext, MainActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      mContext.startActivity(intent);
      mContext.finish();
    } else {
      executeRx(Observable.timer(1, TimeUnit.SECONDS)
          .subscribe(i -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.finish();
            mContext.startActivity(intent);
          }));
    }
  }
}
