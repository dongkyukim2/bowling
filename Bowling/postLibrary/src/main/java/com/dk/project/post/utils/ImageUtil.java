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
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dk.project.post.R;
import com.dk.project.post.base.Define;
import com.dk.project.post.impl.DefaultCallBack;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.ui.widget.FrescoPercentTextView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.wasabeef.glide.transformations.BlurTransformation;
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;
import nl.bravobit.ffmpeg.FFtask;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by dkkim on 2017-10-05.
 */

public class ImageUtil implements Define {

    public static boolean IGNORE_WEBP = true;
    public static String imagePath;
    private static final int SPACE_HEIGHT_INT = 22;
    private static GradientDrawable gradientDrawable;

    public static RequestOptions getGlideRequestOption(DiskCacheStrategy strategy) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(strategy)
                .dontAnimate();
        return options;
    }

    public static RequestOptions getGlideRequestOption() {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
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
            GlideApp.with(context).asBitmap().load(filePath).thumbnail(0.2f).apply(getGlideRequestOption()).into(imageView);
        } else {
            GlideApp.with(context).asBitmap().load(IMAGE_URL + filePath).thumbnail(0.2f).apply(getGlideRequestOption()).into(imageView);
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

        SimpleDraweeView simpleDraweeView = null;
        FrescoPercentTextView percentTextView = new FrescoPercentTextView(context);

        if (!USE_GLIDE) {

            simpleDraweeView = new SimpleDraweeView(context);
            simpleDraweeView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

            ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable() {
                @Override
                protected boolean onLevelChange(int level) {
                    percentTextView.setProgress(level);
                    return super.onLevelChange(level);
                }
            };
            progressBarDrawable.setBarWidth(0);
            simpleDraweeView.getHierarchy().setProgressBarImage(progressBarDrawable);
        }

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

        linearLayout.addView(topSpaceLayout);

        if (USE_GLIDE) {
            AppCompatImageView gifImageView = new AppCompatImageView(context);
            gifImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            GlideApp.with(context).asGif().load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .centerCrop().into(gifImageView);
            linearLayout.addView(gifImageView);
        } else {

            RelativeLayout gifRootLayout = new RelativeLayout(context);
            gifRootLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));


            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setImageRequest(request)
                    .build();
            simpleDraweeView.setController(animatedController);

            gifRootLayout.addView(simpleDraweeView);
            gifRootLayout.addView(percentTextView);

            linearLayout.addView(gifRootLayout);
        }


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

                        Bitmap bitmap = GlideApp.with(context).asBitmap().load(selectFilePath).submit(800, 800).get();
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

    public static Single<List<MediaSelectListModel>> compressImage(Context context, ArrayList<MediaSelectListModel> fileList) {
        return Observable.fromIterable(fileList).filter(file -> {
            if (TextUtils.isEmpty(file.getFilePath()) ||
                    file.getFilePath().startsWith("http") ||
                    !new File(file.getFilePath()).exists()) {
                return false;
            }
            return true;
        }).map(selectFile -> {
            String selectFilePath = selectFile.getFilePath();
            String ext = FilenameUtils.getExtension(selectFilePath);
            File file = new File(selectFilePath);
            if (TextUtils.isEmpty(selectFile.getYoutubeUrl()) && !selectFile.isGif()) {
                selectFile.setOriginalFileName(FilenameUtils.getName(selectFile.getFilePath()));

                Bitmap bitmap = GlideApp.with(context).asBitmap().load(selectFilePath).submit(800, 800).get();
                File destFile = new File(imagePath, UUID.randomUUID().toString() + "." + ext);
                destFile.createNewFile();
                OutputStream out = new FileOutputStream(destFile);
                if (ext.equalsIgnoreCase("png")) {
                    bitmap.compress(CompressFormat.PNG, 70, out);
                } else {
                    bitmap.compress(CompressFormat.JPEG, 70, out);
                }
                selectFile.setFilePath(destFile.getAbsolutePath());
            }
            return selectFile;
        }).toList();
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

    public static GradientDrawable getGradientDrawable(Context context) {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{ContextCompat.getColor(context, R.color.startColor), ContextCompat.getColor(context, R.color.endColor)});
            gradientDrawable.setCornerRadius(80f);
        }
        return gradientDrawable;
    }

    public static GradientDrawable getGradientDrawable(Context context, float cornerRadius) {

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{ContextCompat.getColor(context, R.color.startColor), ContextCompat.getColor(context, R.color.endColor)});
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }

    public static void getClubColors(Context context, int resId, DefaultCallBack<Palette.Swatch> callback) {
        GlideApp.with(context).asBitmap().load(resId).apply(bitmapTransform(new BlurTransformation(25, 30))).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Palette.from(resource).generate(palette -> {
                    for (Palette.Swatch swatch : palette.getSwatches()) {
                        if (swatch != null) {
                            callback.callback(swatch);
                            break;
                        }
                    }

//                    Palette.Swatch a = palette.getVibrantSwatch();
//                    Palette.Swatch b = palette.getDarkVibrantSwatch();
//                    Palette.Swatch c = palette.getLightVibrantSwatch();
//                    Palette.Swatch d = palette.getMutedSwatch();
//                    Palette.Swatch e = palette.getDarkMutedSwatch();
//                    Palette.Swatch f = palette.getLightMutedSwatch();


                });
                return false;
            }
        }).submit();


//        getRgb(): Vibrant로 하셨다면 해당 이미지의 Vibrant에 해당하는 RGB색상값을 가져옵니다.
//        getTitleTextColor(): 위의 RGB색상값에 잘 어울리는 제목의 색상값을 가져옵니다.
//        getBodyTextColor(): 위의 RGB색상값에 잘 어울리는 본문의 색상값을 가져옵니다.
    }

    public static boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    public static void ffmpegTestTaskQuit(Context context) {

        if (FFmpeg.getInstance(context).isSupported()) {
            System.out.println("+++++++++   0000");
        } else {
            System.out.println("+++++++++   1111");
        }

        String[] command = {"-i", "/storage/emulated/0/a.gif", "/storage/emulated/0/a.webp"};


        final FFtask task = FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("+++++++++   onStart");
            }

            @Override
            public void onFinish() {
                System.out.println("+++++++++   onFinish");
            }

            @Override
            public void onSuccess(String message) {
                System.out.println("+++++++++   onSuccess    " + message);
            }

            @Override
            public void onProgress(String message) {
                System.out.println("+++++++++   onProgress    " + message);
            }

            @Override
            public void onFailure(String message) {
                System.out.println("+++++++++   onFailure    " + message);
            }
        });
    }
}