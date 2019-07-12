package com.dk.project.post.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MediatorLiveData;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.service.CommunityService;
import com.dk.project.post.ui.activity.MediaSelectActivity;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.PermissionsUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.Utils;

import static com.dk.project.post.base.Define.*;

/**
 * Created by dkkim on 2017-10-04.
 */

public class WriteViewModel extends BaseViewModel {

    private final MediatorLiveData<Boolean> cameraClickEvent;
    private String clubId;

    public WriteViewModel(@NonNull Application application) {
        super(application);
        cameraClickEvent = new MediatorLiveData<>();
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        clubId = mContext.getIntent().getStringExtra("POST_CLUB_ID");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        int id = view.getId();

        if (id == R.id.image_select_btn) {
            PermissionsUtil.isPermission(mContext, granted -> {
                if (granted) {
                    mContext.startActivityForResult(new Intent(mContext, MediaSelectActivity.class), MEDIA_ATTACH_LIST);
                }
            });
        } else if (id == R.id.youtube_select_btn) {
            if (Utils.isYoutubeInstallCheck(mContext)) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(YOUTUBE_PACKAGE_NAME);
                mContext.startActivity(intent);
            }
        } else if (id == R.id.camera_select_btn) {
            cameraClickEvent.setValue(true);
        }
    }

    public void setImageViewPadding(ViewGroup inputParentLayout) {
        for (int i = 0; i < inputParentLayout.getChildCount(); i++) {
            if (i == 0 || i == inputParentLayout.getChildCount() - 1) {
                continue;
            }
            View view = inputParentLayout.getChildAt(i);
            View prevView = inputParentLayout.getChildAt(i - 1);
            if (view instanceof LinearLayout) {
                if (prevView instanceof LinearLayout) {
                    ((LinearLayout) view).getChildAt(0).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) view).getChildAt(0).setVisibility(View.GONE);
                }
            }
        }
    }

    public void checkShare() {
        Intent shareIntent = mContext.getIntent();
        if (Utils.isShareIntent(shareIntent)) {
            ((WriteActivity) mContext).setYoutubeFragment(shareIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    public void writePost(PostModel postModel) {
        postModel.setClubId(clubId);
        RxBus.getInstance().eventPost(new Pair(EVENT_WRITE_POST, postModel));
    }

    public void modifyPost(PostModel postModel) {
        postModel.setClubId(clubId);
        RxBus.getInstance().eventPost(new Pair(EVENT_MODIFY_POST, postModel));
    }

    public MediatorLiveData<Boolean> getCameraClickEvent() {
        return cameraClickEvent;
    }

    private void startService() {
        if (Utils.isServiceRunning(mContext)) {
        } else {
            mContext.startService(new Intent(mContext, CommunityService.class));
        }
    }
}
