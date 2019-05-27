package com.dk.project.bowling.ui.activity

import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
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

}
