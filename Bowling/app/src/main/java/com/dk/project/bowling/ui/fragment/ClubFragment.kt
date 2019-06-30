package com.dk.project.bowling.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubBinding
import com.dk.project.bowling.ui.activity.ClubDetailActivity
import com.dk.project.bowling.ui.adapter.ClubAdapter
import com.dk.project.bowling.viewModel.ClubViewModel
import com.dk.project.post.base.BindFragment


class ClubFragment : BindFragment<FragmentClubBinding, ClubViewModel>() {

    private lateinit var clubAdapter: ClubAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_club
    }

    override fun createViewModel(): ClubViewModel {
        return ViewModelProviders.of(this).get(ClubViewModel::class.java)
    }

    override fun registerLiveData() {

        viewModel.clubListLiveData.observe(this, Observer {
            if (it.isEmpty()) {
                binding.emptyClub.visibility = View.VISIBLE
            } else {
                binding.emptyClub.visibility = View.GONE
                clubAdapter.setClubList(it)
            }

        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)

        binding.clubRecycler.apply {
            clubAdapter = ClubAdapter()
            adapter = clubAdapter
            layoutManager = LinearLayoutManager(activity).apply {
                orientation = RecyclerView.VERTICAL
            }
            itemAnimator = DefaultItemAnimator()

            val gestureDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
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

        return view
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
