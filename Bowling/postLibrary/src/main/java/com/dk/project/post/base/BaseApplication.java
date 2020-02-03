package com.dk.project.post.base;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.dk.project.post.R;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AppExecutors;
import com.dk.project.post.utils.ImagePipelineConfigFactory;
import com.dk.project.post.utils.KakaoSDKAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.kakao.auth.KakaoSDK;

import static com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G;
import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

/**
 * Created by dkkim on 2017-10-05.
 */

public class BaseApplication extends Application implements LifecycleObserver {

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

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

        MobileAds.getRequestConfiguration()
                .toBuilder()
                .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(MAX_AD_CONTENT_RATING_G)
                .build();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    public static BaseApplication getGlobalApplicationContext() {
        return obj;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onForeground() {
        System.out.println("++++++++++    onForeground");
        LoginManager.getInstance().startTimer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onBackground() {
        System.out.println("++++++++++    onBackground");
        LoginManager.getInstance().stopTimer();
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
