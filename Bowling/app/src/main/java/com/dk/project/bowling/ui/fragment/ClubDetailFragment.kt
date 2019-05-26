package com.dk.project.bowling.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubDetailBinding
import com.dk.project.bowling.model.ClubModel
import com.dk.project.bowling.viewModel.ClubDetailHomeViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define

/**
 * A simple [Fragment] subclass.
 *
 */
class ClubDetailFragment : BindFragment<FragmentClubDetailBinding, ClubDetailHomeViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_club_detail
    }

    override fun createViewModel(): ClubDetailHomeViewModel {
        return ViewModelProviders.of(this).get(ClubDetailHomeViewModel::class.java)
    }

    override fun registerLiveData() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)


        arguments?.apply {
           getParcelable<ClubModel?>(Define.CLUB_MODEL)?.apply {

               binding.clubTitleTextView.text = clubTitle
               binding.clubSubTitleTextView.text = clubInfo
           }





        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, clubModel: ClubModel?) =
            ClubDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                    putParcelable(Define.CLUB_MODEL, clubModel)
                }
            }
    }
}
