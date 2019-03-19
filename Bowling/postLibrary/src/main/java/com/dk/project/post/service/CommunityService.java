package com.dk.project.post.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseService;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.ProgressRequestBody.ProgressListener;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ToastUtil;
import java.util.concurrent.atomic.AtomicInteger;

public class CommunityService extends BaseService {

  private NotificationManager notifyManager;
  private Notification mNotification;
  private RemoteViews contentView;

  private final int notificationId = 8520;

  @Override
  public void onCreate() {
    super.onCreate();
    executeRx(RxBus.getInstance().registerRxObserver(pair -> {
      switch (pair.first) {
        case EVENT_WRITE_POST:
          uploadPostFile(pair, false);
          break;
        case EVENT_MODIFY_POST:
          uploadPostFile(pair, true);
          break;
      }
    }));
//    setForeground();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  // todo 업로드 중 다른 게시물 또 업로드 할때
  private void uploadPostFile(Pair<Integer, Object> postPair, boolean modify) {

    AtomicInteger uploadFileCount = new AtomicInteger(0);

    PostModel postModel = (PostModel) postPair.second;
    if (postModel.getImageList() != null) {
      for (MediaSelectListModel imageFile : postModel.getImageList()) {
        if (TextUtils.isEmpty(imageFile.getYoutubeUrl()) && !imageFile.getFilePath().startsWith("http")) {
          uploadFileCount.incrementAndGet();
        }
      }
    }

    if (uploadFileCount.get() == 0) {
      writePost(modify, postModel);
    } else {
      setForeground();

      AtomicInteger fileCount = new AtomicInteger(0);
      PostApi.getInstance().uploadFile(this, postModel, receivedData -> {
        writePost(modify, postModel);
      }, errorData -> {
        Toast.makeText(this, "업로드 실패", Toast.LENGTH_LONG).show();
      }, new ProgressListener() {
        @Override
        public void onUploadStart(String fileName) {
//          contentView.setTextViewText(R.id.noti_title, fileName);
          contentView.setTextViewText(R.id.noti_file_count, fileCount.incrementAndGet() + "/" + uploadFileCount.get());
          notifyManager.notify(notificationId, mNotification);
        }

        @Override
        public void onRequestProgress(long bytesWritten, long contentLength) {
          int progress = (int) ((double) bytesWritten / (double) contentLength * 100.0);
          contentView.setTextViewText(R.id.noti_file_percent, progress + "%");
          contentView.setProgressBar(R.id.noti_file_progress, (int) contentLength, (int) bytesWritten, false);
          notifyManager.notify(notificationId, mNotification);
        }

        @Override
        public void onUploadEnd(String fileName) {

        }
      });
    }
  }

  private void writePost(boolean modify, PostModel postModel) {
    executeRx(PostApi.getInstance().writePost(postModel, modify,
        writeReceivedData -> {
          if (modify) {
            RxBus.getInstance().eventPost(new Pair(EVENT_POST_REFRESH_MODIFY, writeReceivedData.getData()));
          } else {
            RxBus.getInstance().eventPost(new Pair(EVENT_POST_REFRESH, true));
          }
          ToastUtil.showUploadSuccessToast(this);
          stopSelf();
        },
        errorData -> Toast.makeText(this, "글쓰기 실패 성공", Toast.LENGTH_LONG).show()));
  }

  private void modifyPost(PostModel postModel) {
    executeRx(PostApi.getInstance().modifyPost(postModel,
        writeReceivedData -> {
          RxBus.getInstance().eventPost(new Pair(EVENT_POST_REFRESH_MODIFY, writeReceivedData.getData()));
          ToastUtil.showUploadSuccessToast(this);
        },
        errorData -> System.out.println("bbbbbbbbbb       글수정 에러")));
  }

  private void setForeground() {
    String channelId = "imageUploadChannel";
    String channelName = "게시물 업로드";

    notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

      // NotificationManager.IMPORTANCE_HIGH면 노팅에서 프로그래스 게이지 올라갈때 진동이 울리는 이슈
      int importance = NotificationManager.IMPORTANCE_LOW;
      NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
      notifyManager.createNotificationChannel(mChannel);

    }

    contentView = new RemoteViews(getPackageName(), R.layout.notification);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

    Intent notificationIntent = new Intent(getApplicationContext(), WriteActivity.class);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    ComponentName componentName = new ComponentName("com.dk.project.bowling", "com.dk.project.bowling.ui.activity.MainActivity");
    Intent intent = new Intent();
    intent.setComponent(componentName);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    builder.setSmallIcon(R.drawable.ic_cloud_upload_black_24dp)
        .setCustomContentView(contentView)
        .setContentIntent(PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));

    mNotification = builder.build();

    startForeground(notificationId, mNotification);
  }

}
