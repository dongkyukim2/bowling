package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.bowling.ui.activity.ClubUserListActivity;
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;

import java.util.ArrayList;
import java.util.Collections;

public class CreateGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<UserModel> userList = new ArrayList<>();
    private MutableLiveData<Integer> checkCountLiveData = new MutableLiveData<>();

    private Context mContext;
    private ClubModel clubModel;

    int teamCount = 0;

    public CreateGameAdapter(Context context, ClubModel clubModel) {
        mContext = context;
        this.clubModel = clubModel;
    }

    @Override
    public CreateGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGameViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(CreateGameViewHolder holder, int position) {
        UserModel userModel = userList.get(position);
        holder.onBindView(userModel, position);

        View.OnClickListener onClickListener = v -> {

            switch (v.getId()) {
                case R.id.user_invite_icon:
                    Intent intent = new Intent(mContext, ClubUserListActivity.class);
                    intent.putExtra(CLUB_MODEL, clubModel);
                    ((BindActivity) mContext).startActivityForResult(intent, Define.CLUB_USER_LIST);
                    break;
                case R.id.drag_icon:

                    break;
                default:
                    boolean check = !userModel.isCheck();
                    if (userModel.getViewType() == 0) {
                        userModel.setCheck(check);
                        int startIndex = position;
                        int count = 1;

                        for (int i = position + 1; i < userList.size(); i++) {
                            if (userList.get(i).getViewType() == 0) {
                                break;
                            } else {
                                userList.get(i).setCheck(check);
                                count++;
                            }
                        }

                        notifyItemRangeChanged(startIndex, count);
                    } else {
                        userModel.setCheck(check);

                        Pair<Integer, Boolean> headerIndex = checkTeamSelectAll(position);

                        userList.get(headerIndex.first).setCheck(headerIndex.second);
                        notifyItemChanged(headerIndex.first);


                        notifyItemChanged(position);
                    }


                    int checkUserCount = 0;
                    for (UserModel tempUserModel : userList) {
                        if (tempUserModel.isCheck()) {
                            checkUserCount++;
                        }
                    }
                    checkCountLiveData.setValue(checkUserCount);
                    break;
            }
        };

        holder.getBinding().userInviteIcon.setOnClickListener(onClickListener);
        holder.getBinding().dragIcon.setOnClickListener(onClickListener);


        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (userList.get(fromPosition).getViewType() == 0) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(userList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(userList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(CreateGameViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(CreateGameViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setUserList(ArrayList<UserModel> clubList) {
//        if (userList.isEmpty()) {
        userList.addAll(clubList);
        notifyDataSetChanged();
//        } else {
//            DiffUtil.DiffResult result = getDiffUtil(this.userList, clubList);
//            this.userList = clubList;
//            result.dispatchUpdatesTo(this);
//        }

    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<UserModel> oldList,
                                           ArrayList<UserModel> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                UserModel oldModel = oldList.get(oldItemPosition);
                UserModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getUserId(), newModel.getUserId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                UserModel oldModel = oldList.get(oldItemPosition);
                UserModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getUserId(), newModel.getUserId());
            }
        });
        return diffResult;
    }

    public MutableLiveData<Integer> getCheckCountLiveData() {
        return checkCountLiveData;
    }

    private Pair<Integer, Boolean> checkTeamSelectAll(int position) {
        UserModel userModel;
        int headerIndex = 0;
        boolean checkAll = true;
        for (int i = position - 1; i >= 0; i--) { // 선택한 아이템 위쪽 검사
            userModel = userList.get(i);
            if (userModel.getViewType() == 0) {
                headerIndex = i;
                break;
            } else if (!userModel.isCheck()) {
                checkAll = false;
                break;

            }
        }

        if (!userList.get(position).isCheck()) {
            return new Pair(headerIndex, false);
        }

        if (checkAll) {
            for (int i = position + 1; i < userList.size(); i++) { // 선택한 아이템 아래쪽 검사
                userModel = userList.get(i);
                if (userModel.getViewType() == 0) {
                    break;
                } else if (!userModel.isCheck()) {
                    checkAll = false;
                    break;

                }
            }
        }
        return new Pair(headerIndex, checkAll);
    }

    public void addTeam(UserModel userModel) {
        teamCount++;
        userList.add(userModel);
        if(teamCount == 1){
            userList.add(new UserModel(LoginManager.getInstance().getLoginInfoModel()));
            notifyDataSetChanged();
        } else {
            notifyItemInserted(userList.size() - 1);
        }

    }
}
