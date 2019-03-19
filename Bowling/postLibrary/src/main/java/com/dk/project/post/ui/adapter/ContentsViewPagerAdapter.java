package com.dk.project.post.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.dk.project.post.R;
import com.dk.project.post.base.Define;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.YoutubeUtil;
import java.util.ArrayList;

/**
 * Created by dkkim on 2017-12-25.
 */

public class ContentsViewPagerAdapter extends PagerAdapter implements Define {

  private Context mContext;
  private ArrayList<MediaSelectListModel> imageList;
  private static int maxWidth;
  private static int maxHeight;
  private static int widthSpace;

  public ContentsViewPagerAdapter(Context context) {
    mContext = context;
    if (maxHeight == 0) {

      widthSpace = ScreenUtil.dpToPixel((int) context.getResources().getDimension(R.dimen.content_list_view_holder_width_margin));
      int[] size = ScreenUtil.getDeviceScreenSize(mContext);
      maxWidth = size[0] - widthSpace;
      maxHeight = (int) (maxWidth * CONTENT_VIEW_HOLDER_RATIO);
    }
  }

  public void setImageList(ArrayList<MediaSelectListModel> imageList) {
    this.imageList = imageList;
  }

  @Override
  public int getCount() {
    return imageList == null ? 0 : imageList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { //instantiateItem 에서 생성한 객체를 사용할지 여부를 판단 합니다 .
    return view == object;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    LinearLayout view = getContentView(imageList.get(position));
    container.addView(view);
    return view;
//        return super.instantiateItem(container, position);
  }

  @Override
  public int getItemPosition(@NonNull Object object) {
    return super.getItemPosition(object);
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    container.removeView((LinearLayout) object);
  }


  private LinearLayout getContentView(MediaSelectListModel imageModel) {
    if (!TextUtils.isEmpty(imageModel.getYoutubeUrl())) {
      return YoutubeUtil.getYoutubeThumbnail(mContext, imageModel.getYoutubeUrl(), maxWidth, maxHeight, null);
    } else {
      return addImageView(imageModel, maxWidth, maxHeight);
    }
  }

  private LinearLayout addImageView(MediaSelectListModel imageModel, int width, int height) {
    if (imageModel.isGif() || imageModel.isWebp()) {
      return ImageUtil.getImageGifThumbnail(mContext, width, Math.min(maxHeight, height), imageModel.getFilePath(), false, null);
    } else {
      return ImageUtil.getImageViewThumbnail(mContext, width, Math.min(maxHeight, height), imageModel.getFilePath(), null);
    }
  }
}
