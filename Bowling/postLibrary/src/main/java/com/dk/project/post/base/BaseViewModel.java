package com.dk.project.post.base;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.TimeUnit;

public abstract class BaseViewModel extends AndroidViewModel {

  protected BaseActivity mContext;
  protected BindFragment bindFragment;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  protected PublishSubject<View> clickPublishSubject = PublishSubject.create();

  public BaseViewModel(@NonNull Application application) {
    super(application);
    executeRx(clickPublishSubject.
        throttleFirst(500, TimeUnit.MILLISECONDS).
        observeOn(AndroidSchedulers.mainThread()).
        subscribe(this::onThrottleClick));
  }



  protected void onCreate() {
  }

  protected void onCreated() {
  }

  protected void onResume() {
  }

  protected void onPause() {
  }

  protected void onDestroy() {
    unSubscribeFromObservable();
  }

  public void executeRx(Disposable disposable) {
    if(compositeDisposable == null){
    } else {
      compositeDisposable.add(disposable);
    }

  }

  public void setContext(BaseActivity context) {
    this.mContext = context;
  }

  public BaseActivity getContext() {
    return mContext;
  }

  public BindFragment getBindFragment() {
    return bindFragment;
  }

  public void setBindFragment(BindFragment bindFragment) {
    this.bindFragment = bindFragment;
  }

  private void unSubscribeFromObservable() {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.dispose(); // 객체 받을수 없다.
//            compositeDisposable.clear(); // 객체 받을수 있다.
      compositeDisposable = null;
      mContext = null;
    }
  }

  public final void onClick(View view) {
    clickPublishSubject.onNext(view);
  }

  public abstract void onThrottleClick(View view);
}
