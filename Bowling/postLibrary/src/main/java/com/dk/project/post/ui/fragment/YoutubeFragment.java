package com.dk.project.post.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.databinding.FragmentYoutubeBinding;
import com.dk.project.post.ui.activity.YoutubeFullScreenActivity;
import com.dk.project.post.utils.youtube.FullScreenHelper;
import com.dk.project.post.viewModel.YoutubeViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import org.jetbrains.annotations.NotNull;

/**
 * Created by dkkim on 2017-11-12.
 */

public class YoutubeFragment extends BindFragment<FragmentYoutubeBinding, YoutubeViewModel> {

    private boolean fullScreenButton;
    private boolean fullScreenMode;
    private String videoId;
    private boolean isLoading;

    private YouTubePlayer youTubePlayer;


    public int currentSeconds = 0;

    private FullScreenHelper fullScreenHelper;

    public static YoutubeFragment newInstance() {
        YoutubeFragment youtubeFragment = new YoutubeFragment();
        return youtubeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_youtube;
    }

    @Override
    protected YoutubeViewModel createViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(YoutubeViewModel.class);
    }

    @Override
    protected void registerLiveData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getLifecycle().addObserver(binding.youtubeLayout);

        fullScreenHelper = new FullScreenHelper(getActivity());

        videoId = getArguments().getString(YOUTUBE_VIDEO_ID);

//        binding.youtubeLayout.getPlayerUIController().showFullscreenButton(true);
//        binding.youtubeLayout.getPlayerUIController().showSeekBar(true);
//        binding.youtubeLayout.getPlayerUIController().showYouTubeButton(false);

        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.

        binding.youtubeLayout.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                YoutubeFragment.this.youTubePlayer = youTubePlayer;
                youTubePlayer.cueVideo(videoId, 0);
                addFullScreenListenerToPlayer(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                currentSeconds = (int) second;
            }
        });

        return view;
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        binding.youtubeLayout.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                fullScreenHelper.enterFullScreen();
//                addCustomActionToPlayer(youTubePlayer);

                Intent intent = new Intent(getActivity(), YoutubeFullScreenActivity.class);
//                Intent intent = new Intent(getActivity(), YoutubeNativeFullScreenActivity.class);
                intent.putExtra(YOUTUBE_VIDEO_ID, videoId);
                intent.putExtra(YOUTUBE_VIDEO_CURRENT_SECONDS, currentSeconds);
                startActivityForResult(intent, YOUTUBE_FULL_SCREEN);

                binding.youtubeLayout.exitFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
            }
        });
    }

    /**
     * This method adds a new custom action to the player. Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
//        Drawable customActionIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_36dp);
//
//        binding.youtubeLayout.getPlayerUIController().setCustomAction1(customActionIcon, view -> {
//            if (youTubePlayer != null) youTubePlayer.pause();
//        });
    }

    private void removeCustomActionFromPlayer() {
//        binding.youtubeLayout.getPlayerUIController().showCustomAction1(false);
    }


    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            youTubePlayer.loadVideo(videoId, 0);
        } else {
            youTubePlayer.cueVideo(videoId, 0);
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (player != null) {
//            player.release();
//        }
//    }

//    @Override
//    public void onFullscreen(boolean b) {
//        fullScreenMode = b;
//        if (fullScreenMode) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//    }

    public boolean isFullScreenMode() {
        return binding.youtubeLayout.isFullScreen();
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        if (fullScreenMode) {
            binding.youtubeLayout.enterFullScreen();
        } else {
            binding.youtubeLayout.exitFullScreen();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case YOUTUBE_FULL_SCREEN:
                    int currentSecond = data.getIntExtra(YOUTUBE_VIDEO_CURRENT_SECONDS, 0);
                    System.out.println("youtubePlaySecond  = " + currentSecond);
                    youTubePlayer.seekTo(currentSecond);
                    youTubePlayer.play();
                    break;
            }
        }
    }
}