package com.dk.project.bowling.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateClubBinding
import com.dk.project.bowling.utils
import com.dk.project.bowling.viewModel.CreateClubViewModel
import com.dk.project.post.base.BindActivity
import com.dk.project.post.bowling.model.ClubModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.retrofit.ErrorCallback
import com.dk.project.post.retrofit.SuccessCallback
import com.dk.project.post.utils.GlideApp

class CreateClubActivity : BindActivity<ActivityCreateClubBinding, CreateClubViewModel>() {

    private var defaultImageIndex: Int = 0


    override fun getLayoutId(): Int {
        return R.layout.activity_create_club
    }

    override fun getViewModel(): CreateClubViewModel {
        return ViewModelProviders.of(this).get(CreateClubViewModel::class.java)
    }

    override fun subscribeToModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarRightButton.visibility = View.VISIBLE

        defaultImageIndex = utils.getDefaultImageIndex()

        GlideApp.with(this).load(utils.getDefaultImage(defaultImageIndex)).into(binding.clubTitleImageView)

    }

    override fun onToolbarRightClick() {

        BowlingApi.getInstance().createClub(ClubModel().apply {
            clubTitle = binding.clubTitleTextView.text.toString()
            clubInfo = binding.clubSubTitleTextView.text.toString()
            clubImage = defaultImageIndex.toString()
        }, SuccessCallback {
            finish()
        }, ErrorCallback {
            Toast.makeText(this, "클럽 만들기 실패", Toast.LENGTH_LONG).show()
        })
    }

}
