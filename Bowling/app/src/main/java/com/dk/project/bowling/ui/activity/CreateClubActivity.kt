package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.lifecycle.ViewModelProvider
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateClubBinding
import com.dk.project.bowling.viewModel.CreateClubViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.controller.ListController
import com.dk.project.post.utils.GlideApp
import com.dk.project.post.utils.ImageUtil

class CreateClubActivity : BindActivity<ActivityCreateClubBinding, CreateClubViewModel>() {

    override fun getLayoutId() = R.layout.activity_create_club

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(CreateClubViewModel::class.java)

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        binding.clubSubTitleTextView.movementMethod = ScrollingMovementMethod()


        viewModel.clubModel?.let {
            binding.clubTitleTextView.setText(it.clubTitle)
            binding.clubSubTitleTextView.setText(it.clubInfo)
            toolbarTitle.text = "수정하기"
            binding.createClubBtn.text = "수정완료"
        }

        ListController.getInstance().mediaSelectList.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            MEDIA_ATTACH_LIST -> GlideApp.with(this).applyDefaultRequestOptions(ImageUtil.getGlideRequestOption())
                .load(ListController.getInstance().mediaSelectList[0].filePath)
                .centerCrop()
                .into(binding.clubTitleImageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ListController.getInstance().mediaSelectList.clear()
    }
}
