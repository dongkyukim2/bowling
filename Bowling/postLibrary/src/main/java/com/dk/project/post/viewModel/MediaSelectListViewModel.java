package com.dk.project.post.viewModel;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.MediaSelectModel;
import com.dk.project.post.utils.ImageUtil;
import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MediaSelectListViewModel extends BaseViewModel {


  private MutableLiveData<ArrayList<MediaSelectListModel>> mediaMutableLiveData = new MutableLiveData<>();

  public MediaSelectListViewModel(@NonNull Application application) {
    super(application);
  }

  @Override
  public void onThrottleClick(View view) {

  }

  public void getMediaList(String bucketId, int type) {
    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.ORIENTATION,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
    };

    String selection = MediaStore.Images.Media.BUCKET_ID + " = ? ";
    String[] selectionArgs = {bucketId};
    String sortOrder = MediaStore.Images.Media._ID + " DESC";

    Cursor cursor = null;
    ArrayList<MediaSelectListModel> uriList = new ArrayList<>();

    try {
      switch (type) {
        case MediaSelectModel.ALBUM_ALL:
          cursor = mContext.getContentResolver().query(uri, projection, null, null, sortOrder);
          break;
        default:
          cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
          break;
      }
      if (cursor != null) {
        while (cursor.moveToNext()) {
          int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
          MediaSelectListModel item = new MediaSelectListModel();
          item.setCoverId(cursor.getString(columnIndex));
          item.setFilePath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
          item.setGif(item.getFilePath().toLowerCase().endsWith(".gif"));
          item.setWebp(item.getFilePath().toLowerCase().endsWith(".webp"));
          item.setOrientation(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)));
          item.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)));
          item.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)));
          if (ImageUtil.IGNORE_WEBP && item.getFilePath().toLowerCase().endsWith(".webp")) {
            continue;
          }
          uriList.add(item);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    mediaMutableLiveData.setValue(uriList);
  }

  public MutableLiveData<ArrayList<MediaSelectListModel>> getMediaMutableLiveData() {
    return mediaMutableLiveData;
  }
}
