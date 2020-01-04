package com.dk.project.post.viewModel;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.utils.Utils;

/**
 * Created by dkkim on 2017-10-04.
 */

public class LoginInfoViewModel extends BaseViewModel {

    private String signId;

    public LoginInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        signId = mContext.getIntent().getStringExtra(Define.ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PostApi.getInstance().requestBlockClear(Define.REQUEST_SIGN_UP);
    }

    @Override
    public void onThrottleClick(View view) {
        if (PostApi.getInstance().isRequestBlock(Define.REQUEST_SIGN_UP)) {
            return;
        }
        if (view.getId() == R.id.sign_up_btn) {
            if (TextUtils.isEmpty(signId)) {
                return;
            }
            AppCompatEditText nickNameEdt = mContext.findViewById(R.id.nick_name_edit);
            String nickName = nickNameEdt.getText().toString().trim();

            if (nickName.isEmpty()) {
                Toast.makeText(mContext, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (nickName.length() > 10) {
                Toast.makeText(mContext, "10자까지 입력가능합니다", Toast.LENGTH_SHORT).show();
            } else if (!Utils.stringCheck(nickName)) {
                Toast.makeText(mContext, "한글, 영문, 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
            } else {
                LoginInfoModel loginInfoModel = new LoginInfoModel();
                loginInfoModel.setUserId(signId);
                loginInfoModel.setUserName(nickName);
                executeRx(PostApi.getInstance().signUp(loginInfoModel,
                        receivedData -> {
                            if (receivedData.isSuccess()) { // 회원가입성공
                                LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
                                Intent intent = new Intent(Define.MAIN);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
}
