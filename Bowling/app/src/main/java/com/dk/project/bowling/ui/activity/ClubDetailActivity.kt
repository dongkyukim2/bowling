package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubDetailBinding
import com.dk.project.bowling.ui.adapter.ClubDetailPagerAdapter
import com.dk.project.bowling.ui.fragment.ClubGameListFragment
import com.dk.project.bowling.viewModel.ClubDetailViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.ui.activity.WriteActivity
import com.dk.project.post.utils.ToastUtil


class ClubDetailActivity : BindActivity<ActivityClubDetailBinding, ClubDetailViewModel>() {

    var paletteColorLiveData = MutableLiveData<Palette.Swatch>()
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

        toolbar.setBackgroundResource(android.R.color.transparent)

        toolbarRightButton.visibility = View.VISIBLE
        toolbarRightButton.setImageResource(R.drawable.ic_action_plus)

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
                        0 -> toolbarRightButton.visibility = View.GONE
                        else -> toolbarRightButton.visibility = View.VISIBLE
                    }
                }
            })
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.getItemId()) {
                R.id.navigation_home -> {
                    binding.clubViewpager.setCurrentItem(0, true)
                    true
                }
                R.id.navigation_score_board -> {
                    if (viewModel.isSign) {
                        binding.clubViewpager.setCurrentItem(1, true)
                    } else {
                        ToastUtil.showToastCenter(this, "가입신청을 해주세요.")
                    }
                    true
                }
                R.id.navigation_post_board -> {
                    if (viewModel.isSign) {
                        binding.clubViewpager.setCurrentItem(2, true)
                    } else {
                        ToastUtil.showToastCenter(this, "가입신청을 해주세요.")
                    }
                    true

                }
            }
            false
        }
    }

    override fun onToolbarRightClick() {
        super.onToolbarRightClick()
        when (currentPosition) {
            2 -> {
                Intent(this, WriteActivity::class.java).let {
                    it.putExtra("POST_CLUB_ID", viewModel.clubModel.clubId)
                    startActivity(it)
                }
            }
            else -> {
                Intent(this, CreateGameActivity::class.java).let {
                    it.putExtra(CLUB_MODEL, viewModel.clubModel)
                    startActivityForResult(it, REFRESH_GAME_LIST)
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.filterIsInstance<ClubGameListFragment>()
            .map { fragment -> fragment.onActivityResult(requestCode, resultCode, data) }
    }
}
