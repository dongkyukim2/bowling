package com.dk.project.post.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.databinding.ActivityWriteBinding;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.fragment.Camera2BasicFragment;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.TextViewUtil;
import com.dk.project.post.utils.YoutubeUtil;
import com.dk.project.post.viewModel.WriteViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class WriteActivity extends BindActivity<ActivityWriteBinding, WriteViewModel> implements View.OnClickListener,
        View.OnFocusChangeListener, TextViewUtil.OnKeyBoardBackKey {

    private PostModel writePostModel;
    private PostModel modifyPostModel;
    //  private String youtubeUrl;
//  private ArrayList<String> youtubeUrlList = new ArrayList<>();
    private EditText editText;
    private int keyBoardHeight;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean ignoreBackKeyFinish;
    private PublishSubject takePhotoSubject;
    private PublishSubject<Integer> cameraCallbackSubject;
    private Camera2BasicFragment camera2BasicFragment;
    private File picFile;
    private int cameraPaddind;

    private ArrayList<ClubModel> signClubList = new ArrayList<>();


    private View.OnClickListener editTextClick = view -> {
        Observable.timer(500, TimeUnit.MILLISECONDS).
                filter(aLong -> binding.rlBottomSheet.getVisibility() == View.VISIBLE).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(aLong -> cameraViewVisible(false));
        binding.keyboardSpace.setVisibility(View.VISIBLE);
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write;
    }

    @Override
    protected WriteViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(WriteViewModel.class);
    }

    @Override
    protected void subscribeToModel() {
        viewModel.getCameraClickEvent().observe(this, aBoolean -> { // 카메라 아이콘 선택시
            if (getImageCount() >= 5) {
                Toast.makeText(this, (IMAGE_LIMIT_COUNT + "개 이상 첨부 불가"), Toast.LENGTH_SHORT).show();
                return;
            }
            if (bottomSheetBehavior == null) {
                bottomSheetBehavior = BottomSheetBehavior.from(binding.rlBottomSheet);
                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                cameraViewVisible(false);
                                binding.keyboardSpace.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
            cameraViewVisible(true);
            binding.keyboardSpace.setVisibility(View.VISIBLE);
            TextViewUtil.hideKeyBoard(this, editText);
            if (camera2BasicFragment == null) {
                int[] size = ScreenUtil.getDeviceScreenSize(this);
                camera2BasicFragment = Camera2BasicFragment.newInstance();
                camera2BasicFragment.setMaxPreviewWidth(size[0]);
                camera2BasicFragment.setMaxPreviewHeight(size[1]);
                camera2BasicFragment.setOnCameraStateCallback(status -> cameraCallbackSubject.onNext(status));
                camera2BasicFragment.setOnTakePhotoCallback(file -> runOnUiThread(() -> { //todo 가로로 찍었을 때
                    try {
                        File tempFolder = new File(ImageUtil.imagePath);
                        File tempFile = new File(tempFolder.getAbsolutePath(), "pic_" + System.currentTimeMillis() + ".jpg");

                        boolean test = file.renameTo(tempFile);

                        ExifInterface exif = new ExifInterface(tempFile.getAbsolutePath());
                        int exifDegree = ImageUtil
                                .exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
                        int width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                        int height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);

                        MediaSelectListModel mediaSelectListModel = new MediaSelectListModel();
                        mediaSelectListModel.setWidth(width);
                        mediaSelectListModel.setHeight(height);
                        mediaSelectListModel.setOrientation(exifDegree);
                        mediaSelectListModel.setFilePath(tempFile.getAbsolutePath());
                        setAttachImage(mediaSelectListModel);

                        Toast.makeText(this, "사진~~~~~~~~", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.rlBottomSheet.getId(), camera2BasicFragment)
                        .commit();
            }
        });

        if (getIntent().hasExtra(POST_MODEL)) {
            modifyPostModel = getIntent().getParcelableExtra(POST_MODEL);
            setModifyPost(modifyPostModel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picFile = new File(getFilesDir().getAbsolutePath(), "pic.jpg");
        if (picFile.exists()) {
            picFile.delete();
        }

        writePostModel = new PostModel();

        binding.setViewModel(viewModel);

        editText = TextViewUtil.getEditText(this, editTextClick);
        editText.setHint("입력해주세요.");
        binding.inputParentView.addView(editText);

        viewModel.checkShare();

        toolbarTitle.setText("글쓰기");
        toolbarRightButton.setVisibility(View.VISIBLE);

        toolbarRightButton.setTranslationY(-ScreenUtil.dpToPixel(56));
        toolbarRightButton.animate().translationY(0).setDuration(300).setStartDelay(300);

        binding.writeRoot.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int temp = Math.abs(bottom - oldBottom);

            if (bottom > oldBottom && temp < 500) { // 하단 네비게이션바 숨겨졌을때
                cameraPaddind = temp;
                int[] size = ScreenUtil.getDeviceScreenSize(this);
                ViewGroup.LayoutParams params = binding.rlBottomSheet.getLayoutParams();
                params.width = size[0]; // 카메라 전체화면 일때 가로크기
                params.height = size[1] + cameraPaddind; // 카메라 전체화면 일때 세로크기

                binding.rlBottomSheet.invalidate();

            } else if (bottom < oldBottom && temp < 500) { // 하단 네비게이션바 보여질때
                cameraPaddind = 0;
                int[] size = ScreenUtil.getDeviceScreenSize(this);
                ViewGroup.LayoutParams params = binding.rlBottomSheet.getLayoutParams();
                params.width = size[0]; // 카메라 전체화면 일때 가로크기
                params.height = size[1] + cameraPaddind; // 카메라 전체화면 일때 세로크기

                binding.rlBottomSheet.invalidate();
            }

            if (oldBottom == 0 || temp < 500) {
                return;
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            keyBoardHeight = temp;
            binding.keyboardSpace.getLayoutParams().height = keyBoardHeight;
            binding.keyboardSpace.invalidate();
        });

        setTakePhotoSubject();

        setCameraCallbackSubject();

        binding.takePhotoButton.setOnClickListener(v -> takePhotoSubject.onNext(1));

        binding.writeRoot.postDelayed(() -> TextViewUtil.showKeyBoard(this, (AppCompatEditText) binding.inputParentView.getChildAt(0)), 500);
    }

    private void setModifyPost(PostModel modifyPostModel) {
        binding.inputParentView.removeAllViews();
        int imageIndex = 0;
        String inputString = modifyPostModel.getInputText();

        while (true) {
            if (inputString.startsWith(IMAGE_DIVIDER)) {
                int endIndex = IMAGE_DIVIDER.length();

                MediaSelectListModel model = modifyPostModel.getImageList().get(imageIndex);

                LinearLayout imageLayout;
                if (TextUtils.isEmpty(model.getYoutubeUrl())) {
                    int[] temp = ImageUtil.getResizeWidthHeight(this, model.getConvertWidth(), model.getConvertHeight());

                    if (model.isGif() || model.isWebp()) {
                        imageLayout = ImageUtil.getImageGifThumbnail(this, temp[0], temp[1], model.getFilePath(), false, this);
                    } else {
                        imageLayout = ImageUtil.getImageViewThumbnail(this, temp[0], temp[1], model.getFilePath(), this);
                    }
                    imageLayout.setTag(model);

                    imageLayout.setOnClickListener(view -> {
                        AlertDialogUtil.showAlertDialog(this, "알림", "선택한 이미지를 삭제 하시겠습니까?", (dialog, which) -> {
                            editTextSum(view);
                            binding.inputParentView.removeView(view);
                            setImageMargin();
                        });
                    });
                } else {
                    imageLayout = getYoutubeLayout(model.getYoutubeUrl());
                }

                binding.inputParentView.addView(imageLayout);

                inputString = inputString.substring(endIndex);
                imageIndex++;
                if (inputString.isEmpty()) {
                    break;
                }
            } else {
                int endIndex = inputString.indexOf(IMAGE_DIVIDER, 0);
                if (endIndex == -1) { //뒤로 이미지 없는경우
                    binding.inputParentView.addView(TextViewUtil.getEditText(this, inputString, editTextClick));
                    break;
                } else {
                    String tempString = inputString.substring(0, endIndex).trim();
                    binding.inputParentView.addView(TextViewUtil.getEditText(this, tempString, editTextClick));
                    inputString = inputString.substring(endIndex);
                }
                if (inputString.isEmpty()) {
                    break;
                }
            }
        }

        View tempView = binding.inputParentView.getChildAt(0);
        if (tempView instanceof AppCompatEditText) {
            ((AppCompatEditText) tempView).setHint("입력해주세요.");
        } else {
            AppCompatEditText editText = TextViewUtil.getEditText(this, "", editTextClick);
            editText.setHint("입력해주세요.");
            binding.inputParentView.addView(editText, 0);
        }
        addLastEditText();

        viewModel.setImageViewPadding(binding.inputParentView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        viewModel.checkShare();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MEDIA_ATTACH_LIST:
                if (getImageCount() + ListController.getInstance().getMediaSelectList().size() > 5) {
                    Toast.makeText(this, (IMAGE_LIMIT_COUNT + "개 이상 첨부 불가"), Toast.LENGTH_SHORT).show();
                    return;
                }
                setAttachImage(null);
                break;
        }
    }


    public void setAttachImage(MediaSelectListModel file) {
        ArrayList<MediaSelectListModel> tempList = new ArrayList<>();
        if (file == null) {
            tempList.addAll(ListController.getInstance().getMediaSelectList());
        } else {
            tempList.add(file);
        }
        Pair<Integer, AppCompatEditText> pair = editTextSub(false);
        int insertIndex = pair.first;
        AppCompatEditText insertEditText = pair.second;

        for (MediaSelectListModel mediaSelectListModel : tempList) {
            if (mediaSelectListModel.getWidth() == 0) { // 이미지 크기 가져오지 못하는 경우 있음
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mediaSelectListModel.getFilePath(), options);
                mediaSelectListModel.setWidth(options.outWidth);
                mediaSelectListModel.setHeight(options.outHeight);
            }

            int[] temp = ImageUtil.getResizeWidthHeight(this, mediaSelectListModel.getConvertWidth(), mediaSelectListModel.getConvertHeight());
            LinearLayout imageLayout;
            if (mediaSelectListModel.isGif() || mediaSelectListModel.isWebp()) {
                imageLayout = ImageUtil.getImageGifThumbnail(this, temp[0], temp[1], mediaSelectListModel.getFilePath(), false, this);
            } else {
                imageLayout = ImageUtil.getImageViewThumbnail(this, temp[0], temp[1], mediaSelectListModel.getFilePath(), this);
            }
            imageLayout.setTag(mediaSelectListModel);

            imageLayout.setOnClickListener(view -> {
                AlertDialogUtil.showAlertDialog(this, "알림", "선택한 이미지를 삭제 하시겠습니까?", (dialog, which) -> {
                    editTextSum(view);
                    binding.inputParentView.removeView(view);
                    setImageMargin();
                });
            });

            binding.inputParentView.addView(imageLayout, ++insertIndex);
        }
        if (insertEditText != null && !insertEditText.getText().toString().isEmpty()) {
            binding.inputParentView.addView(insertEditText, ++insertIndex);
        }

        View tempView = binding.inputParentView.getChildAt(binding.inputParentView.getChildCount() - 1); // 마지막 EditText 추가
        if (!(tempView instanceof AppCompatEditText)) {
            binding.inputParentView.addView(TextViewUtil.getEditText(this, editTextClick));
        }
        viewModel.setImageViewPadding(binding.inputParentView);
    }

    @Override
    protected void onPause() {
        binding.keyboardSpace.setVisibility(View.GONE);
        cameraViewVisible(false);
        super.onPause();
    }

    @Override
    public void finish() {
        TextViewUtil.hideKeyBoard(this, editText);
        binding.writeRoot.animate()
                .translationY(ScreenUtil.getDeviceScreenSize(this)[1])
                .setStartDelay(400)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isPrevActivity("MainActivity")
                                && !isPrevActivity("ReadActivity")
                                && !isPrevActivity("ClubDetailActivity")) {
                            ComponentName compoentName = new ComponentName("com.dk.project.bowling", "com.dk.project.bowling.ui.activity.IntroActivity");
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setComponent(compoentName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(Define.INTRO_DELAY, true);
                            startActivity(intent);
                        }
                        WriteActivity.super.finish();
                        overridePendingTransition(0, 0);
                    }
                });
    }

    public void setYoutubeFragment(String url) {
        Pair<Integer, AppCompatEditText> pair = editTextSub(true);
        int insertIndex = pair.first;
        AppCompatEditText insertEditText = pair.second;

        binding.inputParentView.addView(getYoutubeLayout(url), ++insertIndex);

        if (insertEditText != null && !insertEditText.getText().toString().isEmpty()) {
            binding.inputParentView.addView(insertEditText, ++insertIndex);
        }

        addLastEditText();

        viewModel.setImageViewPadding(binding.inputParentView);
    }

    @Override
    public void onToolbarRightClick() {
        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            Toast.makeText(this, "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(viewModel.getClubId())) {
            if (signClubList.isEmpty()) {
                finishPost();
            } else {
                ClubModel open = new ClubModel();
                open.setClubTitle("공개");
                signClubList.add(0, open);
                CharSequence[] items = new CharSequence[signClubList.size()];
                for (int i = 0; i < signClubList.size(); i++) {
                    ClubModel clubModel = signClubList.get(i);
                    items[i] = clubModel.getClubTitle();
                }
                AlertDialogUtil.showListAlertDialog(this, "클럽 선택", items, (dialog, which) -> {
                    viewModel.setClubId(signClubList.get(which).getClubId());
                    finishPost();
                });
            }
        } else {
            finishPost();
        }
    }

    @Override
    public void onClick(View view) { // 이미지 공백 클릭시 사이에 텍스트 뷰 추가
        int clickIndex = 0;
        LinearLayout clickImageViewParent = (LinearLayout) view.getParent();
        for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
            View tempView = binding.inputParentView.getChildAt(i);
            if (clickImageViewParent == tempView) {
                clickIndex = i;
                break;
            }
        }
        clickImageViewParent.getChildAt(0).setVisibility(View.GONE);
        AppCompatEditText editText = TextViewUtil.getEditText(this, editTextClick);
        binding.inputParentView.addView(editText, clickIndex);
        binding.inputParentView.post(() -> {
            binding.keyboardSpace.setVisibility(View.VISIBLE);
            TextViewUtil.showKeyBoard(this, editText);
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            binding.keyboardSpace.setVisibility(View.VISIBLE);
            cameraViewVisible(false);
        }
        if (!hasFocus && ((AppCompatEditText) v).getText().toString().isEmpty()) {
            ViewGroup rootView = (ViewGroup) v.getParent();
            if (rootView.getChildAt(0) != v && rootView.getChildAt(rootView.getChildCount() - 1) != v) {
                rootView.removeView(v);
            }
            viewModel.setImageViewPadding(rootView);
        }
    }

    @Override
    public void onEditTextBackPress() {
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                ignoreBackKeyFinish = true;
                return;
            } else if (/*bottomSheetBehavior.getPeekHeight() != 0*/bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                cameraViewVisible(false);
                ignoreBackKeyFinish = true;
            }
        }
        if (binding.keyboardSpace.getVisibility() == View.VISIBLE) {
            binding.keyboardSpace.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (ignoreBackKeyFinish) {
            ignoreBackKeyFinish = false;
            return;
        }
        super.onBackPressed();
    }

    private void finishPost() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
            View view = binding.inputParentView.getChildAt(i);
            if (view instanceof LinearLayout) {
                MediaSelectListModel mediaTag = (MediaSelectListModel) view.getTag();
//                if (tag instanceof String) {
//                    writePostModel.getImageList().add(new MediaSelectListModel(youtubeUrl.replace(YOUTUBE_SHARE_URL, "")));
//                    stringBuffer.append(IMAGE_DIVIDER);
//                } else {
                writePostModel.getImageList().add(mediaTag);
                stringBuffer.append(IMAGE_DIVIDER);
//                }
            } else {
                String inputText = ((AppCompatEditText) view).getText().toString().trim();
                if ((i == 0 || i == binding.inputParentView.getChildCount() - 1) && TextUtils.isEmpty(inputText)) {
                    continue;
                }
                stringBuffer.append(inputText);
            }
        }
        if (stringBuffer.toString().isEmpty()) {
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            writePostModel.getImageList().clear();
            return;
        }
        writePostModel.setInputText(stringBuffer.toString());

        if (modifyPostModel != null) {
            writePostModel.setPostId(modifyPostModel.getPostId());
            writePostModel.setIdx(modifyPostModel.getIdx());
            writePostModel.setWriteDate(modifyPostModel.getWriteDate());
            viewModel.modifyPost(writePostModel);
            setResult(RESULT_OK);
        } else {
            viewModel.writePost(writePostModel);
        }
        finish();
    }

    private void addLastEditText() {
        View tempView = binding.inputParentView.getChildAt(binding.inputParentView.getChildCount() - 1); // 마지막 EditText 추가
        if (!(tempView instanceof AppCompatEditText)) {
            binding.inputParentView.addView(TextViewUtil.getEditText(this, editTextClick));
        }
    }

    private LinearLayout getYoutubeLayout(String url) {

        String videoId = url.replace(YOUTUBE_SHARE_URL, "");

        MediaSelectListModel selectListModel = new MediaSelectListModel();
        selectListModel.setYoutubeUrl(videoId);

        LinearLayout youtubeLayout = YoutubeUtil.getYoutubeThumbnail(this, videoId, this);
        youtubeLayout.setTag(selectListModel);

        youtubeLayout.getChildAt(1)
                .setOnClickListener(view -> AlertDialogUtil.showAlertDialog(WriteActivity.this, "알림", "선택한 동영상를 삭제 하시겠습니까?", (dialog, which) -> {
                    editTextSum(view);
                    binding.inputParentView.removeView((View) view.getParent());
                }));

        return youtubeLayout;
    }

    private void setTakePhotoSubject() {
        takePhotoSubject = PublishSubject.create();
        takePhotoSubject.
                throttleFirst(3, TimeUnit.SECONDS).
                map(o -> {
                    int value = 0;
                    if (getImageCount() >= 5) {
                        Toast.makeText(this, (IMAGE_LIMIT_COUNT + "개 이상 첨부 불가"), Toast.LENGTH_SHORT).show();
                    } else {
                        value = 1;
                    }
                    return value;
                }).observeOn(AndroidSchedulers.mainThread()).
                filter(value -> (int) value == 0 ? false : true).
                debounce(300, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (camera2BasicFragment != null) {
                        camera2BasicFragment.takePicture();
                    }
                });
    }

    private void setCameraCallbackSubject() {
        cameraCallbackSubject = PublishSubject.create();
        cameraCallbackSubject.
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(integer -> {
                    switch (integer) {
                        case 0:
                            if (bottomSheetBehavior.getPeekHeight() != 0) {
                                binding.takePhotoButton.setVisibility(View.VISIBLE);
                            }
                            break;
                        default:
                            binding.takePhotoButton.setVisibility(View.GONE);
                            break;
                    }
                });
    }

    private void cameraViewVisible(boolean visible) {
        if (visible) {
            if (bottomSheetBehavior != null) {
                if (bottomSheetBehavior.getPeekHeight() == 0) {
                    bottomSheetBehavior.setPeekHeight(keyBoardHeight);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        } else {
            if (bottomSheetBehavior != null && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            binding.takePhotoButton.setVisibility(View.GONE);
            binding.takePhotoButton.postDelayed(() -> {
                if (camera2BasicFragment != null) {
                    getSupportFragmentManager().beginTransaction().detach(camera2BasicFragment).commitAllowingStateLoss();
                    camera2BasicFragment = null;
                }
            }, 400);
        }
    }

    private void editTextSum(View view) { // 지웠을때 edittext 글씨 합치기
        for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
            if (binding.inputParentView.getChildAt(i) == view) {
                View prevView = binding.inputParentView.getChildAt(i - 1);
                View nextView = binding.inputParentView.getChildAt(i + 1);
                if (prevView instanceof AppCompatEditText && nextView instanceof AppCompatEditText) {
                    AppCompatEditText prevEditText = (AppCompatEditText) prevView;
                    AppCompatEditText nextEditText = (AppCompatEditText) nextView;
                    if (!nextEditText.getText().toString().trim().isEmpty()) {
                        if (prevEditText.getText().toString().trim().isEmpty()) {
                            prevEditText.setText(nextEditText.getText());
                        } else {
                            prevEditText.getText().append(NEW_LINE + nextEditText.getText());
                        }
                    }
                    binding.inputParentView.removeView(nextEditText);
                }
                break;
            }
        }
    }

    private Pair<Integer, AppCompatEditText> editTextSub(boolean isYoutube) { // 추가했을 때 글씨 나누기
        if (isYoutube) {
            for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
                View view = binding.inputParentView.getChildAt(i);
                if (view.getTag() != null && TextUtils.equals(view.getTag().toString(), YOUTUBE_PREVIEW_TAG)) {
                    binding.inputParentView.removeView(view);
                    break;
                }
            }
        }
        int insertIndex = 0;
        AppCompatEditText insertEditText = null;
        for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
            View view = binding.inputParentView.getChildAt(i);
            if (view instanceof AppCompatEditText && view.isFocused()) {
                insertIndex = i;
                AppCompatEditText editText = (AppCompatEditText) view;
                int index = editText.getSelectionStart();
                Editable tempEditable = editText.getText();
                String tempString = tempEditable.toString().substring(index);
                if (tempString.startsWith(NEW_LINE)) {
                    tempString = tempString.substring(1);
                }
                tempEditable.delete(index, tempEditable.length());
                insertEditText = TextViewUtil.getEditText(this, tempString, editTextClick);
                break;
            }
        }
        return new Pair<>(insertIndex, insertEditText);
    }

    private void setImageMargin() {
        for (int i = 1; i < binding.inputParentView.getChildCount(); i++) {
            View prevView = binding.inputParentView.getChildAt(i - 1);
            View currentView = binding.inputParentView.getChildAt(i);
            if (prevView instanceof LinearLayout && currentView instanceof LinearLayout) {
                ((LinearLayout) currentView).getChildAt(0).setVisibility(View.VISIBLE);
            } else if (prevView instanceof LinearLayout && currentView instanceof AppCompatEditText) {

            } else if (prevView instanceof AppCompatEditText && currentView instanceof LinearLayout) {
                ((LinearLayout) currentView).getChildAt(0).setVisibility(View.GONE);
            }
        }
    }

    private int getImageCount() {
        int count = 0;
        for (int i = 0; i < binding.inputParentView.getChildCount(); i++) {
            View mediaView = binding.inputParentView.getChildAt(i);
            if (mediaView instanceof LinearLayout && TextUtils.isEmpty(((MediaSelectListModel) mediaView.getTag()).getYoutubeUrl())) {
                count++;
            }
        }
        return count;
    }

    private void isLogin() {
        // todo 로그인되어있으면 열려있는듯
        if (KakaoLoginUtils.checkLogin()) {
            KakaoLoginUtils.getUserInfo(new MeV2ResponseCallback() {
                @Override
                public void onSuccess(MeV2Response result) {
                    long userKakaoCode = result.getId();
                    viewModel.executeRx(PostApi.getInstance().getUserInfo(String.valueOf(userKakaoCode),
                            receivedData -> {
                                if (receivedData.getData() == null) {
                                } else { // 세션 열려있고 디비에 가입도 되어있음
                                    LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
                                    BowlingApi.getInstance().getSignUpClubList(receiveCignClubList -> {
                                        signClubList.clear();
                                        signClubList.addAll(receiveCignClubList.getData());
                                    }, errorData -> {
                                    });
                                }
                            }, errorData -> {
                            }));
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }
            });
        }
    }
}
