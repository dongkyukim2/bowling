package com.dk.project.post.ui.activity;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityLoginBinding;
import com.dk.project.post.utils.SessionCallback;
import com.dk.project.post.viewModel.LoginViewModel;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

public class LoginActivity extends BindActivity<ActivityLoginBinding, LoginViewModel> {

    private SessionCallback callback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginViewModel getViewModel() {
        return ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    protected void subscribeToModel() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            // 액세스토큰 유효하거나 리프레시 토큰으로 액세스 토큰 갱신을 시도할 수 있는 경우
            //세션이 열린 경우 사용자 정보 가져오기

            System.out.println("========================  1111111");
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                @Override
                public void onSuccess(MeV2Response result) {
                    long userCode = result.getId(); // 로그인 성공후 유저 고유 키
                    Toast.makeText(LoginActivity.this, "자동 로그인 성공", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LoginInfoActivity.class));
                    finish();
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    System.out.println("==============   4");
                    Toast.makeText(LoginActivity.this, "세션 닫힘", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            // 무조건 재로그인을 시켜야 하는 경우
            System.out.println("========================  222222");
            Toast.makeText(LoginActivity.this, "세션 닫힘2", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
