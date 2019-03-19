package com.dk.project.post.base;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dkkim on 2017-04-12.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements Define {


  public BaseViewHolder(@NonNull View itemView) {
    super(itemView);
  }
}
