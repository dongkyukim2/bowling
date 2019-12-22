package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityReadGameBinding
import com.dk.project.bowling.shareData.ShareData
import com.dk.project.bowling.ui.adapter.ReadGameAdapter
import com.dk.project.bowling.viewModel.ReadGameViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.utils.AlertDialogUtil
import com.dk.project.post.utils.RxBus

class ReadGameActivity : BindActivity<ActivityReadGameBinding, ReadGameViewModel>() {

    private lateinit var readGameAdapter: ReadGameAdapter

    override fun getLayoutId() = R.layout.activity_read_game

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(ReadGameViewModel::class.java)

    override fun subscribeToModel() {

        // 유저목록 가져와서 게임 점수 목록 표시
        viewModel.gameUserAndGameLiveData.observe(this, Observer {
            viewModel.readGameModel = it.second?.data
            binding.readGameRecyclerView.apply {
                toolbarTitle.text = viewModel.readGameModel.gameName
                layoutManager = LinearLayoutManager(this@ReadGameActivity)
                ReadGameAdapter(this@ReadGameActivity, it.first?.data, it.second?.data).let {
                    adapter = it
                    readGameAdapter = it
                }
            }
            binding.loading.visibility = View.GONE
        })

        RxBus.getInstance().registerRxObserver {
            when (it.first) {
                Define.EVENT_CLOSE_CREATE_MODIFY_GAME_ACTIVITY -> {
                    viewModel.requestGameModel()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarRightButton.visibility = View.VISIBLE
        toolbarRightButton.setImageResource(R.drawable.ic_more_grey)

        toolbarTitle.text = viewModel.readGameModel.gameName
        toolbarTitle.isSelected = true
    }


    override fun onToolbarRightClick() {
        super.onToolbarRightClick()
        AlertDialogUtil.showBottomSheetDialog(this) {
            if (it.id == R.id.btnModify) {
                readGameAdapter.let {
                    val intent = Intent(this, CreateGameActivity::class.java)
                    intent.putExtra(Define.READ_GAME_MODEL, viewModel.readGameModel)
                    ShareData.getInstance().scoreList.clear()
                    ShareData.getInstance().scoreList.addAll(it.scoreUserList)
                    startActivity(intent)
                }
            } else if (it.id == com.dk.project.post.R.id.btnDelete) {
                BowlingApi.getInstance().requestDeleteGame(viewModel.readGameModel.gameId, {
                    setResult(Activity.RESULT_OK)
                    finish()
                }, {

                })
            }
        }
    }
}
