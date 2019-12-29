package com.dk.project.bowling.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentScoreListBinding
import com.dk.project.bowling.ui.adapter.DayScoreAdapter
import com.dk.project.bowling.viewModel.ScoreListViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.utils.AlertDialogUtil


/**
 * A simple [Fragment] subclass.
 *
 */
class ScoreListFragment : BindFragment<FragmentScoreListBinding, ScoreListViewModel>() {

    private lateinit var dayAdapter: DayScoreAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_score_list
    }

    override fun createViewModel(): ScoreListViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ScoreListViewModel::class.java
        )
    }

    override fun registerLiveData() {
        viewModel.scoreDayListLiveData.observe(mContext, Observer {
            dayAdapter.setDayScoreList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)


        binding.dayScoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity).apply {
                orientation = RecyclerView.VERTICAL
            }
            dayAdapter = DayScoreAdapter()
            adapter = dayAdapter
            itemAnimator = DefaultItemAnimator()
            dayAdapter.setRecyclerViewLongClickListener {

                AlertDialogUtil.showBottomSheetDialog(activity) {
                    if (it.id == com.dk.project.post.R.id.btnModify) {
                        Toast.makeText(activity, "수정하기", Toast.LENGTH_SHORT).show()
                    } else {
                        AlertDialogUtil.showAlertDialog(activity,
                            null,
                            "삭제 하시겠습니까?",
                            { _, _ ->
                                Toast.makeText(activity, "삭제하기", Toast.LENGTH_SHORT).show()
                            }, { _, _ -> })

                    }
                }
            }

        }
        return view
    }
}
