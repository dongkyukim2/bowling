package com.dk.project.bowling.ui.fragment


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.FragmentClubDetailBinding
import com.dk.project.bowling.shareData.ShareData
import com.dk.project.bowling.ui.activity.ClubDetailActivity
import com.dk.project.bowling.utils
import com.dk.project.bowling.viewModel.ClubDetailHomeViewModel
import com.dk.project.post.base.BindFragment
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.ClubModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.utils.GlideApp

/**
 * A simple [Fragment] subclass.
 *
 */
class ClubDetailFragment : BindFragment<FragmentClubDetailBinding, ClubDetailHomeViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_club_detail
    }

    override fun createViewModel(): ClubDetailHomeViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            ClubDetailHomeViewModel::class.java
        )
    }

    override fun registerLiveData() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = super.onCreateView(inflater, container, savedInstanceState)

        binding.viewModel = viewModel

        ShareData.getInstance().clubUserList.clear()

        GlideApp.with(activity!!).asBitmap().load(utils.getDefaultImage()).centerCrop()
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource!!).generate { palette ->
                        for (swatch in palette!!.swatches) {
                            if (swatch != null) {
                                binding.signUpBtn.setBackgroundColor(swatch.rgb)
                                break
                            }
                        }
                    }

                    return false
                }

            }).into(binding.clubTitleImage)

        arguments?.apply {
            getParcelable<ClubModel?>(Define.CLUB_MODEL)?.apply {
                binding.clubTitleTextView.text = clubTitle
                binding.clubSubTitleTextView.text = clubInfo
                viewModel.setClubModel(this)
                (activity as ClubDetailActivity).paletteColorLiveData.observe(
                    viewLifecycleOwner,
                    Observer {
                        binding.clubTitleTextView.setTextColor(it.titleTextColor)
                        binding.clubSubTitleTextView.setTextColor(it.bodyTextColor)
                    })
                BowlingApi.getInstance().getClubUserList(
                    clubId, {
                        binding.clubUserCount.text =
                            it.data.filter { user -> user.type <= Define.USER_TYPE_OWNER }
                                .size.toString()
                        ShareData.getInstance().clubUserList.addAll(it.data)
                    }, { })


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

        return view
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
