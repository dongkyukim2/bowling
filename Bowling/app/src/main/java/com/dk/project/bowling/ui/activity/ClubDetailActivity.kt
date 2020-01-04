package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubDetailBinding
import com.dk.project.bowling.ui.adapter.ClubDetailPagerAdapter
import com.dk.project.bowling.ui.fragment.ClubDetailFragment
import com.dk.project.bowling.viewModel.ClubDetailViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.ClubUserModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.manager.LoginManager
import com.dk.project.post.ui.activity.WriteActivity
import com.dk.project.post.utils.AlertDialogUtil
import com.dk.project.post.utils.ToastUtil
import com.dk.project.post.utils.Utils


class ClubDetailActivity : BindActivity<ActivityClubDetailBinding, ClubDetailViewModel>() {

    private lateinit var clubDetailPagerAdapter: ClubDetailPagerAdapter

    var currentPosition = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_club_detail
    }

    override fun getViewModel(): ClubDetailViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubDetailViewModel::class.java
        )
    }

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        toolbarRightButton.visibility = View.VISIBLE

        binding.clubViewpager.apply {
            offscreenPageLimit = 2
            clubDetailPagerAdapter = ClubDetailPagerAdapter(this@ClubDetailActivity, viewModel)
            adapter = clubDetailPagerAdapter
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position
                    when (position) {
                        0 -> toolbarRightButton.setImageResource(R.drawable.ic_action_add_user)
                        else -> toolbarRightButton.setImageResource(R.drawable.ic_action_plus)
                    }
                }
            })
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    binding.clubViewpager.setCurrentItem(0, true)
                }
                R.id.navigation_score_board -> {
                    movePage(1)
                }
                R.id.navigation_post_board -> {
                    movePage(2)
                }
            }
            true
        }
    }

    private fun movePage(index: Int) {
        if (!LoginManager.getInstance().isLogIn) {
            AlertDialogUtil.showLoginAlertDialog(this)
            return
        }
        when (viewModel.clubModel.type) {
            Define.USER_TYPE_JOIN, Define.USER_TYPE_OWNER -> binding.clubViewpager.setCurrentItem(
                index,
                true
            )
            Define.USER_TYPE_JOIN_WAIT -> ToastUtil.showToastCenter(this, "가입신청 대기중입니다.")
            else -> ToastUtil.showToastCenter(this, "가입신청을 해주세요.")
        }
    }

    override fun onToolbarRightClick() {
        super.onToolbarRightClick()
        if (LoginManager.getInstance().loginInfoModel == null) {
            AlertDialogUtil.showLoginAlertDialog(this)
            return
        }
        when (currentPosition) {
            0 -> {
                AlertDialogUtil.showEditTextAlertDialog(
                    this,
                    "일반회원 추가",
                    "이름을 입력하세요."
                ) {

                    val clubUserModel = ClubUserModel()
                    clubUserModel.clubId = viewModel.clubModel.clubId
                    clubUserModel.userId = "N_" + Utils.getRandomString()
                    clubUserModel.type = USER_TYPE_JOIN
                    clubUserModel.userName = (it as EditText).text.toString().trim()
                    viewModel.executeRx(
                        BowlingApi.getInstance().setModifyClubUserType(
                            clubUserModel,
                            { receivedData ->
                                if (receivedData.isSuccess) {
                                    (clubDetailPagerAdapter.createFragment(0) as ClubDetailFragment).getUserList();
                                    Toast.makeText(this, "일반회원 추가 성공", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "일반회원 추가 실패", Toast.LENGTH_SHORT).show()
                                }
                            },
                            {
                                Toast.makeText(this, "일반회원 추가 실패", Toast.LENGTH_SHORT).show()
                            })
                    )
                }
            }
            1 -> Intent(this, CreateGameActivity::class.java).let {
                it.putExtra(CLUB_MODEL, viewModel.clubModel)
                startActivityForResult(it, REFRESH_GAME_LIST)
            }

            2 -> Intent(this, WriteActivity::class.java).let {
                it.putExtra("POST_CLUB_ID", viewModel.clubModel.clubId)
                startActivity(it)
            }

        }
    }
}
