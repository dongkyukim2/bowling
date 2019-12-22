package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.ClubUserListActivity;
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;
import com.dk.project.bowling.viewModel.impl.CreateGameListener;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AlertDialogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;

public class CreateGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private LayoutInflater layoutInflater;

    private HashMap<String, Boolean> userMap = new HashMap<>();
    private ArrayList<ScoreClubUserModel> userList = new ArrayList<ScoreClubUserModel>() {
        @Override
        public boolean add(ScoreClubUserModel userModel) {
            if (userModel.isUserType()) {
                userMap.put(userModel.getUserId(), true);
            }
            return super.add(userModel);
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends ScoreClubUserModel> c) {
            Observable.fromIterable(c).
                    filter(o -> o.isUserType()).
                    subscribe(userModel -> userMap.put(userModel.getUserId(), true)).
                    dispose();
            return super.addAll(index, c);
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            Observable.fromIterable(c).
                    filter(o -> ((ScoreClubUserModel) o).isUserType()).
                    subscribe(userModel -> userMap.remove(((ScoreClubUserModel) userModel).getUserId())).
                    dispose();
            return super.removeAll(c);
        }
    };

    private RecyclerView recyclerView;
    private Context mContext;
    private ClubModel clubModel;
    private int teamCount;
    private int selectInviteIndex;
    private CreateGameListener createGameListener;
    private boolean deleteMode;

    public CreateGameAdapter(RecyclerView recyclerView, ClubModel clubModel, CreateGameListener createGameListener) {
        this.recyclerView = recyclerView;
        mContext = recyclerView.getContext();
        this.clubModel = clubModel;
        layoutInflater = LayoutInflater.from(mContext);
        this.createGameListener = createGameListener;
    }

    @Override
    public CreateGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGameViewHolder(layoutInflater.inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(CreateGameViewHolder holder, int position) {
        ScoreClubUserModel userModel = userList.get(position);
        holder.onBindView(userModel, position);

        holder.getBinding().userInviteIcon.setTag(position);
        holder.getBinding().dragIcon.setTag(position);
        holder.itemView.setTag(position);

        holder.getBinding().gameNum1.setTag(position);
        holder.getBinding().gameNumScore1.setTag(position);
        holder.getBinding().gameNum2.setTag(position);
        holder.getBinding().gameNumScore2.setTag(position);
        holder.getBinding().gameNum3.setTag(position);
        holder.getBinding().gameNumScore3.setTag(position);
        holder.getBinding().gameNum4.setTag(position);
        holder.getBinding().gameNumScore4.setTag(position);
        holder.getBinding().gameNum5.setTag(position);
        holder.getBinding().gameNumScore5.setTag(position);
        holder.getBinding().gameNum6.setTag(position);
        holder.getBinding().gameNumScore6.setTag(position);

        View.OnClickListener onClickListener = v -> {

            int index = (int) v.getTag();
            AtomicInteger scoreIndex = new AtomicInteger(0);

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
                case R.id.game_num_6:
                case R.id.game_num_score_6:
                    scoreIndex.incrementAndGet();
                case R.id.game_num_5:
                case R.id.game_num_score_5:
                    scoreIndex.incrementAndGet();
                case R.id.game_num_4:
                case R.id.game_num_score_4:
                    scoreIndex.incrementAndGet();
                case R.id.game_num_3:
                case R.id.game_num_score_3:
                    scoreIndex.incrementAndGet();
                case R.id.game_num_2:
                case R.id.game_num_score_2:
                    scoreIndex.incrementAndGet();
                case R.id.game_num_1:
                case R.id.game_num_score_1:
                    AlertDialogUtil.showScoreEditTextAlertDialog(mContext, "점수를 입력해주세요", editText -> {
                        setScore(userList.get(index), scoreIndex.get(), Integer.valueOf(((EditText) editText).getText().toString().trim()));
                        notifyItemChanged(index);
                    });
                    break;
                default:
                    setCheckBox(index);
                    break;
            }
        };

        holder.getBinding().gameNum1.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore1.setOnClickListener(onClickListener);
        holder.getBinding().gameNum2.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore2.setOnClickListener(onClickListener);
        holder.getBinding().gameNum3.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore3.setOnClickListener(onClickListener);
        holder.getBinding().gameNum4.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore4.setOnClickListener(onClickListener);
        holder.getBinding().gameNum5.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore5.setOnClickListener(onClickListener);
        holder.getBinding().gameNum6.setOnClickListener(onClickListener);
        holder.getBinding().gameNumScore6.setOnClickListener(onClickListener);

        holder.getBinding().userInviteIcon.setOnClickListener(onClickListener);
        holder.getBinding().dragIcon.setOnClickListener(onClickListener);
        holder.itemView.setOnClickListener(onClickListener);

    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (!userList.get(fromPosition).isUserType()) {
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

        CreateGameViewHolder from = (CreateGameViewHolder) recyclerView.findViewHolderForAdapterPosition(fromPosition);
        CreateGameViewHolder to = (CreateGameViewHolder) recyclerView.findViewHolderForAdapterPosition(toPosition);


        from.itemView.setTag(toPosition);
        from.getBinding().userInviteIcon.setTag(toPosition);

        from.getBinding().gameNum1.setTag(toPosition);
        from.getBinding().gameNumScore1.setTag(toPosition);
        from.getBinding().gameNum2.setTag(toPosition);
        from.getBinding().gameNumScore2.setTag(toPosition);
        from.getBinding().gameNum3.setTag(toPosition);
        from.getBinding().gameNumScore3.setTag(toPosition);
        from.getBinding().gameNum4.setTag(toPosition);
        from.getBinding().gameNumScore4.setTag(toPosition);
        from.getBinding().gameNum5.setTag(toPosition);
        from.getBinding().gameNumScore5.setTag(toPosition);
        from.getBinding().gameNum6.setTag(toPosition);
        from.getBinding().gameNumScore6.setTag(toPosition);

        to.itemView.setTag(fromPosition);
        to.getBinding().userInviteIcon.setTag(fromPosition);

        to.getBinding().gameNum1.setTag(fromPosition);
        to.getBinding().gameNumScore1.setTag(fromPosition);
        to.getBinding().gameNum2.setTag(fromPosition);
        to.getBinding().gameNumScore2.setTag(fromPosition);
        to.getBinding().gameNum3.setTag(fromPosition);
        to.getBinding().gameNumScore3.setTag(fromPosition);
        to.getBinding().gameNum4.setTag(fromPosition);
        to.getBinding().gameNumScore4.setTag(fromPosition);
        to.getBinding().gameNum5.setTag(fromPosition);
        to.getBinding().gameNumScore5.setTag(fromPosition);
        to.getBinding().gameNum6.setTag(fromPosition);
        to.getBinding().gameNumScore6.setTag(fromPosition);

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
        if (deleteMode) {
            Observable.fromIterable(userList).subscribe(userModel -> userModel.setCheck(false), throwable -> {
            }, () -> {
                notifyDataSetChanged();
                deleteMode = false;
                createGameListener.OnCheckedChange(0);
            }).dispose();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setInviteUserList(ArrayList<ScoreClubUserModel> clubList) {
        userList.addAll(selectInviteIndex + 1, clubList);
        notifyDataSetChanged();
    }

    public void setModifyUserList(ArrayList<ScoreClubUserModel> clubList) {
        for (ScoreClubUserModel clubUserModel : clubList) {
            if (!clubUserModel.isUserType()) {
                teamCount++;
            }
        }
        userList.addAll(0, clubList);
        notifyDataSetChanged();
    }

    private Pair<Integer, Boolean> checkTeamSelectAll(int position) {
        ScoreClubUserModel userModel;
        int headerIndex = 0;
        boolean checkAll = true;
        for (int i = position - 1; i >= 0; i--) { // 선택한 아이템 위쪽 검사
            userModel = userList.get(i);
            if (!userModel.isUserType()) {
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
                if (!userModel.isUserType()) {
                    break;
                } else if (!userModel.isCheck()) {
                    checkAll = false;
                    break;

                }
            }
        }
        return new Pair(headerIndex, checkAll);
    }

    public void addTeam(ScoreClubUserModel userModel) {
        teamCount++;
        userList.add(userModel);
        if (teamCount == 1) {
            userList.add(new ScoreClubUserModel(LoginManager.getInstance().getLoginInfoModel()));
            notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }

    }

    public void setCheckBox(int position) {
        ScoreClubUserModel userModel = userList.get(position);
        boolean check = !userModel.isCheck();
        if (!userModel.isUserType()) {
            userModel.setCheck(check);
            int startIndex = position;
            int count = 1;

            for (int i = position + 1; i < userList.size(); i++) {
                if (!userList.get(i).isUserType()) {
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
        for (ScoreClubUserModel tempUserModel : userList) {
            if (tempUserModel.isCheck()) {
                checkUserCount++;
            }
        }
        deleteMode = checkUserCount > 0;
        createGameListener.OnCheckedChange(checkUserCount);
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public ArrayList<ScoreClubUserModel> getUserList() {
        return userList;
    }

    private void setScore(ScoreClubUserModel userModel, int index, int score) {
        userModel.setScore(index, score);
    }

    public void deleteSelectUser() {
        ArrayList<ScoreClubUserModel> removeList = new ArrayList<>();
        for (ScoreClubUserModel userModel : userList) {
            if (userModel.isCheck()) {
                removeList.add(userModel);
            }
        }
        userList.removeAll(removeList);
        notifyDataSetChanged();
        deleteMode = false;
        createGameListener.OnCheckedChange(0);
    }
}