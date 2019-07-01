package com.dk.project.post.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.ui.fragment.YoutubeFragment;
import java.util.HashSet;


/**
 * Created by dkkim on 2017-11-12.
 */

public class YoutubeUtil implements Define {

  private static final HashSet<String> lowImageList = new HashSet<>();

  public static LinearLayout setYoutubeFragment(BaseActivity context, String videoId,
      View.OnClickListener onClickListener, boolean fullScreenButton) {
    LinearLayout youtubeRoot = new LinearLayout(context);
    youtubeRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    youtubeRoot.setOrientation(LinearLayout.VERTICAL);

    int spaceHeight = ScreenUtil.dpToPixel(18);
    LinearLayout topSpaceLayout = new LinearLayout(context);
    topSpaceLayout.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
    topSpaceLayout.setVisibility(View.GONE);
    if (onClickListener != null) {
      topSpaceLayout.setOnClickListener(onClickListener);
    }

    LinearLayout youtubeLayout = new LinearLayout(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    youtubeLayout.setLayoutParams(layoutParams);
    youtubeLayout.setId(View.generateViewId());

    youtubeRoot.addView(topSpaceLayout);
    youtubeRoot.addView(youtubeLayout);

    YoutubeFragment youtubeFragment = YoutubeFragment.newInstance();
    Bundle bundle = new Bundle();
    bundle.putString(YOUTUBE_VIDEO_ID, videoId);
    bundle.putBoolean("fullScreenButton", fullScreenButton);
    youtubeFragment.setArguments(bundle);
    FragmentManager fragmentManager = context.getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(youtubeLayout.getId(), youtubeFragment, YOUTUBE_FRAGMENT_TAG)
        .commitAllowingStateLoss();

    return youtubeRoot;
  }

  public static String getYoutubeThumbnailUrl(String videoUri) {
    return "https://i.ytimg.com/vi/" + videoUri + "/maxresdefault.jpg";
  }

  public static String getSecondYoutubeThumbnailUrl(String videoUri) {
    return "https://img.youtube.com/vi/" + videoUri + "/hqdefault.jpg";


//    return "https://i.ytimg.com/vi/" + videoUri + "/hqdefault.jpg";
  }

  public static LinearLayout getYoutubeThumbnail(Context context, String videoUri,
      View.OnClickListener onClickListener) {
    int[] size = ImageUtil.getResizeWidthHeight(context, 480, 270);
    return getYoutubeThumbnail(context, videoUri, size[0], size[1], onClickListener);
  }

  public static LinearLayout getYoutubeThumbnail(Context context, String videoUri, int width,
      int height, View.OnClickListener onClickListener) {

    int spaceHeight = ScreenUtil.dpToPixel(18);

    LinearLayout linearLayout = new LinearLayout(context);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    linearLayout.setLayoutParams(layoutParams);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    LinearLayout topSpaceLayout = new LinearLayout(context);
    topSpaceLayout.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
    topSpaceLayout.setOrientation(LinearLayout.VERTICAL);
//        topSpaceLayout.setBackgroundColor(Color.RED);
    topSpaceLayout.setVisibility(View.GONE);
    if (onClickListener != null) {
      topSpaceLayout.setOnClickListener(onClickListener);
    }

    ImageView imageView = new ImageView(context);
    imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    ImageView playIcon = new ImageView(context);
    int size = (int) (height / 4);
    RelativeLayout.LayoutParams playIconParam = new RelativeLayout.LayoutParams(size, size);
    playIconParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    playIcon.setLayoutParams(playIconParam);
    playIcon.setBackgroundResource(R.drawable.youtube_play);

    RelativeLayout relativeLayout = new RelativeLayout(context);
    relativeLayout.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    relativeLayout.addView(imageView);
    relativeLayout.addView(playIcon);

    linearLayout.addView(topSpaceLayout);
    linearLayout.addView(relativeLayout);

    setYoutubeThumbnail(context, imageView, videoUri);

    linearLayout.setTag(IMAGE_DIVIDER);
    return linearLayout;
  }


  // todo 유튜브에서 바로 첨부 후 다시 첨부 하면 첨부 안됨
  // todo 썸네일 없는거 로컬 디비에 저정하면 좋을듯
  private static void setYoutubeThumbnail(Context context, ImageView imageView, String videoUri) {
    GlideApp.with(context).load(getSecondYoutubeThumbnailUrl(videoUri)).thumbnail(0.1f).apply(ImageUtil.getGlideRequestOption()).into(imageView);
//    GlideApp.with(context).load(getYoutubeThumbnailUrl(videoUri)).thumbnail(0.1f).listener(new RequestListener<Drawable>() {
//      @Override
//      public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//        imageView.post(()->{
//          GlideApp.with(context).load(getSecondYoutubeThumbnailUrl(videoUri)).thumbnail(0.1f).apply(ImageUtil.getGlideRequestOption()).into(imageView);
//        });
//        return false;
//      }
//
//      @Override
//      public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//        return false;
//      }
//    }).into(imageView);
  }
}
