package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubUserListBinding
import com.dk.project.bowling.ui.adapter.ClubUserListAdapter
import com.dk.project.bowling.viewModel.ClubUserListViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.utils.RecyclerViewClickListener


class ClubUserListActivity : BindActivity<ActivityClubUserListBinding, ClubUserListViewModel>() {

    lateinit var clubUserListAdapter: ClubUserListAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_club_user_list
    }

    override fun getViewModel(): ClubUserListViewModel {
        return ViewModelProviders.of(this).get(ClubUserListViewModel::class.java)
    }

    override fun subscribeToModel() {
        viewModel.userListLiveData.observe(this, Observer {
            clubUserListAdapter.setClubUserList(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarTitle.text = "초대하기"
        toolbarRightButton.visibility = View.VISIBLE


        binding.clubUserRecycler.apply {
            clubUserListAdapter = ClubUserListAdapter()
            clubUserListAdapter.setSelectedUserMap(viewModel.selectedUserMap)
            layoutManager = LinearLayoutManager(this@ClubUserListActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = clubUserListAdapter
            RecyclerViewClickListener.setItemClickListener(this) { view, position ->
                view.visibility
                clubUserListAdapter.setCheck(position)
            }
        }
    }

    override fun onToolbarRightClick() {
        Intent().let {
            it.putParcelableArrayListExtra(CLUB_USER_LIST_MODEL, clubUserListAdapter.selectList)
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }
}