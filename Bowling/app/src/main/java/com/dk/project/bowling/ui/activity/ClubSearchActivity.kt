package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubSearchBinding
import com.dk.project.bowling.ui.adapter.callback.ClubSearchAdapter
import com.dk.project.bowling.viewModel.SearchClubViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.utils.RecyclerViewClickListener
import com.dk.project.post.utils.TextViewUtil

class ClubSearchActivity : BindActivity<ActivityClubSearchBinding, SearchClubViewModel>() {

    private var isLoading = false
    private var searchClubAdapter = ClubSearchAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var finalSearchWord: String

    override fun getLayoutId() = R.layout.activity_club_search

    override fun getViewModel() =
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            SearchClubViewModel::class.java
        )

    override fun subscribeToModel() {
        viewModel.searchLiveData.observe(this, Observer {
            searchClubAdapter.setClubList(it.first, it.second)
            if(it.second){
                linearLayoutManager.scrollToPosition(0)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel


        binding.searchRecycler.apply {
            linearLayoutManager = LinearLayoutManager(this@ClubSearchActivity)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchClubAdapter
            RecyclerViewClickListener.setItemClickListener(this) { _, position ->
                val intent = Intent(this@ClubSearchActivity, ClubDetailActivity::class.java)
                intent.putExtra(Define.CLUB_MODEL, searchClubAdapter.getClubModel(position))
                startActivity(intent)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && !isLoading && linearLayoutManager.itemCount - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                        isLoading = true
                        viewModel.requestSearchClub(finalSearchWord, linearLayoutManager.itemCount)
                    }
                }
            })
        }

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchWord = v.text.toString().trim()
                if (searchWord.length < 4) {
                    Toast.makeText(this, "네글자 이상 검색하세요", Toast.LENGTH_SHORT).show()
                    binding.searchEditText.postDelayed(
                        { binding.searchEditText.requestFocus() },
                        500
                    )
                } else {
                    isLoading = false
                    finalSearchWord = searchWord
                    TextViewUtil.hideKeyBoard(this, binding.searchEditText)
                    viewModel.requestSearchClub(finalSearchWord, 0)
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