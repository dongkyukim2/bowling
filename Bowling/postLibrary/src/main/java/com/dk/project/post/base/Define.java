package com.dk.project.post.base;

/**
 * Created by dkkim on 2017-03-26.
 */

public interface Define {


    boolean ON_CREATE_LOG = false;

//    boolean TEST_USER = false;
    boolean TEST_USER = true;
    String TEST_ID = "1087708737";
//    String TEST_ID = "1087708739";


    boolean USE_GLIDE = false;

    String SCORE_DATE = "SCORE_DATE";
    String CLUB_MODEL = "CLUB_MODEL";
    String SELECTED_USER_MAP = "SELECTED_USER_MAP";
    String CLUB_USER_LIST_MODEL = "CLUB_USER_LIST";

    String CLUB_ID = "CLUB_ID";

    String READ_GAME_MODEL = "READ_GAME_MODEL";

    boolean MAIN_PAGER_ACTIVITY = false;

    float CONTENT_VIEW_HOLDER_RATIO = 0.85f;

    String IMAGE_DIVIDER = "&A&t&t&a&c&h";
    String NEW_LINE = "\n";

    int IMAGE_LIMIT_COUNT = 5;

    String IMAGE_URL = "http://project-dk.iptime.org:8081/community/image/";
    String S3_DEFAULT_PATH = "https://s3.ap-northeast-2.amazonaws.com/2018community/";

    String YOUTUBE_PREVIEW_TAG = "YOUTUBE_PREVIEW";
    String YOUTUBE_FRAGMENT_TAG = "YoutubeFragment";
    String YOUTUBE_API_KEY = "AIzaSyBABHuB8Gc4YAYI91baMk1ZUWwdNVgt7W0";
    String YOUTUBE_VIDEO_ID = "YOUTUBE_VIDEO_ID";
    String YOUTUBE_VIDEO_CURRENT_SECONDS = "YOUTUBE_VIDEO_CURRENT_SECONDS";
    String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    String YOUTUBE_SHARE_URL = "https://youtu.be/";
    String INTRO_DELAY = "intro delay";
    String IMAGE_MULTI_SELECT = "IMAGE_MULTI_SELECT";

//    String FIX_POSITION = "FIX_POSITION";


    String POST_MODEL = "POST_MODEL";
    String REPLY_MODEL = "REPLY_MODEL";






    int mdpi = 0;
    int hdpi = 1;
    int xhdpi = 2;
    int xxhdpi = 3;
    int xxxhdpi = 4;






    /*
    startActivityForResult RequestCode
    */
    int MEDIA_ATTACH_LIST = 1;
    int LOGIN = 9001;
    int MODIFY_REPLY = 7000;
    int MODIFY_POST = 5000;
    int CLUB_USER_LIST = 1597;

    int CLUB_DETAIL = 5275;

    int REFRESH_GAME_LIST = 2597;


    int PERMISSION_REQUEST_STORAGE = 7;

    int THUMBNAIL_TYPE = 0;
    int LIST_TYPE = 1;

    int YOUTUBE_FULL_SCREEN = 5434;

    /*
     =====================   Event Bus   ======================
     */

    int EVENT_WRITE_POST = 1000; // Activity -> Service 글쓰기 전송
    int EVENT_POST_REFRESH = 1001; //  Service -> Activity 글쓰기 완료 후 갱신
    int EVENT_MODIFY_POST = 1002; // Activity -> Service 글수정 전송
    int EVENT_POST_REFRESH_MODIFY = 1003; //  Service -> Activity 글수정 완료 후 갱신
    int EVENT_DELETE_POST = 1004; // ViewHolder 에서 글삭제
    int EVENT_POST_REFRESH_REPLY_LIKE = 1005; //  ReadActivity에서 댓글이나 좋아요 수 변경 갱신

    int EVENT_REFRESH_SCORE = 1006; //  메인 액티비티에서 점수 등록했을때 갱신하기 위해

    int EVENT_REFRESH_MY_CLUB_LIST = 1007; // 내가 만들고 클럽 목록 갱신

    int EVENT_REFRESH_CLUB_USER_LIST = 1008; // 클럽 가입 승인이나 거부했을때 갱신






    /*
    0 : 가입 완료
    1 : 클럽 주인 (당연 가립된 상태)
    2 : 가입 승인대기
    3 : 탈퇴
    4 : 강퇴
    5 : 가입 거부
    6 : 가입안된 상태, 아직 아무것도 안함
    */

    int USER_TYPE_JOIN = 0;
    int USER_TYPE_OWNER = 1;
    int USER_TYPE_JOIN_WAIT = 2;
    int USER_TYPE_SECESSION = 3;
    int USER_TYPE_FORCE_SECESSION = 4;
    int USER_TYPE_JOIN_NEGATIVE = 5;
    int USER_TYPE_NOT_JOIN = 6;

}
