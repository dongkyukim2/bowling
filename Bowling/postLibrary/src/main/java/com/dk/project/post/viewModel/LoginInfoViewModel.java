package com.dk.project.post.viewModel;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.ProgressRequestBody;
import com.dk.project.post.ui.activity.MediaSelectActivity;
import com.dk.project.post.ui.widget.CircleImageView;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.PermissionsUtil;
import com.dk.project.post.utils.ToastUtil;
import com.dk.project.post.utils.Utils;

/**
 * Created by dkkim on 2017-10-04.
 */

public class LoginInfoViewModel extends BaseViewModel {

    private String signId;
    private CircleImageView profileImage;
    private LoginInfoModel model;

    public LoginInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        profileImage = mContext.findViewById(R.id.user_profile_image);

        signId = mContext.getIntent().getStringExtra(Define.ID);
        model = LoginManager.getInstance().getLoginInfoModel();
        if (model != null) {
            signId = model.getUserId();
            ((AppCompatEditText) mContext.findViewById(R.id.nick_name_edit)).setText(model.getUserName());
            if (!TextUtils.isEmpty(model.getUserPhoto())) {
                GlideApp.with(mContext)
                        .load(Define.IMAGE_URL + model.getUserPhoto())
                        .apply(ImageUtil.getGlideConterCropRequestOption())
                        .into(profileImage);
            }
        }
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
                ToastUtil.showToastCenter(mContext, "가입 할 수 없습니다.");
                return;
            }
            AppCompatEditText nickNameEdt = mContext.findViewById(R.id.nick_name_edit);
            String nickName = nickNameEdt.getText().toString().trim();

            if (nickName.isEmpty()) {
                ToastUtil.showToastCenter(mContext, "이름을 입력해주세요");
            } else if (nickName.length() > 10) {
                ToastUtil.showToastCenter(mContext, "10자까지 입력가능합니다");
            } else if (!Utils.stringCheck(nickName)) {
                ToastUtil.showToastCenter(mContext, "한글, 영문, 숫자만 가능합니다.");
            } else {
                LoginInfoModel loginInfoModel = new LoginInfoModel();
                loginInfoModel.setUserId(signId);
                loginInfoModel.setUserName(nickName);
                loginInfoModel.setModify(model != null);
                if (ListController.getInstance().getMediaSelectList().isEmpty()) {
                    executeRx(PostApi.getInstance().signUp(loginInfoModel,
                            receivedData -> {
                                if (receivedData.isSuccess()) { // 회원가입성공
                                    LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
                                    Intent intent = new Intent(Define.MAIN);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                    ToastUtil.showToastCenter(mContext, "가입 성공했습니다.");
                                } else {
                                    ToastUtil.showToastCenter(mContext, "가입 실패했습니다.");
                                }
                            }, errorData -> ToastUtil.showToastCenter(mContext, "가입 실패했습니다.")));
                } else {
                    PostApi.getInstance().test(mContext, ListController.getInstance().getMediaSelectList(), iamgeList -> {
                        loginInfoModel.setUserPhoto(iamgeList.get(0));
                        executeRx(PostApi.getInstance().signUp(loginInfoModel,
                                receivedData -> {
                                    if (receivedData.isSuccess()) { // 회원가입성공
                                        LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
                                        Intent intent = new Intent(Define.MAIN);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        mContext.startActivity(intent);
                                        mContext.finish();
                                        ToastUtil.showToastCenter(mContext, "가입 성공했습니다.");
                                    } else {
                                        ToastUtil.showToastCenter(mContext, "가입 실패했습니다.");
                                    }
                                }, errorData -> ToastUtil.showToastCenter(mContext, "가입 실패했습니다.")));
                    }, errorData -> {

                    }, new ProgressRequestBody.ProgressListener() {
                        @Override
                        public void onUploadStart(String fileName) {

                        }

                        @Override
                        public void onRequestProgress(long bytesWritten, long contentLength) {

                        }

                        @Override
                        public void onUploadEnd(String fileName) {

                        }
                    });
                }


            }
        } else if (view.getId() == R.id.user_profile_image) {
            PermissionsUtil.isPermission(mContext, granted -> {
                if (granted) {
                    Intent intent = new Intent(mContext, MediaSelectActivity.class);
                    intent.putExtra(Define.IMAGE_MULTI_SELECT, false);
                    mContext.startActivityForResult(intent, Define.MEDIA_ATTACH_LIST);
                }
            });
        }

    }
}