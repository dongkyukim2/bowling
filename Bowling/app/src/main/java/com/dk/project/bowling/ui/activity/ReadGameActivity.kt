package com.dk.project.bowling.ui.activity

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

class ReadGameActivity : BindActivity<ActivityReadGameBinding, ReadGameViewModel>() {

    private lateinit var readGameAdapter: ReadGameAdapter

    override fun getLayoutId() = R.layout.activity_read_game

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(ReadGameViewModel::class.java)

    override fun subscribeToModel() {

        // 유저목록 가져와서 게임 점수 목록 표시
        viewModel.gameUserLiveData.observe(this, Observer {
            binding.readGameRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@ReadGameActivity)
                ReadGameAdapter(this@ReadGameActivity, it, viewModel.readGameModel).let {
                    adapter = it
                    readGameAdapter = it
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarRightButton.visibility = View.VISIBLE
        toolbarRightButton.setImageResource(R.drawable.ic_create)

        toolbarTitle.text = viewModel.readGameModel.gameName
        toolbarTitle.isSelected = true
    }


    override fun onToolbarRightClick() {
        super.onToolbarRightClick()

        readGameAdapter?.let {
            val intent = Intent(this, CreateGameActivity::class.java)
            intent.putExtra(Define.READ_GAME_MODEL, viewModel.readGameModel)
            ShareData.getInstance().scoreList.clear()
            ShareData.getInstance().scoreList.addAll(it.scoreUserList)

            startActivity(intent)
        }
    }
}
