package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.LoginInfoActivity;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import static com.dk.project.post.base.Define.USER_CODE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class LoginViewModel extends BaseViewModel {

    private long userKakaoCode = 0;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        userKakaoCode = mContext.getIntent().getLongExtra("USER_CODE", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        if (view.getId() == R.id.sign_up_btn) {
            AppCompatEditText nickNameEdt = mContext.findViewById(R.id.nick_name_edit);
            String nickName = nickNameEdt.getText().toString().trim();

            if (nickName.isEmpty()) {
                Toast.makeText(mContext, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (nickName.length() > 10) {
                Toast.makeText(mContext, "10자까지 입력가능합니다", Toast.LENGTH_SHORT).show();
            } else {
                LoginInfoModel loginInfoModel = new LoginInfoModel();
                loginInfoModel.setUserId(String.valueOf(userKakaoCode));
                loginInfoModel.setUserName(nickName);
                executeRx(PostApi.getInstance().signUp(loginInfoModel,
                        receivedData -> {
                            if (receivedData.getCode().equals("0000")) { // 회원가입성공
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.putExtra(USER_CODE, userKakaoCode);
                                mContext.startActivity(intent);
                                mContext.finish();
                            } else {
                                Toast.makeText(mContext, receivedData.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, errorData -> {
                        }));
            }
        }
    }

    public void kakaoLogin() {
        Session.getCurrentSession().addCallback(new ISessionCallback() { // 로그인 버튼 눌렀을때 (회원가입시)
            @Override
            public void onSessionOpened() {
                KakaoLoginUtils.getUserInfo(new MeV2ResponseCallback() {
                    @Override
                    public void onSuccess(MeV2Response result) {
                        userKakaoCode = result.getId();

                        Intent intent = new Intent(mContext, LoginInfoActivity.class);
                        intent.putExtra(USER_CODE, userKakaoCode);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        System.out.println("====================    2");
                    }
                });

            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                System.out.println("====================    3");
            }
        });
        Session.getCurrentSession().checkAndImplicitOpen();
    }
}
