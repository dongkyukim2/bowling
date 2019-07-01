package com.dk.project.bowling.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityClubDetailBinding
import com.dk.project.bowling.ui.adapter.ClubDetailPagerAdapter
import com.dk.project.bowling.ui.fragment.ClubGameListFragment
import com.dk.project.bowling.viewModel.ClubDetailViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.utils.GlideApp
import com.dk.project.post.utils.ImageUtil
import jp.wasabeef.glide.transformations.BlurTransformation

class ClubDetailActivity : BindActivity<ActivityClubDetailBinding, ClubDetailViewModel>() {

    var paletteColorLiveData = MutableLiveData<Palette.Swatch>()

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

        GlideApp.with(this).load(R.drawable.team_default).apply(bitmapTransform(BlurTransformation(25, 25)))
            .into(binding.clubBackImg)
        ImageUtil.getClubColors(this, R.drawable.team_default) {
            //            paletteColorLiveData.value = it
            //            binding.createGameBtn.backgroundTintList = ColorStateList.valueOf(it.rgb)
            window.statusBarColor = it.rgb

        }
        toolbar.setBackgroundResource(android.R.color.transparent)

        binding.layoutTab.setSelectedTabIndicatorColor(Color.parseColor("#000000"))
        binding.layoutTab.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#000000"))

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
                        1 -> binding.createGameBtn.visibility = View.VISIBLE
                        else -> binding.createGameBtn.visibility = View.GONE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }

        binding.createGameBtn.setOnClickListener {
            var intent = Intent(this, CreateGameActivity::class.java)
            intent.putExtra(CLUB_MODEL, viewModel.clubModel)
            startActivityForResult(intent, REFRESH_GAME_LIST)
        }


        for (i in 0 until binding.layoutTab.tabCount) {
            var textview = ((binding.layoutTab.getTabAt(i)?.view as ViewGroup).getChildAt(1) as AppCompatTextView)
            textview.setTypeface(textview.typeface, Typeface.BOLD)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.filterIsInstance<ClubGameListFragment>()
            .map { fragment -> fragment.onActivityResult(requestCode, resultCode, data) }
    }
}
