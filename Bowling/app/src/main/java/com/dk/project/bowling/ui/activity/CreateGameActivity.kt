package com.dk.project.bowling.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
import com.dk.project.bowling.ui.adapter.CreateGameAdapter
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback
import com.dk.project.bowling.viewModel.CreateGameViewModel
import com.dk.project.post.base.BindActivity


class CreateGameActivity : BindActivity<ActivityCreateGameBinding, CreateGameViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_create_game
    }

    override fun getViewModel(): CreateGameViewModel {
        return ViewModelProviders.of(this).get(CreateGameViewModel::class.java)
    }

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.createGameRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CreateGameActivity)
            CreateGameAdapter().let {
                ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                adapter = it
            }
        }
    }

}
