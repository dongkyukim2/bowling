package com.dk.project.post.utils;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.dk.project.post.base.BaseActivity;

/**
 * Created by dkkim on 2017-11-24.
 */

public class FragmentUtil {

  public static void replaceFragment(BaseActivity activity, @IdRes int layoutId, Fragment fragment) {
    FragmentManager fm = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fm.beginTransaction();
    fragmentTransaction.replace(layoutId, fragment);
    fragmentTransaction.commit();
  }
}
