package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.bowling.ui.activity.LoginActivity;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
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

        //todo 여기도 수정해야함
        if (mContext.getIntent().hasExtra(Define.INTRO_DELAY)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
            mContext.finish();
        } else {

            // 세션이 열려있는지
            // todo 로그인되어있으면 열려있는듯
            if (KakaoLoginUtils.checkLogin()) {
                KakaoLoginUtils.getUserInfo(new MeV2ResponseCallback() {
                    @Override
                    public void onSuccess(MeV2Response result) {
                        long userKakaoCode = result.getId();
                        executeRx(PostApi.getInstance().getUserInfo(String.valueOf(userKakaoCode),
                                receivedData -> {
                                    if (receivedData.getData() == null) {
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        mContext.startActivity(intent);
                                        mContext.finish();
                                    } else { // 세션 열려있고 디비에 가입도 되어있음
                                        LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        mContext.startActivity(intent);
                                        mContext.finish();

                                    }
                                }, errorData -> {

                                }));
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {

                    }
                });

            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
            }


            // ------------------------------   TEST   -----------------------------------

            //todo LoginInfoModel, setUserCode 중복 수정해야함
//            LoginInfoModel loginInfoModel = new LoginInfoModel();
//            loginInfoModel.setUserId("1087708735");
//            LoginManager.getInstance().setLoginInfoModel(loginInfoModel);
//            Intent intent = new Intent(mContext, MainActivity.class);
//            intent.putExtra(USER_CODE, 1087708735l);
//            mContext.startActivity(intent);
//            mContext.finish();
        }
    }
}
