package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.GameScoreModel;
import com.dk.project.bowling.model.ReadGameModel;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.model.LoginInfoModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<LoginInfoModel> userList;
//    private ReadGameModel readGameModel;

    private ArrayList<UserModel> scoreUserList;

    private int prevTeamPos = -1;
    private int prevUserPos = -1;
    private int prevScorePos = -1;


    public ReadGameAdapter(Context context, ArrayList<LoginInfoModel> userList, ReadGameModel readGameModel) {


        layoutInflater = LayoutInflater.from(context);
        this.userList = userList;
//        this.readGameModel = readGameModel;


        scoreUserList = new ArrayList<>();

        HashMap<String, String> userMap = new HashMap<>();

        for (LoginInfoModel model : userList) {
            userMap.put(model.getUserId(), model.getUserName());
        }

        UserModel readUserModel = null;
        for (GameScoreModel gameScoreModel : readGameModel.getScoreList()) {

            int teamPosition = gameScoreModel.getTeamPosition();
            String teamName = gameScoreModel.getTeamName();
            int userPosition = gameScoreModel.getUserPosition();
            String userId = gameScoreModel.getUserId();
            int scorePosition = gameScoreModel.getScorePosition();
            int score = gameScoreModel.getScore();
            System.out.println("+++++++++++     " + teamPosition + "    " + userPosition + "    " + scorePosition + "    ");


            if (teamPosition == prevTeamPos) {

                if (userPosition == prevUserPos) {
                    readUserModel.setScore(scorePosition, score);
                } else {
                    readUserModel = new UserModel(teamName);
                    readUserModel.setViewType(1);
                    readUserModel.setUserName(userMap.get(gameScoreModel.getUserId()));
                    readUserModel.setScore(scorePosition, score);
                    scoreUserList.add(readUserModel);
                    prevUserPos = userPosition;

                }
            } else {
                readUserModel = new UserModel(teamName);
                scoreUserList.add(readUserModel);
                prevTeamPos = teamPosition;
            }
        }
    }

    @Override
    public CreateGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGameViewHolder(layoutInflater.inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(CreateGameViewHolder holder, int position) {
        holder.onBindView(scoreUserList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return scoreUserList.size();
    }
}