package com.dk.project.bowling.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubGameListBinding
import com.dk.project.bowling.ui.activity.CreateGameActivity
import com.dk.project.bowling.ui.adapter.ClubGameListAdapter
import com.dk.project.bowling.viewModel.ClubScoreListViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.ClubModel

class ClubGameListFragment : BindFragment<FragmentClubGameListBinding, ClubScoreListViewModel>() {

    private lateinit var clubGameListAdapter: ClubGameListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun getLayoutId() = R.layout.fragment_club_game_list

    override fun createViewModel() =
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubScoreListViewModel::class.java
        )

    override fun registerLiveData() {
        viewModel.gameMutableLiveData.observe(this, Observer {
            binding.emptyView.visibility = if (it.first == null) View.VISIBLE else View.GONE
            if (it.first != null) {
                clubGameListAdapter.setClubList(it)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)


        binding.clubGameRecyclerView.apply {

            clubGameListAdapter = ClubGameListAdapter()

            linearLayoutManager = LinearLayoutManager(mContext)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = clubGameListAdapter

            GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            }).let {
                addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        if (it.onTouchEvent(e)) {
                            rv.findChildViewUnder(e.x, e.y)?.let { view ->
                                val position = rv.getChildAdapterPosition(view)
                                var intent = Intent(activity, CreateGameActivity::class.java)
                                intent.putExtra(
                                    Define.READ_GAME_MODEL,
                                    clubGameListAdapter.getClubGame(position)
                                )
                                activity?.startActivity(intent)
                                return true
                            }
                        }
                        return super.onInterceptTouchEvent(rv, e)
                    }
                })

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        linearLayoutManager.let {
                            if (dy > 0 && !viewModel.isLoading && linearLayoutManager.itemCount - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                                viewModel.isLoading = true
                                viewModel.requestGameList(linearLayoutManager.itemCount)
                            }
                        }

                    }
                })
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            Define.REFRESH_GAME_LIST -> {
                viewModel.requestGameList(0)
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(position: Int, clubModel: ClubModel?) =
            ClubGameListFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                    putParcelable(Define.CLUB_MODEL, clubModel)
                }
            }
    }
}
