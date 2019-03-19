package com.dk.project.post.ui.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.app.ActivityOptionsCompat;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.FragmentContentsListItemBinding;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.ui.activity.ReadActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.Utils;
import com.dk.project.post.utils.YoutubeUtil;
import com.dk.project.post.viewModel.ContentListViewModel;

/**
 * Created by dkkim on 2017-03-26.
 */

public class ContentsListViewHolder<T extends PostModel> extends BaseContentsViewHolder<FragmentContentsListItemBinding, T> {

  private static int maxWidth;
  private static int maxHeight;
  private static int spaceWidth;
  private PostModel postModel;

  private Context mContext;
  //    private GestureDetectorCompat gestureDetectorCompat;
  private LayoutInflater layoutInflater;
  private ContentListViewModel contentListViewModel;
  //    private ContentsViewPagerAdapter contentsViewPagerAdapter;
//    private ContentsListener contentsListener;

  public ContentsListViewHolder(View itemView, ContentListViewModel contentListViewModel) {
    super(itemView);
    mContext = itemView.getContext();
    layoutInflater = LayoutInflater.from(mContext);
    this.contentListViewModel = contentListViewModel;
    if (maxHeight == 0) {
      int[] size = ScreenUtil.getDeviceScreenSize(mContext);
      maxWidth = size[0] - ScreenUtil.dpToPixel(16);
      maxHeight = (int) (maxWidth * CONTENT_VIEW_HOLDER_RATIO);
    }
    if (spaceWidth == 0) {
      spaceWidth = ScreenUtil.dpToPixel(6);
    }

//        contentsViewPagerAdapter = new ContentsViewPagerAdapter(mContext);
//        binding.attachViewPager.setOffscreenPageLimit(2);
//        binding.attachViewPager.setAdapter(contentsViewPagerAdapter);
//        binding.attachViewPager.getLayoutParams().height = maxHeight;
//        binding.attachViewPager.invalidate();
//        binding.attachViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                postModel.setCurrentPosition(position);
//                AnimationUtil.setZoomAnimation(binding.pagerPositionText, (position + 1) + "/" + postModel.getImageList().size());
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//            }
//        });

//        gestureDetectorCompat = new GestureDetectorCompat(mContext, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                startReadActivity();
//                return super.onSingleTapConfirmed(e);
//            }
//        });

//        binding.attachViewPager.setOnTouchListener((v, event) -> {
//            gestureDetectorCompat.onTouchEvent(event);
//            return false;
//        });
    itemView.setOnClickListener(v -> startReadActivity());
    itemView.setOnLongClickListener(v -> {
      AlertDialogUtil.showListAlertDialog(mContext, null, new String[]{"수정하기", "삭제하기"}, (dialog, which) -> {
        switch (which) {
          case 0:
            startModify(mContext, postModel);
            break;
          case 1:
            startDelete(mContext, postModel);
            break;
        }
      });
      return true;
    });

    binding.likeImageParent.setOnClickListener(v -> contentListViewModel.onLikeClick(binding.likeImageView, binding.likeCountText, postModel));
  }

