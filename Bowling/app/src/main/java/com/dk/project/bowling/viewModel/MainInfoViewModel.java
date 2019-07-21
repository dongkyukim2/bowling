package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.bowling.ui.activity.ScoreListActivity;
import com.dk.project.bowling.ui.fragment.GraphFragment;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.bowling.model.ScoreModel;

import static com.dk.project.post.base.Define.SCORE_DATE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainInfoViewModel extends BaseViewModel {

    // 점수등록했을떄 갱신용
    private MutableLiveData<ScoreModel> writeScoreLiveData = new MutableLiveData<>();

    public MainInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        switch (view.getId()) {
            case R.id.score_graph_left:

                ((GraphFragment) ((MainActivity) bindFragment.getActivity()).getMainViewPagerFragmentAdapter().createFragment(1)).getBinding().prevMonth.callOnClick();
                break;
            case R.id.score_graph_right:
                ((GraphFragment) ((MainActivity) bindFragment.getActivity()).getMainViewPagerFragmentAdapter().createFragment(1)).getBinding().nextMonth.callOnClick();
                break;
        }
    }

    public void getScoreDayList(ScoreModel scoreModel) {
        Intent intent = new Intent(mContext, ScoreListActivity.class);
        intent.putExtra(SCORE_DATE, scoreModel.getPlayDate());
        mContext.startActivity(intent);
    }

    public MutableLiveData<ScoreModel> getWriteScoreLiveData() {
        return writeScoreLiveData;
    }
}
