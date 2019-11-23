package com.dk.project.bowling.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubBinding
import com.dk.project.bowling.ui.activity.ClubDetailActivity
import com.dk.project.bowling.ui.adapter.ClubAdapter
import com.dk.project.bowling.ui.adapter.SignClubViewPagerAdapter
import com.dk.project.bowling.ui.widget.CustomMarginPageTransformer
import com.dk.project.bowling.viewModel.ClubViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.utils.RxBus
import com.dk.project.post.utils.ScreenUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior


class ClubFragment : BindFragment<FragmentClubBinding, ClubViewModel>() {

    private lateinit var clubAdapter: ClubAdapter
    private lateinit var signClubViewPagerAdapter: SignClubViewPagerAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayoutCompat>

    override fun getLayoutId() = R.layout.fragment_club

    override fun createViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(ClubViewModel::class.java)

    override fun registerLiveData() {
        viewModel.clubListLiveData.observe(this, Observer {

            binding.emptyView.visibility =
                if (it.first?.data?.isEmpty()!!) View.VISIBLE else View.GONE
            
            clubAdapter.setClubList(it)
            signClubViewPagerAdapter.setClubList(it)
            binding.dotsIndicator.setViewPager2(binding.signClubViewPager)
        })

        viewModel.executeRx(RxBus.getInstance().registerRxObserver { pair ->
            run {
                when (pair.first) {
                    EVENT_REFRESH_MY_CLUB_LIST -> {
                        viewModel.getClubList()
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)

        binding.clubRecycler.apply {
            clubAdapter = ClubAdapter()
            adapter = clubAdapter
            layoutManager = LinearLayoutManager(activity).apply {
                orientation = RecyclerView.VERTICAL
            }
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)

            val gestureDetector =
                GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })
            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (gestureDetector.onTouchEvent(e)) {
                        rv.findChildViewUnder(e.x, e.y)?.let {
                            val position = rv.getChildAdapterPosition(it)
                            var intent = Intent(mContext, ClubDetailActivity::class.java)
                            var item = clubAdapter.getClubModel(position)
                            intent.putExtra(CLUB_MODEL, item)
                            startActivity(intent)
                            return true
                        }
                    }
                    return super.onInterceptTouchEvent(rv, e)
                }
            })
        }

        binding.signClubViewPager.apply {
            setPageTransformer(CustomMarginPageTransformer(ScreenUtil.dpToPixel(46) * -1))
            signClubViewPagerAdapter = SignClubViewPagerAdapter(activity)
            offscreenPageLimit = 3
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = signClubViewPagerAdapter
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.recommendClubParent)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                binding.signClubViewPager.alpha = abs(1.0f - slideOffset)
//                binding.recommendClubTextView
//                    .setBackgroundColor(Utils.getColorWithAlpha(Color.WHITE, slideOffset))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
        setRecommendClubHeight()

        return view
    }

    private fun setRecommendClubHeight() {
        binding.clubRecycler.postDelayed({
            bottomSheetBehavior.peekHeight = binding.recommendClubParentSpace.height
            binding.recommendClubParent.requestLayout()
        }, 1000)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClubFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
