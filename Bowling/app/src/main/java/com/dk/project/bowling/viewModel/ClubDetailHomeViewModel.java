package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.dk.project.bowling.R;
import com.dk.project.post.base.BaseViewModel;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubDetailHomeViewModel extends BaseViewModel {

    private boolean clubSign;

    public ClubDetailHomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_btn:
                if (clubSign) {
                    Toast.makeText(mContext, "탈퇴하기", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "가입하기", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setClubSign(boolean clubSign) {
        this.clubSign = clubSign;
    }
}
