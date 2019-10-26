package com.dk.project.bowling.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubBinding
import com.dk.project.bowling.ui.activity.ClubDetailActivity
import com.dk.project.bowling.ui.activity.MainActivity
import com.dk.project.bowling.ui.adapter.ClubAdapter
import com.dk.project.bowling.ui.adapter.SignClubViewPagerAdapter
import com.dk.project.bowling.ui.widget.CustomMarginPageTransformer
import com.dk.project.bowling.viewModel.ClubViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.utils.RxBus
import com.dk.project.post.utils.ScreenUtil


class ClubFragment : BindFragment<FragmentClubBinding, ClubViewModel>() {

    private lateinit var clubAdapter: ClubAdapter
    private lateinit var signClubViewPagerAdapter: SignClubViewPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_club
    }

    override fun createViewModel(): ClubViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubViewModel::class.java
        )
    }

    override fun registerLiveData() {
        viewModel.clubListLiveData.observe(this, Observer {
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

            val gestureDetector =
                GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })
            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (gestureDetector.onTouchEvent(e) && rv.findChildViewUnder(
                            e.x,
                            e.y
                        ) != null
                    ) {
                        rv.findChildViewUnder(e.x, e.y)?.let {
                            val position = rv.getChildAdapterPosition(it)
                            var intent = Intent(mContext, ClubDetailActivity::class.java)
                            var item = clubAdapter.getClubModel(position)
                            intent.putExtra(CLUB_MODEL, item)
                            startActivity(intent)
                        }
                        return true
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

//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//
//            })
        }


//        binding.searchClubEditText.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (TextUtils.isEmpty(v.text.toString().trim())) {
//                    Toast.makeText(activity, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
//                } else {
//                    TextViewUtil.hideKeyBoard(activity, binding.searchClubEditText)
//                    (activity as MainActivity).bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                }
//            }
//            true
//        }

        setBottomSheetHeight()
//        GlideApp.with(this).load(R.drawable.team_default_1).centerCrop()
//            .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 25)))
//            .into(binding.signClubViewPagerBg)
        return view
    }

    private fun setBottomSheetHeight() {
        binding.searchLayout.postDelayed({
            var param = (activity as MainActivity).binding.rlBottomSheet.layoutParams
            param.height =
                (activity as MainActivity).binding.navigation.height + binding.clubRecycler.height
            (activity as MainActivity).binding.rlBottomSheet.layoutParams = param
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
