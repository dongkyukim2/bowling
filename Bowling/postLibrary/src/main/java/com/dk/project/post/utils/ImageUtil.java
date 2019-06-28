package com.dk.project.post.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dk.project.post.base.Define;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dkkim on 2017-10-05.
 */

public class ImageUtil implements Define {

    public static boolean IGNORE_WEBP = true;
    public static String imagePath;
    private static final String IMAGE_URL = "http://project-dk.iptime.org:8081/community/image/";
    private static final int SPACE_HEIGHT_INT = 22;
    private static GradientDrawable gradientDrawable;

    public static RequestOptions getGlideRequestOption() {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate();
        return options;
    }

    public static RequestOptions getGlideConterCropRequestOption() {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .optionalCenterCrop()
                .sizeMultiplier(0.6f);
        return options;
    }

    public static DraweeController getDraweeController(String filePath, int width, int height) {
        Uri imageUri = Uri.fromFile(new File(filePath));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .build();
        return animatedController;
    }

    public static int[] getResizeWidthHeight(Context context, int currentWidth, int currentHeight) {
        return getResizeWidthHeight(context, currentWidth, currentHeight, 0);
    }

    public static int[] getResizeWidthHeight(Context context, int currentWidth, int currentHeight,
                                             int space) {
        int maxWidth = ScreenUtil.getDeviceScreenSize(context)[0] - space;
        int[] size = new int[2];
        float rate = maxWidth / (float) currentWidth;
        int newWidth = maxWidth;
        int newHeight = (int) (currentHeight * rate);
        size[0] = newWidth;
        size[1] = newHeight;
        return size;
    }

    public static LinearLayout getImageViewThumbnail(Context context, int width, int height,
                                                     String filePath, View.OnClickListener onClickListener) {

        int spaceHeight = ScreenUtil.dpToPixel(SPACE_HEIGHT_INT);

        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout topSpaceLayout = new LinearLayout(context);
        topSpaceLayout.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
        topSpaceLayout.setOrientation(LinearLayout.VERTICAL);
//        topSpaceLayout.setBackgroundColor(Color.BLUE);
        topSpaceLayout.setVisibility(View.GONE);
        if (onClickListener != null) {
            topSpaceLayout.setOnClickListener(onClickListener);
        }

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        linearLayout.addView(topSpaceLayout);
        linearLayout.addView(imageView);

        if (new File(filePath).exists()) {
            Glide.with(context).asBitmap().load(filePath).thumbnail(0.2f).apply(getGlideRequestOption()).into(imageView);
        } else {
            Glide.with(context).asBitmap().load(IMAGE_URL + filePath).thumbnail(0.2f).apply(getGlideRequestOption()).into(imageView);
        }
        return linearLayout;
    }

    public static LinearLayout getImageGifThumbnail(Context context, int width, int height,
                                                    String filePath, boolean isResource, View.OnClickListener onClickListener) {
        int spaceHeight = ScreenUtil.dpToPixel(SPACE_HEIGHT_INT);

        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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

        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        simpleDraweeView.setLayoutParams(new LinearLayout.LayoutParams(width, height));


        Uri imageUri;
        if (isResource) {
            imageUri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(filePath)
                    .build();
        } else {
            File file = new File(filePath);
            if (file.exists()) {

                imageUri = Uri.fromFile(new File(filePath));// For files on device
            } else {
                imageUri = Uri.parse(IMAGE_URL + filePath);
            }
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(animatedController);

        linearLayout.addView(topSpaceLayout);
        linearLayout.addView(simpleDraweeView);

        return linearLayout;
    }

    public static Single<List<MediaSelectListModel>> compressImageObservable(Context context, PostModel postModel) {
        return Observable.fromIterable(postModel.getImageList()).
                filter(mediaSelectListModel -> {
                    if (TextUtils.isEmpty(mediaSelectListModel.getFilePath())) {
                        return false;
                    }
                    File file = new File(mediaSelectListModel.getFilePath());
                    return file.exists();
                }).
                map(selectFile -> {
                    String selectFilePath = selectFile.getFilePath();
                    String ext = FilenameUtils.getExtension(selectFilePath);
                    File file = new File(selectFilePath);
                    if (TextUtils.isEmpty(selectFile.getYoutubeUrl()) && !selectFile.isGif() && file.exists()) {
                        selectFile.setOriginalFileName(FilenameUtils.getName(selectFile.getFilePath()));

                        Bitmap bitmap = Glide.with(context).asBitmap().load(selectFilePath).submit(800, 800).get();
                        File destFile = new File(imagePath, UUID.randomUUID().toString() + "." + ext);
                        destFile.createNewFile();
                        OutputStream out = new FileOutputStream(destFile);
                        if (ext.equalsIgnoreCase("png")) {
                            bitmap.compress(CompressFormat.PNG, 100, out);
                        } else {
                            bitmap.compress(CompressFormat.JPEG, 100, out);
                        }
                        selectFile.setFilePath(destFile.getAbsolutePath());
                    }
                    return selectFile;
                }).toList();
    }

    public static void compressImage(Context context, ArrayList<MediaSelectListModel> fileList, Action onComplete) {
        Observable.fromArray(fileList).map(filepath -> {
            for (MediaSelectListModel file : fileList) {
                String ext = FilenameUtils.getExtension(file.getFilePath());
                if (TextUtils.isEmpty(file.getYoutubeUrl()) && !file.isGif() && !file.getFilePath().startsWith("http")) {
                    file.setOriginalFileName(FilenameUtils.getName(file.getFilePath()));
                    Bitmap bitmap = Glide
                            .with(context).asBitmap().load(file.getFilePath()).submit(800, 800).get();
                    File destFile = new File(imagePath, UUID.randomUUID().toString() + "." + ext);
                    destFile.createNewFile();
                    OutputStream out = new FileOutputStream(destFile);
                    if (ext.equals("png")) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    }
                    file.setFilePath(destFile.getAbsolutePath());
                }
            }
            return true;
        }).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.computation()).subscribe(s -> {
        }, throwable -> {
            System.out.println("cccccccccccc       " + throwable.getMessage());
        }, onComplete);
    }

    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static String getImagePath(Context context) {
        if (imagePath == null) {
            File tempDirectory = new File(context.getFilesDir().getAbsolutePath() + "/images");
            if (!tempDirectory.exists()) {
                boolean create = tempDirectory.mkdir();
            }
            imagePath = tempDirectory.getAbsolutePath();
        }
        return imagePath;
    }

    public static String getImagePath() {
        if (imagePath == null) {
            throw new NullPointerException("image path null");
        }
        return imagePath;
    }

    public static GradientDrawable getGradientDrawable() {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF38068f, 0xFF9b0493});
            gradientDrawable.setCornerRadius(80f);
        }
        return gradientDrawable;
    }

    public static GradientDrawable getGradientDrawable(float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF38068f, 0xFF9b0493});
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }
}

























