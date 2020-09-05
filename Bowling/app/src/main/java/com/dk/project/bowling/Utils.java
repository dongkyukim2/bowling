package com.dk.project.bowling;

import android.content.res.Resources;

public class Utils {


    public static float getScorePercent(int score) {
        return 1.0f - (score / 300.0f);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


//    public static void test(MainActivity context) {
//        new Thread(() -> {
//            try {
//                Bitmap bitmap = GlideApp.with(context).asBitmap().load("/storage/emulated/0/b.jpg").submit().get();
//                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//                FirebaseVision.getInstance()
//                        .getOnDeviceTextRecognizer()
//                        .processImage(image)
//                        .addOnSuccessListener(firebaseVisionText -> {
//                            for (FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()) {
//                                System.out.println("++++++++++    " + textBlock.getText());
//
//                            }
//                        })
//                        .addOnFailureListener(e -> {
//                            System.out.println("++++++++++    " + e.getLocalizedMessage());
//                        });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//
//
//        Uri sourceUri = Uri.fromFile(new File("/storage/emulated/0/a.jpg"));
//        Uri destinationUri = Uri.fromFile(new File("/storage/emulated/0/aa.jpg"));
//        UCrop.of(sourceUri, destinationUri)
//                .withMaxResultSize(1000, 1000)
//                .start(context);
//    }
}
