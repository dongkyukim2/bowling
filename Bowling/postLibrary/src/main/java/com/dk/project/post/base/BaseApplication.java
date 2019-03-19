package com.dk.project.post.base;

import android.app.Application;
import com.dk.project.post.utils.AppExecutors;
import com.dk.project.post.utils.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by dkkim on 2017-10-05.
 */

public class BaseApplication extends Application {

  private AppExecutors mAppExecutors;

  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    mAppExecutors = new AppExecutors();
  }

   /*
   public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
    */
}
