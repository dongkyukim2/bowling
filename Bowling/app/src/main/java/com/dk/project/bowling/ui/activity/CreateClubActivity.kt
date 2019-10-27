package com.dk.project.bowling.ui.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateClubBinding
import com.dk.project.bowling.viewModel.CreateClubViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.utils.ScreenUtil

class CreateClubActivity : BindActivity<ActivityCreateClubBinding, CreateClubViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_create_club
    }

    override fun getViewModel(): CreateClubViewModel {
        return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(
            CreateClubViewModel::class.java
        )
    }

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        (binding.space.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentHeight =
                if (ScreenUtil.screenRatio(this@CreateClubActivity) < 56) { // 세로가 짧은 디바이스
                    0.1.toFloat()
                } else { // 세로가 긴 디바이스
                    0.2.toFloat()
                }
            binding.space.layoutParams = this
        }

        binding.clubSubTitleTextView.movementMethod = ScrollingMovementMethod()
    }
}
