package com.dk.project.post.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.bumptech.glide.Glide;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by dkkim on 2017-12-17.
 */

public class BaseService extends Service implements Define {

  private CompositeDisposable compositeDisposable = new CompositeDisposable();


  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onCreate");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onStartCommand");
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    Glide.get(this).trimMemory(level);
    if (level == TRIM_MEMORY_UI_HIDDEN) {
    }
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onTrimMemory");
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    super.onTaskRemoved(rootIntent);
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onTaskRemoved");
  }


  @Override
  public void onLowMemory() {
    super.onLowMemory();
    Glide.get(this).clearMemory();
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onLowMemory");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unSubscribeFromObservable();
    System.out.println("aaaaaaaaaaa      " + getClass().getSimpleName() + "       onDestroy");
  }

  private void unSubscribeFromObservable() {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.dispose(); // 객체 받을수 없다.
//            compositeDisposable.clear(); // 객체 받을수 있다.
      compositeDisposable = null;
    }
  }

  protected void executeRx(Disposable disposable) {
    compositeDisposable.add(disposable);
  }
}
