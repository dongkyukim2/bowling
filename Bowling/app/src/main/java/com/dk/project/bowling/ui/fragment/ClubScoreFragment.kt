package com.dk.project.bowling.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentScoreListBinding
import com.dk.project.bowling.model.ClubModel
import com.dk.project.bowling.viewModel.ClubScoreListViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define

class ClubScoreFragment : BindFragment<FragmentScoreListBinding, ClubScoreListViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_club_score
    }

    override fun createViewModel(): ClubScoreListViewModel {
        return ViewModelProviders.of(this).get(ClubScoreListViewModel::class.java)
    }

    override fun registerLiveData() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)
        arguments?.apply {
            getParcelable<ClubModel?>(Define.CLUB_MODEL)?.apply {

            }
        }
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(position: Int, clubModel: ClubModel?) =
            ClubScoreFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                    putParcelable(Define.CLUB_MODEL, clubModel)
                }
            }
    }
}
