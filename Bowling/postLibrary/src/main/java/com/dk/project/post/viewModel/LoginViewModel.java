package com.dk.project.post.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.PostApi;

/**
 * Created by dkkim on 2017-10-04.
 */

public class LoginViewModel extends BaseViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
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
                loginInfoModel.setUserId("awdawdawd");
                loginInfoModel.setUserName(nickName);
                executeRx(PostApi.getInstance().signUp(loginInfoModel,
                        receivedData -> {
                            if (receivedData.getCode().equals("0000")) {
                                Toast.makeText(mContext, "가입 성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, receivedData.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, errorData -> {
                        }));
            }
        }
    }
}
