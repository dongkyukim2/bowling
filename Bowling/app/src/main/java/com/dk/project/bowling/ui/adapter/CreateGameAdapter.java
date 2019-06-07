package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
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
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class CreateGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private HashMap<String, Boolean> userMap = new HashMap<>();

    private ArrayList<UserModel> userList = new ArrayList<UserModel>() {
        @Override
        public boolean add(UserModel userModel) {
            if (userModel.getViewType() == 1) {
                userMap.put(userModel.getUserId(), true);
            }
            return super.add(userModel);
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends UserModel> c) {
            Observable.fromIterable(c).filter(o -> o.getViewType() == 1).subscribe(userModel -> userMap.put(userModel.getUserId(), true)).dispose();
            return super.addAll(index, c);
        }
    };
    private MutableLiveData<Integer> checkCountLiveData = new MutableLiveData<>();

    private RecyclerView recyclerView;
    private Context mContext;
    private ClubModel clubModel;
    private int teamCount;
    private int selectInviteIndex;

    public CreateGameAdapter(RecyclerView recyclerView, ClubModel clubModel) {
        this.recyclerView = recyclerView;
        mContext = recyclerView.getContext();
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


//        System.out.println("==============   onBindViewHolder   " + position);
        holder.getBinding().userInviteIcon.setTag(position);
        holder.itemView.setTag(position);

        View.OnClickListener onClickListener = v -> {

            int index = (int) v.getTag();

//            System.out.println("==============   onclick   " + index);

            switch (v.getId()) {
                case R.id.user_invite_icon:
                    selectInviteIndex = index;
                    Intent intent = new Intent(mContext, ClubUserListActivity.class);
                    intent.putExtra(SELECTED_USER_MAP, userMap);
                    intent.putExtra(CLUB_MODEL, clubModel);

                    ((BindActivity) mContext).startActivityForResult(intent, Define.CLUB_USER_LIST);
                    break;
                case R.id.drag_icon:

                    break;
                default:
                    setCheckBox(index);
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


//        System.out.println("==============   onRowMoved   fromPosition   " + fromPosition + "    toPosition   " + toPosition);


        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(userList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(userList, i, i - 1);
            }
        }


        CreateGameViewHolder from = (CreateGameViewHolder) recyclerView.findViewHolderForAdapterPosition(fromPosition);
        CreateGameViewHolder to = (CreateGameViewHolder) recyclerView.findViewHolderForAdapterPosition(toPosition);


//        System.out.println("==============   onRowMoved  00000  fromPosition   " + from.itemView.getTag() + "    toPosition   " + to.itemView.getTag());

        from.itemView.setTag(toPosition);
        from.getBinding().userInviteIcon.setTag(toPosition);

        to.itemView.setTag(fromPosition);
        to.getBinding().userInviteIcon.setTag(fromPosition);

//        System.out.println("==============   onRowMoved  11111  fromPosition   " + from.itemView.getTag() + "    toPosition   " + to.itemView.getTag());


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
    public void onDragStart() {
        if (checkCountLiveData.getValue() != null && checkCountLiveData.getValue() > 0) {

            Observable.fromIterable(userList).subscribe(userModel -> userModel.setCheck(false), throwable -> {
            }, () -> {
                notifyDataSetChanged();
                checkCountLiveData.setValue(0);
            }).dispose();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setInviteUserList(ArrayList<UserModel> clubList) {
//        if (userList.isEmpty()) {
        userList.addAll(selectInviteIndex + 1, clubList);
        notifyDataSetChanged();
//        } else {
//            DiffUtil.DiffResult result = getDiffUtil(this.userList, clubList);
//            this.userList = clubList;
//            result.dispatchUpdatesTo(this);
//        }

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
        if (teamCount == 1) {
            userList.add(new UserModel(LoginManager.getInstance().getLoginInfoModel()));
            notifyDataSetChanged();
        } else {
//            notifyItemInserted(userList.size() - 1);
            notifyDataSetChanged();
        }

    }

    public void setCheckBox(int position) {
        UserModel userModel = userList.get(position);
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
//            notifyDataSetChanged();
        } else {
            userModel.setCheck(check);

            Pair<Integer, Boolean> headerIndex = checkTeamSelectAll(position);

            userList.get(headerIndex.first).setCheck(headerIndex.second);
            notifyItemChanged(headerIndex.first);
            notifyItemChanged(position);
//            notifyDataSetChanged();
        }


        int checkUserCount = 0;
        for (UserModel tempUserModel : userList) {
            if (tempUserModel.isCheck()) {
                checkUserCount++;
            }
        }
        checkCountLiveData.setValue(checkUserCount);
    }

    public int getSelectInviteIndex() {
        return selectInviteIndex;
    }
}
