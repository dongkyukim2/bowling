package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
import com.dk.project.bowling.model.GameModel
import com.dk.project.bowling.model.ReadGameModel
import com.dk.project.bowling.model.UserModel
import com.dk.project.bowling.retrofit.BowlingApi
import com.dk.project.bowling.ui.adapter.CreateGameAdapter
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback
import com.dk.project.bowling.viewModel.CreateGameViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.retrofit.ErrorCallback
import com.dk.project.post.retrofit.SuccessCallback
import com.dk.project.post.utils.AlertDialogUtil

class CreateGameActivity : BindActivity<ActivityCreateGameBinding, CreateGameViewModel>() {

    private lateinit var createGameAdapter: CreateGameAdapter


    private var deleteMode = false


    override fun getLayoutId(): Int {
        return R.layout.activity_create_game
    }

    override fun getViewModel(): CreateGameViewModel {
        return ViewModelProviders.of(this).get(CreateGameViewModel::class.java)
    }

    override fun subscribeToModel() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (viewModel.isReadMode) {
            toolbarTitle.text = viewModel.readGameModel.gameName
            toolbarRightButton.visibility = View.GONE

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
                createGameAdapter.addTeam(UserModel(title))
            }

            binding.addTeam.setOnClickListener {
                AlertDialogUtil.showEditTextAlertDialog(this, "팀명을 입력해주세요.", teamTitleClickListener)
            }
            AlertDialogUtil.showEditTextAlertDialog(this, "팀명을 입력해주세요.", teamTitleClickListener)
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
}
