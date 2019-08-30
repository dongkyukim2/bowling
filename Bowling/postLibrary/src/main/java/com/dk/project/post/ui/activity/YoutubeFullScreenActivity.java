package com.dk.project.post.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityYoutubeFullscreenBinding;
import com.dk.project.post.utils.youtube.FullScreenHelper;
import com.dk.project.post.viewModel.YoutubeFullScreenViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import org.jetbrains.annotations.NotNull;

public class YoutubeFullScreenActivity extends BindActivity<ActivityYoutubeFullscreenBinding, YoutubeFullScreenViewModel> {

    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    private YouTubePlayer youTubePlayer;
    private String videoId;
    private int currentSeconds;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_youtube_fullscreen;
    }

    @Override
    protected YoutubeFullScreenViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(YoutubeFullScreenViewModel.class);
    }

    @Override
    protected void subscribeToModel() {
        videoId = getIntent().getExtras().getString(YOUTUBE_VIDEO_ID);
        currentSeconds = getIntent().getExtras().getInt(YOUTUBE_VIDEO_CURRENT_SECONDS);
        initYouTubePlayerView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
//    binding.youtubePlayerView.getPlayerUIController().getMenu().dismiss();
    }

    private void initYouTubePlayerView() {

        initPlayerMenu();

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.

        getLifecycle().addObserver(binding.youtubePlayerView);

        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                YoutubeFullScreenActivity.this.youTubePlayer = youTubePlayer;
                youTubePlayer.loadVideo(videoId, Math.max(0, currentSeconds - 1));
                addFullScreenListenerToPlayer(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                currentSeconds = (int) second;
            }
        });
    }

    /**
     * Shows the menu button in the player and adds an item to it.
     */
    private void initPlayerMenu() {
        fullScreenHelper.enterFullScreen();
        binding.youtubePlayerView.enterFullScreen();
        YoutubeFullScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    binding.youtubePlayerView.getPlayerUIController().showYouTubeButton(false);
//        youTubePlayerView.getPlayerUIController().showMenuButton(true);
//        youTubePlayerView.getPlayerUIController().getMenu().addItem(
//                new MenuItem("example", R.drawable.ic_settings_24dp, (view) -> Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show())
//        );
    }

    /**
     * Load a video if the activity is resumed, cue it otherwise. See difference between {@link YouTubePlayer#cueVideo(String, float)} and {@link
     * YouTubePlayer#loadVideo(String, float)}
     * <p>
     * With this library is possible to play videos even if the player is not visible. But this goes against YouTube's terms of service therefore, if
     * you plan to publish your app on the Play Store, always pause the video when the player is not visible. If you don't intend to publish your app on
     * the Play Store you can play and pause whenever you want.
     */
    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            youTubePlayer.loadVideo(videoId, 0);
        } else {
            youTubePlayer.cueVideo(videoId, 0);
        }

        //setVideoTitle(youTubePlayerView.getPlayerUIController(), videoId);
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        binding.youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                if (youTubePlayer != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(YOUTUBE_VIDEO_CURRENT_SECONDS, currentSeconds);
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayer != null) {
            if (binding.youtubePlayerView.isFullScreen()) {
                binding.youtubePlayerView.exitFullScreen();
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra(YOUTUBE_VIDEO_CURRENT_SECONDS, currentSeconds);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
    }

    /**
     * This method adds a new custom action to the player. Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
//    Drawable customActionIcon = ContextCompat.getDrawable(this, R.drawable.ic_pause_36dp);
//
//    binding.youtubePlayerView.getPlayerUIController().setCustomAction1(customActionIcon, view -> {
//      if (youTubePlayer != null) {
//        youTubePlayer.pause();
//      }
//    });
    }

    private void removeCustomActionFromPlayer() {
//    binding.youtubePlayerView.getPlayerUIController().showCustomAction1(false);
    }

}