  @Override
  public void onBindView(T postModel) {
    this.postModel = postModel;
    String[] textArray = postModel.getInputText().split(IMAGE_DIVIDER);
    StringBuffer stringBuffer = new StringBuffer();
    for (String str : textArray) {
      if (!str.trim().isEmpty()) {
        stringBuffer.append(str);
        stringBuffer.append(NEW_LINE);
      }
    }
    if (stringBuffer.length() > 0) {
      stringBuffer.deleteCharAt(stringBuffer.length() - 1);
      binding.contentsText.setText(stringBuffer.toString());
//            binding.contentsText.setVisibility(View.VISIBLE);
    } else {
      binding.contentsText.setText("내용없음");
//            binding.contentsText.setVisibility(View.GONE);
    }

    binding.userName.setText(postModel.getUserName());

    binding.writeDate.setText(Utils.converterDate(postModel.getWriteDate()));

    binding.likeImageView.setImageResource(postModel.isLikeSelected() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);

    binding.replyCountText.setText(String.valueOf(postModel.getReplyCount()));
    binding.likeCountText.setText(String.valueOf(postModel.getLikeCount()));

//        Glide.with(mContext).load(postModel.getUserProfile()).apply(ImageUtil.getGlideRequestOption().placeholder(R.drawable.ic_user_profile)).into(binding.userProfile);

    if (binding.contentsText.getVisibility() == View.VISIBLE && postModel.getImageList().size() != 0) {
      binding.titleImage.setVisibility(View.VISIBLE);
      binding.titleImage.removeAllViews();
      binding.titleImage.addView(getTest(postModel.getImageList().get(0)));
    } else {
      binding.titleImage.setVisibility(View.GONE);
    }

//        if (postModel.getImageList().isEmpty()) {
//            binding.attachViewPagerParent.setVisibility(View.GONE);
//        } else {
//            binding.attachViewPagerParent.setVisibility(View.VISIBLE);
//            contentsViewPagerAdapter.setImageList(postModel.getImageList());
//            binding.attachViewPager.setAdapter(contentsViewPagerAdapter);
//            binding.attachViewPager.setCurrentItem(postModel.getCurrentPosition());
//        }

//        if (postModel.getImageList().size() == 1) {
//            binding.pagerPositionTextParent.setVisibility(View.INVISIBLE);
//        } else if (postModel.getImageList().size() > 1) {
//            binding.pagerPositionTextParent.setVisibility(View.VISIBLE);
//            binding.pagerPositionText.setText((postModel.getCurrentPosition() + 1) + "/" + postModel.getImageList().size());
//        }
  }

  private LinearLayout getTest(MediaSelectListModel imageModel) { //todo 중복 함수
    int imageSize = ScreenUtil.dpToPixel(50);
    if (!TextUtils.isEmpty(imageModel.getYoutubeUrl())) {
      return YoutubeUtil.getYoutubeThumbnail(mContext, imageModel.getYoutubeUrl(), imageSize, imageSize, null);
    } else {
      return addImageView(imageModel, imageSize, imageSize);
    }
  }

  private LinearLayout addImageView(MediaSelectListModel imageModel, int width, int height) { //todo 중복 함수
    if (imageModel.isGif() || imageModel.isWebp()) {
      return ImageUtil.getImageGifThumbnail(mContext, width, Math.min(maxHeight, height), imageModel.getFilePath(), false, null);
    } else {
      return ImageUtil.getImageViewThumbnail(mContext, width, Math.min(maxHeight, height), imageModel.getFilePath(), null);
    }
  }

//    private void setReply(View view, ReplyModel replyModel) {
//        CircleImageView circleImageView = view.findViewById(R.id.reply_profile_image);
//        TextView date = view.findViewById(R.id.reply_date_text);
//        TextView name = view.findViewById(R.id.reply_user_name_text);
//        TextView contents = view.findViewById(R.id.reply_contents_text);
//
//        date.setText(Utils.converterDate(replyModel.getReplyDate()));
//        name.setText(replyModel.getUserName());
//        contents.setText(replyModel.getReplyContents());
//        Glide.with(mContext).load(replyModel.getUserProfile()).apply(ImageUtil.getGlideRequestOption().placeholder(R.drawable.ic_user_profile)).into(circleImageView);
//    }

  public PostModel getPostModel() {
    return postModel;
  }

  @Override
  public void refreshWriteDate() {
    binding.writeDate.setText(Utils.converterDate(postModel.getWriteDate()));
  }

  private void startReadActivity() {
    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((BindActivity) mContext/*,
                new Pair<>(binding.userProfile, "profile"),
                new Pair<>(binding.userName, "userName"),
                new Pair<>(binding.writeDate, "writeDate")
                new Pair<>(binding.likeImageView, "likeImage"),
                new Pair<>(binding.replyImageView, "replyImage"),
                new Pair<>(binding.moreImageView, "moreImage"),
                new Pair<>(binding.likesCountImage, "likeCountImage"),
                new Pair<>(binding.likesCount, "likeCount"),
                new Pair<>(binding.contentRoot, "contents")*/);

    Intent intent = new Intent(mContext, ReadActivity.class);
    intent.putExtra(POST_MODEL, postModel);
    mContext.startActivity(intent, options.toBundle());
  }
}
