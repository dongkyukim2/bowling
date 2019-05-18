package com.dk.project.bowling.ui.fragment


import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentScoreListBinding
import com.dk.project.bowling.viewModel.ScoreListViewModel
import com.dk.project.post.base.BindFragment


/**
 * A simple [Fragment] subclass.
 *
 */
class ScoreListFragment : BindFragment<FragmentScoreListBinding, ScoreListViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_score_list
    }

    override fun createViewModel(): ScoreListViewModel {
        return ViewModelProviders.of(this).get(ScoreListViewModel::class.java)
    }

    override fun registerLiveData() {

    }
}
