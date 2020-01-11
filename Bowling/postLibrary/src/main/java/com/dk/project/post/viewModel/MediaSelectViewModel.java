package com.dk.project.post.viewModel;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.model.MediaSelectModel;
import com.dk.project.post.utils.ImageUtil;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MediaSelectViewModel extends BaseViewModel {

    private MutableLiveData<ArrayList<MediaSelectModel>> mediaSelectlistLiveData = new MutableLiveData<>();

    public MediaSelectViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public void getMediaList() {
        ArrayList<MediaSelectModel> mediaList = new ArrayList<>();
        Uri uri;
        String[] projection;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA};

        String sortOrder = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, sortOrder);
        int count = 0;
        ArrayList<String> ids = new ArrayList<>();
        mediaList.clear();
        if (cursor != null) {
            Long lastCoverId = 0l;
            String lastPath = "";
            while (cursor.moveToNext()) {
                MediaSelectModel model = new MediaSelectModel();

                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                String id = cursor.getString(columnIndex);
                model.setBucketId(id);

                columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long coverID = cursor.getLong(columnIndex);

                columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                String path = cursor.getString(columnIndex);
                if (ImageUtil.IGNORE_WEBP && path.toLowerCase().endsWith(".webp")) {
                    continue;
                }
                if (ImageUtil.IGNORE_GIF && path.toLowerCase().endsWith(".gif")) {
                    continue;
                }
                model.setPath(path);
                model.setGif(path.toLowerCase().endsWith(".gif"));

                count++;
                if (!ids.contains(id)) {

                    columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    String name = cursor.getString(columnIndex);
                    model.setName(name);

                    model.setCoverId(coverID);
                    ids.add(id);
                    model.increaseCount();
                    mediaList.add(model);
                } else {
                    mediaList.get(ids.indexOf(model.getBucketId())).increaseCount();
                    mediaList.get(ids.indexOf(model.getBucketId())).setCoverId(coverID);
                }
                if (TextUtils.isEmpty(lastPath)) {
                    lastCoverId = coverID;
                    lastPath = path;
                }

            }
            MediaSelectModel allAlbum = new MediaSelectModel();
            allAlbum.setName("전체보기");
            allAlbum.setAlbumCount(count);
            allAlbum.setType(MediaSelectModel.ALBUM_ALL);
            allAlbum.setCoverId(lastCoverId);
            allAlbum.setPath(lastPath);
            mediaList.add(0, allAlbum);
        }
        if (cursor != null) {
            cursor.close();
        }
        mediaSelectlistLiveData.setValue(mediaList);
    }

    public MutableLiveData<ArrayList<MediaSelectModel>> getMediaSelectlistLiveData() {
        return mediaSelectlistLiveData;
    }
}
