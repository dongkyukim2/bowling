package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubSearchBinding
import com.dk.project.bowling.ui.adapter.callback.ClubSearchAdapter
import com.dk.project.bowling.viewModel.SearchClubViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.utils.RecyclerViewClickListener
import com.dk.project.post.utils.TextViewUtil

class ClubSearchActivity : BindActivity<ActivityClubSearchBinding, SearchClubViewModel>() {

    private var searchClubAdapter = ClubSearchAdapter()

    override fun getLayoutId() = R.layout.activity_club_search

    override fun getViewModel() =
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            SearchClubViewModel::class.java
        )

    override fun subscribeToModel() {
        viewModel.searchLiveData.observe(this, Observer {
            searchClubAdapter.setClubList(it, 0)
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel


        binding.searchRecycler.apply {
            layoutManager = LinearLayoutManager(this@ClubSearchActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = searchClubAdapter
            RecyclerViewClickListener.setItemClickListener(this) { _, position ->
                val intent = Intent(this@ClubSearchActivity, ClubDetailActivity::class.java)
                intent.putExtra(Define.CLUB_MODEL, searchClubAdapter.getClubModel(position))
                startActivity(intent)
            }
        }

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchWord = v.text.toString().trim();
                if (searchWord.length < 4) {
                    Toast.makeText(this, "세글자 이상 검색하세요", Toast.LENGTH_SHORT).show()
                    binding.searchEditText.postDelayed(
                        { binding.searchEditText.requestFocus() },
                        500
                    )
                } else {
                    TextViewUtil.hideKeyBoard(this, binding.searchEditText)
                    viewModel.requestSearchClub(v.text.toString().trim(), 0)
                    v.text = ""
                }
                true
            }
            false
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        TextViewUtil.showKeyBoard(this, binding.searchEditText)
    }
}