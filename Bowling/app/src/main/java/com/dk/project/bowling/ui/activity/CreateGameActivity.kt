package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
import com.dk.project.bowling.shareData.ShareData
import com.dk.project.bowling.ui.adapter.CreateGameAdapter
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback
import com.dk.project.bowling.viewModel.CreateGameViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.GameModel
import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.utils.AlertDialogUtil
import com.dk.project.post.utils.RxBus

class CreateGameActivity : BindActivity<ActivityCreateGameBinding, CreateGameViewModel>() {

    private lateinit var createGameAdapter: CreateGameAdapter
    private var deleteMode = false


    override fun getLayoutId() = R.layout.activity_create_game

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(CreateGameViewModel::class.java)

    override fun subscribeToModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var teamTitleClickListener: View.OnClickListener = View.OnClickListener {
            it as EditText
            var title = it.text.toString().trim()
            createGameAdapter.addTeam(ScoreClubUserModel(title))
        }


        when (viewModel.adapterMode) {
            Define.MODEFY_MODE -> {
                toolbarTitle.text = "경기 수정하기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleEditText.visibility = View.VISIBLE
                binding.gameTitleEditText.setText(viewModel.gameName)

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)

                    CreateGameAdapter(this, viewModel.clubModel).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                        it.setModifyUserList(viewModel.gameScoreList)
                    }
                }

                binding.addTeam.setOnClickListener { showDialog(teamTitleClickListener) }

            }
            Define.CREATE_MODE -> {
                toolbarTitle.text = "경기 만들기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleEditText.visibility = View.VISIBLE

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)
                    CreateGameAdapter(this, viewModel.clubModel).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                    }
                }

                binding.addTeam.setOnClickListener { showDialog(teamTitleClickListener) }
                showDialog(teamTitleClickListener)
            }

        }
    }

    private fun showDialog(teamTitleClickListener: View.OnClickListener) {
        AlertDialogUtil.showEditTextAlertDialog(
            this,
            "팀 추가",
            "팀명을 입력해주세요.",
            teamTitleClickListener
        )
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        when (viewModel.adapterMode) {
            Define.CREATE_MODE -> {
                createGameAdapter.checkCountLiveData.observe(this, Observer {
                    deleteMode = it != 0
                    if (deleteMode) {
                        toolbarRightButton.setImageResource(R.drawable.ic_action_delete)
                    } else {
                        toolbarRightButton.setImageResource(R.drawable.ic_done)
                    }
                })
            }

        }
    }

    override fun onToolbarRightClick() {
        when (viewModel.adapterMode) {
            Define.CREATE_MODE -> {
                GameModel().apply {
                    clubId = viewModel.clubModel.clubId
                    val title = binding.gameTitleEditText.text?.trim()
                    gameName = if (TextUtils.isEmpty(title)) {
                        "임시 게임 이름"
                    } else {
                        title.toString()
                    }
                    userList = createGameAdapter.userList
                    BowlingApi.getInstance().setGameAndScoreList(this, {
                        finish()
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_REFRESH_CLUB_GAME_LIST, clubId))
                    }, {
                        Toast.makeText(this@CreateGameActivity, "게임 등록 오류!!!", Toast.LENGTH_SHORT)
                            .show()
                    })
                }
            }
            Define.MODEFY_MODE -> {
                GameModel().apply {
                    clubId = viewModel.clubModel.clubId
                    gameId = viewModel.gameId

                    val title = binding.gameTitleEditText.text?.trim()
                    gameName = if (TextUtils.isEmpty(title)) {
                        viewModel.gameName
                    } else {
                        title.toString()
                    }
                    userList = createGameAdapter.userList

                    BowlingApi.getInstance().setGameAndScoreList(this, {
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_CLOSE_CREATE_MODIFY_GAME_ACTIVITY, null))
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_REFRESH_CLUB_GAME_LIST, clubId))
                        finish()
                    }, {
                        Toast.makeText(this@CreateGameActivity, "게임 수정 오류!!!", Toast.LENGTH_SHORT)
                            .show()
                    })
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            Define.CLUB_USER_LIST -> {
                // todo parcelable로 넘기면 깨짐
                ShareData.getInstance().inviteUserList.map { it.isCheck = false }
                createGameAdapter.setInviteUserList(ShareData.getInstance().inviteUserList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.adapterMode == Define.MODEFY_MODE) {
            ShareData.getInstance().scoreList.clear()
        }
    }
}
