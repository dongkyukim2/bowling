package com.dk.project.bowling.ui.activity

import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityScoreListBinding
import com.dk.project.bowling.viewModel.ScoreListActivityViewModel
import com.dk.project.post.base.BindActivity

class ScoreListActivity : BindActivity<ActivityScoreListBinding, ScoreListActivityViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_score_list
    }

    override fun getViewModel(): ScoreListActivityViewModel {
        return ViewModelProviders.of(this).get(ScoreListActivityViewModel::class.java)
    }

    override fun subscribeToModel() {

    }
}
