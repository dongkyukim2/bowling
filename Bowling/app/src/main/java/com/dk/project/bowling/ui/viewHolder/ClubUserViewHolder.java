package com.dk.project.bowling.ui.viewHolder;

import android.text.TextUtils;
import android.view.View;

import androidx.core.util.Pair;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderClubUserBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubUserModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ToastUtil;

public class ClubUserViewHolder extends BindViewHolder<ViewHolderClubUserBinding, ScoreClubUserModel> implements View.OnClickListener {


    private ScoreClubUserModel userModel;

    public ClubUserViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreClubUserModel item, int position) {
        userModel = item;
        binding.userNameText.setText(item.getUserName());
        binding.userCheckBox.setClickable(false);

        if (item.isCheck()) {
            binding.userCheckBox.setBackgroundResource(R.drawable.check_enable);
        } else {
            binding.userCheckBox.setBackgroundResource(R.drawable.check_disable);
        }

        binding.joinNo.setOnClickListener(this);
        binding.joinYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.join_no:
                AlertDialogUtil.showAlertDialog(binding.userNameText.getContext(), null, "가입을 거부 하시겠습니까?", (dialog, which) -> {
                    setJoinType(Define.USER_TYPE_JOIN_NEGATIVE);
                }, (dialog, which) -> {
                });
                break;
            case R.id.join_yes:
                AlertDialogUtil.showAlertDialog(binding.userNameText.getContext(), null, "가입을 수락 하시겠습니까?", (dialog, which) -> {
                    setJoinType(Define.USER_TYPE_JOIN);
                }, (dialog, which) -> {
                });
                break;
        }
    }

    private void setJoinType(int type) {
        ClubUserModel clubUserModel = new ClubUserModel();
        clubUserModel.setClubId(userModel.getClubId());
        clubUserModel.setUserId(userModel.getUserId());
        clubUserModel.setType(type);

        BowlingApi.getInstance().setModifyClubUserType(clubUserModel, receivedData -> {
            if (receivedData.isSuccess()) {
                if (!TextUtils.isEmpty(receivedData.getMessage())) {
                    ToastUtil.showToastCenter(binding.userNameText.getContext(), receivedData.getMessage());
                }
                RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_CLUB_USER_LIST, null));
            }
        }, errorData -> {
        });
    }
}