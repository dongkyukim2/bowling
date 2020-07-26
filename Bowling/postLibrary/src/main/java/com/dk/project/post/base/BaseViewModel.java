package com.dk.project.post.base;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewModel<T extends BaseActivity> extends AndroidViewModel {

    protected T mContext;
    protected BindFragment bindFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected PublishSubject<View> clickPublishSubject = PublishSubject.create();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        executeRx(clickPublishSubject.
                throttleFirst(1000, TimeUnit.MILLISECONDS).
                filter(view -> mContext != null && !mContext.isDestroyed() && !mContext.isFinishing()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onThrottleClick));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("+++++++++++++++++++++   onCleared");
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
        if (compositeDisposable == null) {
            disposable.dispose();
        } else {
            compositeDisposable.add(disposable);
        }

    }

    public void setContext(T mContext) {
        this.mContext = mContext;
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
        }
    }

    public final void onClick(View view) {
        clickPublishSubject.onNext(view);
    }

    public abstract void onThrottleClick(View view);
}
