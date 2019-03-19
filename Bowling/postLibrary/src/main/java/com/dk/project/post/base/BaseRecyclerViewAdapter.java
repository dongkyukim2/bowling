package com.dk.project.post.base;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by dkkim on 2017-04-30.
 */

public abstract class BaseRecyclerViewAdapter<VH extends BindViewHolder> extends RecyclerView.Adapter<VH> implements Define {

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public final void unSubscribeFromObservable() {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.dispose(); // 객체 받을수 없다.
//            compositeDisposable.clear(); // 객체 받을수 있다.
      compositeDisposable = null;
    }
  }

  public final void executeRx(Disposable disposable) {
    compositeDisposable.add(disposable);
  }
}
