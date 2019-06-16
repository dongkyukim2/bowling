package com.dk.project.bowling.ui.fragment


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubGameListBinding
import com.dk.project.bowling.model.ClubModel
import com.dk.project.bowling.ui.adapter.ClubGameListAdapter
import com.dk.project.bowling.viewModel.ClubScoreListViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define

class ClubGameListFragment : BindFragment<FragmentClubGameListBinding, ClubScoreListViewModel>() {

    private lateinit var clubGameListAdapter: ClubGameListAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_club_game_list
    }

    override fun createViewModel() = ViewModelProviders.of(this).get(ClubScoreListViewModel::class.java)

    override fun registerLiveData() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)


        binding.clubGameRecyclerView.apply {

            clubGameListAdapter = ClubGameListAdapter()

            layoutManager = LinearLayoutManager(mContext)
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
                            val child = rv.findChildViewUnder(e.x, e.y)
                            val position = rv.getChildAdapterPosition(child!!)
                            Toast.makeText(mContext, position.toString(), Toast.LENGTH_SHORT).show()


                            return true
                        }
                        return super.onInterceptTouchEvent(rv, e)
                    }
                })
            }


        }


        return view
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
