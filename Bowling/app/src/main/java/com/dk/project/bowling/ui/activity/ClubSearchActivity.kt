package com.dk.project.bowling.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubSearchBinding
import com.dk.project.bowling.viewModel.SearchClubViewModel
import com.dk.project.post.base.BindActivity

class ClubSearchActivity : BindActivity<ActivityClubSearchBinding, SearchClubViewModel>() {

    override fun getLayoutId() = R.layout.activity_club_search

    override fun getViewModel() =
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            SearchClubViewModel::class.java)

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        toolbarTitle.text = "클럽 검색하기"

    }


}
