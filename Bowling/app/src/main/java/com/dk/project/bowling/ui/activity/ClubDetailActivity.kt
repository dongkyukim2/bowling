package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubDetailBinding
import com.dk.project.bowling.ui.adapter.ClubDetailPagerAdapter
import com.dk.project.bowling.ui.fragment.ClubGameListFragment
import com.dk.project.bowling.viewModel.ClubDetailViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.ui.activity.WriteActivity
import com.google.android.material.tabs.TabLayoutMediator


class ClubDetailActivity : BindActivity<ActivityClubDetailBinding, ClubDetailViewModel>() {

    var paletteColorLiveData = MutableLiveData<Palette.Swatch>()
    private lateinit var clubDetailPagerAdapter: ClubDetailPagerAdapter

    var currentPosition = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_club_detail
    }

    override fun getViewModel(): ClubDetailViewModel {
        return ViewModelProviders.of(this).get(ClubDetailViewModel::class.java)
    }

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        GlideApp.with(this).load(utils.getDefualtImage()).apply(bitmapTransform(BlurTransformation(25, 25)))
            .into(binding.clubBackImg)
        ImageUtil.getClubColors(this, utils.getDefualtImage()) {
            //            paletteColorLiveData.value = it
            //            binding.createGameBtn.backgroundTintList = ColorStateList.valueOf(it.rgb)
            window.statusBarColor = it.rgb
        }
        */


        binding.viewModel = viewModel

        toolbar.setBackgroundResource(android.R.color.transparent)

        toolbarRightButton.visibility = View.VISIBLE
        toolbarRightButton.setImageResource(R.drawable.ic_action_plus)

        binding.layoutTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.startColor))
        binding.layoutTab.setTabTextColors(
            Color.parseColor("#b4b4b4"),
            ContextCompat.getColor(this, R.color.startColor)
        )


        if (viewModel.isSign) binding.layoutTabDisable.visibility = View.GONE

        binding.clubViewpager.apply {
            offscreenPageLimit = 2
            clubDetailPagerAdapter = ClubDetailPagerAdapter(this@ClubDetailActivity, viewModel)
            adapter = clubDetailPagerAdapter
            isUserInputEnabled = viewModel.isSign
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


        TabLayoutMediator(binding.layoutTab, binding.clubViewpager, true) { tab, position ->
            tab.text = clubDetailPagerAdapter.getPageTitle(position)
        }.attach()

        for (i in 0 until binding.layoutTab.tabCount) {
            var textView = ((binding.layoutTab.getTabAt(i)?.view as ViewGroup).getChildAt(1) as AppCompatTextView)
            textView.setTypeface(textView.typeface, Typeface.BOLD)
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
