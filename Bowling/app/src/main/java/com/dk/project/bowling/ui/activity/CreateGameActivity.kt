package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
import com.dk.project.bowling.ui.adapter.CreateGameAdapter
import com.dk.project.bowling.ui.adapter.ReadGameAdapter
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback
import com.dk.project.bowling.viewModel.CreateGameViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.GameModel
import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.retrofit.ErrorCallback
import com.dk.project.post.retrofit.SuccessCallback
import com.dk.project.post.utils.AlertDialogUtil

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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (viewModel.isReadMode) {
            toolbarTitle.text = viewModel.readGameModel.gameName
            toolbarRightButton.visibility = View.GONE
            binding.addTeam.visibility = View.GONE
        } else {
            toolbarTitle.text = "경기 만들기"
            toolbarRightButton.visibility = View.VISIBLE

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
                AlertDialogUtil.showEditTextAlertDialog(this, "팀 추가","팀명을 입력해주세요.", teamTitleClickListener)
            }
            AlertDialogUtil.showEditTextAlertDialog(this, "팀 추가","팀명을 입력해주세요.", teamTitleClickListener)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (viewModel.isReadMode) {


        } else {
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

    override fun onToolbarRightClick() {

        GameModel().apply {
            clubId = viewModel.clubModel.clubId
            gameName = "임시 게임 이름"
            userList = createGameAdapter.userList
            BowlingApi.getInstance().setGameAndScoreList(this, SuccessCallback {
                setResult(Activity.RESULT_OK)
                finish()
            }, ErrorCallback {
                Toast.makeText(this@CreateGameActivity, "게임 등록 오류!!!", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {

            Define.CLUB_USER_LIST -> {
                data?.getParcelableArrayListExtra<ScoreClubUserModel>(Define.CLUB_USER_LIST_MODEL)
                    ?.let {
                        it.map { it.isCheck = false }
                        createGameAdapter.setInviteUserList(it)
                    }
            }
        }
    }
}
