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
import com.dk.project.bowling.ui.adapter.ReadGameAdapter
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
    private lateinit var readGameAdapter: ReadGameAdapter


    private var deleteMode = false


    override fun getLayoutId() = R.layout.activity_create_game

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(CreateGameViewModel::class.java)

    override fun subscribeToModel() {
        viewModel.gameUserLiveData.observe(this, Observer {

            binding.createGameRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@CreateGameActivity)
                ReadGameAdapter(this@CreateGameActivity, it, viewModel.readGameModel).let {
                    adapter = it
                    readGameAdapter = it
                }
            }
        })

        RxBus.getInstance().registerRxObserver {
            when (it.first) {
                Define.EVENT_CLOSE_CREATE_READ_GAME_ACTIVITY -> finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (viewModel.adapterMode) {
            Define.READ_MODE -> {
//            toolbarTitle.text = viewModel.readGameModel.gameName
                toolbarRightButton.visibility = View.VISIBLE
                toolbarRightButton.setImageResource(R.drawable.ic_create)
                binding.addTeam.visibility = View.GONE
                binding.gameTitleTextView.visibility = View.VISIBLE
                binding.gameTitleTextView.text = viewModel.readGameModel.gameName
                binding.gameTitleTextView.isSelected = true
                binding.gameTitleEditText.visibility = View.INVISIBLE
            }
            Define.MODEFY_MODE -> {
                toolbarTitle.text = "경기 수정하기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleTextView.visibility = View.GONE
                binding.gameTitleEditText.visibility = View.VISIBLE
                binding.gameTitleEditText.setText(viewModel.readGameModel.gameName)

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)

                    CreateGameAdapter(this, viewModel.readGameModel).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                        it.setmodifyUserList(viewModel.gameScoreList)
                    }
                }

                var teamTitleClickListener: View.OnClickListener = View.OnClickListener {
                    it as EditText
                    var title = it.text.toString().trim()
                    createGameAdapter.addTeam(ScoreClubUserModel(title))
                }

                binding.addTeam.setOnClickListener {
                    AlertDialogUtil.showEditTextAlertDialog(
                        this,
                        "팀 추가",
                        "팀명을 입력해주세요.",
                        teamTitleClickListener
                    )
                }

            }
            Define.CREATE_MODE -> {
                toolbarTitle.text = "경기 만들기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleTextView.visibility = View.GONE
                binding.gameTitleEditText.visibility = View.VISIBLE

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)
                    CreateGameAdapter(this, viewModel.clubModel).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                    }
                }

                var teamTitleClickListener: View.OnClickListener = View.OnClickListener {
                    it as EditText
                    var title = it.text.toString().trim()
                    createGameAdapter.addTeam(ScoreClubUserModel(title))
                }

                binding.addTeam.setOnClickListener {
                    AlertDialogUtil.showEditTextAlertDialog(
                        this,
                        "팀 추가",
                        "팀명을 입력해주세요.",
                        teamTitleClickListener
                    )
                }
                AlertDialogUtil.showEditTextAlertDialog(
                    this,
                    "팀 추가",
                    "팀명을 입력해주세요.",
                    teamTitleClickListener
                )
            }

        }
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
            Define.READ_MODE -> {
                readGameAdapter?.let {
                    val intent = Intent(this, CreateGameActivity::class.java)
                    intent.putExtra(Define.READ_GAME_MODEL, viewModel.readGameModel)
                    ShareData.getInstance().scoreList.clear()
                    ShareData.getInstance().scoreList.addAll(it.scoreUserList)

                    startActivity(intent)
                }
            }
            Define.MODEFY_MODE -> {

                GameModel().apply {
                    clubId = viewModel.readGameModel.clubId
                    gameId = viewModel.readGameModel.gameId

                    val title = binding.gameTitleEditText.text?.trim()
                    gameName = if (TextUtils.isEmpty(title)) {
                        viewModel.readGameModel.gameName
                    } else {
                        title.toString()
                    }

                    userList = createGameAdapter.userList

                    BowlingApi.getInstance().setGameAndScoreList(this, {

                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_CLOSE_CREATE_READ_GAME_ACTIVITY, null))
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_REFRESH_CLUB_GAME_LIST, clubId))
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
