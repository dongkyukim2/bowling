package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubUserListBinding
import com.dk.project.bowling.model.UserModel
import com.dk.project.bowling.ui.adapter.ClubUserListAdapter
import com.dk.project.bowling.viewModel.ClubUserListViewModel
import com.dk.project.post.base.BindActivity


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
        toolbarRightButton.visibility = View.VISIBLE;


        binding.clubUserRecycler.apply {
            clubUserListAdapter = ClubUserListAdapter()
            layoutManager = LinearLayoutManager(this@ClubUserListActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = clubUserListAdapter


            val gestureDetector =
                GestureDetector(this@ClubUserListActivity, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })

            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (gestureDetector.onTouchEvent(e)) {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        val position = rv.getChildAdapterPosition(child!!)
                        clubUserListAdapter.setCheck(position)
                    }
                    return super.onInterceptTouchEvent(rv, e)
                }
            })
        }
    }

    override fun onToolbarRightClick() {
        Intent().let {
            it.putParcelableArrayListExtra(CLUB_USER_LIST_MODEL, ArrayList<UserModel>().apply {
                viewModel.userListLiveData.value?.filter { user -> user.isCheck }?.toList()?.let { selectList ->
                    this.addAll(selectList)
                }
            })
            setResult(Activity.RESULT_OK, it)
            finish()
        }
    }
}
