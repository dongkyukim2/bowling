package com.dk.project.post.ui.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

import com.dk.project.post.R;
import com.dk.project.post.base.Define;
import com.dk.project.post.databinding.FragmentContentsThumbItemBinding;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.ui.activity.ReadActivity;
import com.dk.project.post.ui.adapter.ContentsViewPagerAdapter;
import com.dk.project.post.ui.widget.CircleImageView;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.AnimationUtil;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.Utils;
import com.dk.project.post.viewModel.ContentListViewModel;

/**
 * Created by dkkim on 2017-03-26.
 */

public class ContentsThumbViewHolder<T extends PostModel> extends BaseContentsViewHolder<FragmentContentsThumbItemBinding, T> {

    private static int maxWidth;
    private static int maxHeight;
    private static int spaceWidth;
    private PostModel postModel;

    private Context mContext;
    private GestureDetectorCompat gestureDetectorCompat;
    private ContentsViewPagerAdapter contentsViewPagerAdapter;
    private ContentListViewModel contentListViewModel;

    private LayoutInflater layoutInflater;

    public ContentsThumbViewHolder(View itemView, ContentListViewModel contentListViewModel) {
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

        contentsViewPagerAdapter = new ContentsViewPagerAdapter(mContext);
        binding.attachViewPager.setOffscreenPageLimit(2);
        binding.attachViewPager.setAdapter(contentsViewPagerAdapter);
        binding.attachViewPager.getLayoutParams().height = maxHeight;
        binding.attachViewPager.invalidate();
        binding.attachViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                postModel.setCurrentPosition(position);
                AnimationUtil.setZoomAnimation(binding.pagerPositionText, (position + 1) + "/" + postModel.getImageList().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        gestureDetectorCompat = new GestureDetectorCompat(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                startReadActivity();
                return super.onSingleTapConfirmed(e);
            }
        });

        binding.attachViewPager.setOnTouchListener((v, event) -> {
            gestureDetectorCompat.onTouchEvent(event);
            return false;
        });

        itemView.setOnClickListener(v -> startReadActivity());

        binding.likeImageParent.setOnClickListener(v -> contentListViewModel.onLikeClick(binding.likeImageView, binding.likeCountText, postModel));

        binding.moreImageParent.setOnClickListener(v -> {
            AlertDialogUtil.showBottomSheetDialog(mContext, view -> {
                if (view.getId() == R.id.btnModify) {
                    startModify(mContext, postModel);
                } else if (view.getId() == R.id.btnDelete) {
                    startDelete(mContext, postModel);
                }
            });
        });

        //todo 계속 타는지 확인해 볼것
        layoutInflater.inflate(R.layout.reply_item, binding.replyRoot, true);
        layoutInflater.inflate(R.layout.reply_item, binding.replyRoot, true);

    }

    @Override
    public void onBindView(T postModel, int position) {
        this.postModel = postModel;
        String[] textArray = postModel.getInputText().split(IMAGE_DIVIDER);
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : textArray) {
            if (!str.trim().isEmpty()) {
                stringBuffer.append(str);
                stringBuffer.append(NEW_LINE);
            }
        }

        binding.setViewModel(contentListViewModel);
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            binding.contentsText.setText(stringBuffer.toString());
            binding.contentsText.setVisibility(View.VISIBLE);
        } else {
            binding.contentsText.setVisibility(View.GONE);
        }

        binding.userName.setText(postModel.getUserName());
        binding.userName.setSelected(true);

        binding.writeDate.setText(Utils.converterDate(postModel.getWriteDate()));

        binding.likeImageView.setImageResource(postModel.isLikeSelected() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);

        binding.replyCountText.setText(String.valueOf(postModel.getReplyCount()));
        binding.likeCountText.setText(String.valueOf(postModel.getLikeCount()));

        if (TextUtils.isEmpty(postModel.getUserProfile())) {
            GlideApp.with(mContext).load(R.drawable.user_profile)
                    .centerCrop()
                    .into(binding.userProfile);
        } else {
            GlideApp.with(mContext).applyDefaultRequestOptions(ImageUtil.getGlideRequestOption())
                    .load(Define.IMAGE_URL + postModel.getUserProfile())
                    .centerCrop()
                    .into(binding.userProfile);
        }

        if (binding.contentsText.getVisibility() == View.VISIBLE && postModel.getImageList().size() != 0) {
            binding.attachSpace.setVisibility(View.VISIBLE);
        } else {
            binding.attachSpace.setVisibility(View.GONE);
        }

        if (postModel.getImageList().isEmpty()) {
            binding.attachViewPagerParent.setVisibility(View.GONE);
        } else {
            binding.attachViewPagerParent.setVisibility(View.VISIBLE);
            contentsViewPagerAdapter.setImageList(postModel.getImageList());
            binding.attachViewPager.setAdapter(contentsViewPagerAdapter);
            binding.attachViewPager.setCurrentItem(postModel.getCurrentPosition());
        }

        if (postModel.getImageList().size() == 1) {
            binding.pagerPositionTextParent.setVisibility(View.INVISIBLE);
        } else if (postModel.getImageList().size() > 1) {
            binding.pagerPositionTextParent.setVisibility(View.VISIBLE);
            binding.pagerPositionText.setText((postModel.getCurrentPosition() + 1) + "/" + postModel.getImageList().size());
        }
        if (LoginManager.getInstance().isPermissionUser(postModel.getUserId()) == Define.OK) {
            binding.moreImageParent.setVisibility(View.VISIBLE);
        } else {
            binding.moreImageParent.setVisibility(View.INVISIBLE);
        }


//    if (postModel.getReplyList().isEmpty()) {
        binding.replyRoot.getChildAt(0).setVisibility(View.GONE);
        binding.replyRoot.getChildAt(1).setVisibility(View.GONE);
//    } else {
//      switch (postModel.getReplyList().size()) {
//        case 1:
//          binding.replyRoot.getChildAt(0).setVisibility(View.VISIBLE);
//          binding.replyRoot.getChildAt(1).setVisibility(View.GONE);
//          setReply(binding.replyRoot.getChildAt(0), postModel.getReplyList().get(0));
//          break;
//        default:
//          binding.replyRoot.getChildAt(0).setVisibility(View.VISIBLE);
//          binding.replyRoot.getChildAt(1).setVisibility(View.VISIBLE);
//          setReply(binding.replyRoot.getChildAt(0), postModel.getReplyList().get(0));
//          setReply(binding.replyRoot.getChildAt(1), postModel.getReplyList().get(1));
//          break;
//      }
//    }
    }

    private void setReply(View view, ReplyModel replyModel) {
        CircleImageView circleImageView = view.findViewById(R.id.reply_profile_image);
        TextView date = view.findViewById(R.id.reply_date_text);
        TextView name = view.findViewById(R.id.reply_user_name_text);
        TextView contents = view.findViewById(R.id.reply_contents_text);

        date.setText(Utils.converterDate(replyModel.getReplyDate()));
        name.setText(replyModel.getUserName());
        contents.setText(replyModel.getReplyContents());
        GlideApp.with(mContext).load(replyModel.getUserProfile()).apply(ImageUtil.getGlideRequestOption().placeholder(R.drawable.user_profile))
                .into(circleImageView);
    }

    public PostModel getPostModel() {
        return postModel;
    }

    @Override
    public void refreshWriteDate() {
        binding.writeDate.setText(Utils.converterDate(postModel.getWriteDate()));
    }

    private void startReadActivity() {
        Intent intent = new Intent(mContext, ReadActivity.class);
        intent.putExtra(POST_MODEL, postModel);
        mContext.startActivity(intent);
    }
}
