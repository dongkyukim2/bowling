package com.dk.project.bowling.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubDetailBinding
import com.dk.project.bowling.viewModel.ClubDetailHomeViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.ClubModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.utils.GlideApp
import com.dk.project.post.utils.ImageUtil
import com.dk.project.post.utils.RxBus
import com.dk.project.post.utils.ScreenUtil


/**
 * A simple [Fragment] subclass.
 *
 */
class ClubDetailFragment : BindFragment<FragmentClubDetailBinding, ClubDetailHomeViewModel>() {

    override fun getLayoutId() = R.layout.fragment_club_detail

    override fun createViewModel() =
        ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubDetailHomeViewModel::class.java
        )

    override fun registerLiveData() {
        RxBus.getInstance().registerRxObserver { integerObjectPair ->
            when (integerObjectPair.first) {
                Define.EVENT_REFRESH_CLUB_USER_LIST -> getUserList()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)

        binding.viewModel = viewModel

        arguments?.apply {
            getParcelable<ClubModel?>(Define.CLUB_MODEL)?.apply {
                binding.clubTitleTextView.text = clubTitle
                binding.clubSubTitleTextView.text = clubInfo
                binding.clubSubTitleTextView.movementMethod = ScrollingMovementMethod()

                GlideApp.with(activity!!).asBitmap()
                    .load(if (clubImage.length == 1) R.drawable.team_default_1 else Define.IMAGE_URL + clubImage)
                    .centerCrop()
                    .apply(ImageUtil.getGlideRequestOption())
                    .into(binding.clubTitleImage)

                viewModel.clubModel = this

                getUserList()

                when (type) {
                    Define.USER_TYPE_JOIN,
                    Define.USER_TYPE_OWNER -> {
                        binding.signUpBtn.text = "탈퇴하기"
                    }
                    Define.USER_TYPE_JOIN_WAIT -> {
                        binding.signUpBtn.text = "가입신청 취소하기"
                    }
                    Define.USER_TYPE_SECESSION,
                    Define.USER_TYPE_FORCE_SECESSION,
                    Define.USER_TYPE_NOT_JOIN -> {
                        binding.signUpBtn.text = "가입신청 하기"
                    }
                }
            }
        }

        (binding.space.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentHeight =
                if (ScreenUtil.screenRatio(activity) < 56) { // 세로가 짧은 디바이스
                    0.1.toFloat()
                } else { // 세로가 긴 디바이스
                    0.2.toFloat()
                }
            binding.space.layoutParams = this
        }

        return view
    }

    fun getUserList() {
        BowlingApi.getInstance().getClubUserList(
            viewModel.clubModel.clubId, {
                binding.clubUserCount.text =
                    it.data.filter { user -> user.type <= Define.USER_TYPE_OWNER }.size.toString()
            }, { })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            Define.CLUB_MODIFY -> {
                data?.getParcelableExtra<ClubModel>(Define.CLUB_MODEL)?.let {
                    viewModel.clubModel.clubTitle = it.clubTitle
                    viewModel.clubModel.clubInfo = it.clubInfo
                    viewModel.clubModel.clubImage = it.clubNewImage
                }

                binding.clubTitleTextView.text = viewModel.clubModel.clubTitle
                binding.clubSubTitleTextView.text = viewModel.clubModel.clubInfo

                GlideApp.with(activity!!).asBitmap()
                    .load(if (viewModel.clubModel.clubImage.length == 1) R.drawable.team_default_1 else Define.IMAGE_URL + viewModel.clubModel.clubImage)
                    .centerCrop()
                    .apply(ImageUtil.getGlideRequestOption())
                    .into(binding.clubTitleImage)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, clubModel: ClubModel?) = ClubDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("position", position)
                putParcelable(Define.CLUB_MODEL, clubModel)
            }
        }
    }
}
