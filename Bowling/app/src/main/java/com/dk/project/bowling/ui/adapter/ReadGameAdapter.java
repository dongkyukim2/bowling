package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.GameScoreModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.model.LoginInfoModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> {

    private LayoutInflater layoutInflater;

    private ArrayList<ScoreClubUserModel> scoreUserList;

    private int prevTeamPos = -1;
    private int prevUserPos = -1;


    public ReadGameAdapter(Context context, ArrayList<LoginInfoModel> userList, ReadGameModel readGameModel) {

        layoutInflater = LayoutInflater.from(context);
        scoreUserList = new ArrayList<>();

        HashMap<String, LoginInfoModel> userMap = new HashMap<>();

        for (LoginInfoModel model : userList) {
            userMap.put(model.getUserId(), model);
        }

        ScoreClubUserModel readUserModel = null;
        for (GameScoreModel gameScoreModel : readGameModel.getScoreList()) {

            int teamPosition = gameScoreModel.getTeamPosition();
            String teamName = gameScoreModel.getTeamName();
            int userPosition = gameScoreModel.getUserPosition();
            String userId = gameScoreModel.getUserId();
            int scorePosition = gameScoreModel.getScorePosition();
            int score = gameScoreModel.getScore();

            if (teamPosition == prevTeamPos) {

                if (userPosition == prevUserPos) {
                    readUserModel.setScore(scorePosition, score);
                } else {
                    readUserModel = new ScoreClubUserModel(teamName);
                    readUserModel.setViewType(1);
                    readUserModel.setUserId(userId);
                    readUserModel.setUserName(userMap.get(gameScoreModel.getUserId()).getUserName());
                    readUserModel.setScore(scorePosition, score);
                    scoreUserList.add(readUserModel);

                }
            } else {
                readUserModel = new ScoreClubUserModel(teamName);
                scoreUserList.add(readUserModel);

                readUserModel = new ScoreClubUserModel(teamName);
                readUserModel.setViewType(1);
                readUserModel.setUserId(userId);
                readUserModel.setUserName(userMap.get(gameScoreModel.getUserId()).getUserName());
                readUserModel.setScore(scorePosition, score);
                scoreUserList.add(readUserModel);
            }

            prevUserPos = userPosition;
            prevTeamPos = teamPosition;
        }
    }

    @Override
    public CreateGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGameViewHolder(layoutInflater.inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(CreateGameViewHolder holder, int position) {
        holder.onBindView(scoreUserList.get(position), position);

        holder.getBinding().userCheckBox.setVisibility(View.GONE);
        holder.getBinding().userCheckBoxSpace.setVisibility(View.GONE);
        holder.getBinding().userInviteIcon.setVisibility(View.GONE);
        holder.getBinding().dragIcon.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return scoreUserList.size();
    }

    public ArrayList<ScoreClubUserModel> getScoreUserList() {
        return scoreUserList;
    }
}