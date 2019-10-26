package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubUserListBinding
import com.dk.project.bowling.ui.adapter.ClubUserListAdapter
import com.dk.project.bowling.viewModel.ClubUserListViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.utils.RecyclerViewClickListener
import java.util.*


class ClubUserListActivity : BindActivity<ActivityClubUserListBinding, ClubUserListViewModel>() {

    lateinit var clubUserListAdapter: ClubUserListAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_club_user_list
    }

    override fun getViewModel(): ClubUserListViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubUserListViewModel::class.java
        )
    }

    override fun subscribeToModel() {
        viewModel.userListLiveData.observe(this, Observer {
            val joinWaitList = ArrayList<ScoreClubUserModel>()
            for (userModel in it) {
                if (userModel.type == Define.USER_TYPE_JOIN_WAIT) {
                    joinWaitList.add(userModel)
                }
            }
            it.removeAll(joinWaitList)
            clubUserListAdapter.setClubUserList(Pair(joinWaitList, it))
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isSelectMode) {
            toolbarTitle.text = "초대하기"
            toolbarRightButton.visibility = View.VISIBLE
        } else {
            toolbarTitle.text = "클럽인원 목록"
        }

        binding.clubUserRecycler.apply {
            clubUserListAdapter = ClubUserListAdapter()
            if (viewModel.isSelectMode) {
                clubUserListAdapter.setSelectedUserMap(viewModel.selectedUserMap)
            }
            clubUserListAdapter.setSelectMode(viewModel.isSelectMode)
            clubUserListAdapter.setClubUserListViewModel(viewModel)
            layoutManager = LinearLayoutManager(this@ClubUserListActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = clubUserListAdapter
            RecyclerViewClickListener.setItemClickListener(this) { view, position ->
                if (viewModel.isSelectMode) {
                    clubUserListAdapter.setCheck(position)
                } else {

                }

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
