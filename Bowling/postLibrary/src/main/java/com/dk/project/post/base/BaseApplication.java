package com.dk.project.post.base;

import android.app.Application;

import com.dk.project.post.utils.AppExecutors;
import com.dk.project.post.utils.ImagePipelineConfigFactory;
import com.dk.project.post.utils.KakaoSDKAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.kakao.auth.KakaoSDK;

/**
 * Created by dkkim on 2017-10-05.
 */

public class BaseApplication extends Application {

    private AppExecutors mAppExecutors;
    private static volatile BaseApplication obj = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
//        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        Fresco.initialize(this, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));
        mAppExecutors = new AppExecutors();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }


    public static BaseApplication getGlobalApplicationContext() {
        return obj;
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
