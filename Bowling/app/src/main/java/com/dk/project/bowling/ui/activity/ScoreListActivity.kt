package com.dk.project.bowling.ui.activity

import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityScoreListBinding
import com.dk.project.bowling.viewModel.ScoreListViewModel
import com.dk.project.post.base.BindActivity

class ScoreListActivity : BindActivity<ActivityScoreListBinding, ScoreListViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_score_list
    }

    override fun getViewModel(): ScoreListViewModel {
        return ViewModelProviders.of(this).get(ScoreListViewModel::class.java)
    }

    override fun subscribeToModel() {

    }
}
