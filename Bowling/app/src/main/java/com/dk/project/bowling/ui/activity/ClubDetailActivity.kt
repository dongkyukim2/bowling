package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubDetailBinding
import com.dk.project.bowling.ui.adapter.ClubDetailPagerAdapter
import com.dk.project.bowling.viewModel.ClubDetailViewModel
import com.dk.project.post.base.BindActivity

class ClubDetailActivity : BindActivity<ActivityClubDetailBinding, ClubDetailViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_club_detail
    }

    override fun getViewModel(): ClubDetailViewModel {
        return ViewModelProviders.of(this).get(ClubDetailViewModel::class.java)
    }

    override fun subscribeToModel() {


        binding.clubViewpager.apply {
            offscreenPageLimit = 2
            adapter = ClubDetailPagerAdapter(
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                viewModel.clubModel
            )
            binding.layoutTab.setupWithViewPager(this)

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    when (position) {
                        1 -> toolbarRightButton.visibility = View.VISIBLE
                        else -> toolbarRightButton.visibility = View.GONE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }


    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        viewModel.clubModel = intent.getParcelableExtra(CLUB_MODEL)
        super.onPostCreate(savedInstanceState)
    }


    override fun onToolbarRightClick() {
        var intent = Intent(this, CreateGameActivity::class.java)
        startActivity(intent)
    }
}
