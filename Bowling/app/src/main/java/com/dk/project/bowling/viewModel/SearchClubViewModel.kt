package com.dk.project.bowling.viewModel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.dk.project.post.base.BaseViewModel
import com.dk.project.post.bowling.model.ClubModel
import com.dk.project.post.bowling.retrofit.BowlingApi

class SearchClubViewModel(application: Application) : BaseViewModel(application) {

    val searchLiveData = MutableLiveData<Pair<ArrayList<ClubModel>, Boolean>>()

    override fun onThrottleClick(view: View?) {

    }

    fun requestSearchClub(name: String, page: Int) {
        BowlingApi.getInstance()
            .getSearchClubList(name, page, {
                if (it.isSuccess) {
                    if (it.data.isEmpty()) {
                        if (page == 0) {
                            Toast.makeText(mContext, "검색된 클럽이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        searchLiveData.value = Pair(it.data, page == 0)
                    }
                }
            }, {

            })

    }
}
