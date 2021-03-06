package com.dk.project.post.base;

/**
 * Created by dkkim on 2017-03-26.
 */

public interface Define {
    
    boolean ON_CREATE_LOG = false;

    boolean USE_GLIDE = false;

    String SCORE_DATE = "SCORE_DATE";
    String CLUB_MODEL = "CLUB_MODEL";
    String SELECTED_USER_MAP = "SELECTED_USER_MAP";

    int LOGOUT = 0;
    int NO_PERMISSION = 1;
    int OK = 2;

    String ID = "ID";

    String PREFERENCES = "info";


    String CLUB_ID = "CLUB_ID";

    String READ_GAME_MODEL = "READ_GAME_MODEL";

    boolean MAIN_PAGER_ACTIVITY = false;

    float CONTENT_VIEW_HOLDER_RATIO = 0.85f;

    String IMAGE_DIVIDER = "&A&t&t&a&c&h";
    String NEW_LINE = "\n";

    int IMAGE_LIMIT_COUNT = 5;

    String IMAGE_URL = "https://api-storage.cloud.toast.com/v1/AUTH_34ee9d51c09041778e1ad43165b6370d/bowling_image/";
//    String IMAGE_URL = "http://project-dk.iptime.org:8081/community/image/";

    String YOUTUBE_PREVIEW_TAG = "YOUTUBE_PREVIEW";
    String YOUTUBE_FRAGMENT_TAG = "YoutubeFragment";
    String YOUTUBE_API_KEY = "AIzaSyBABHuB8Gc4YAYI91baMk1ZUWwdNVgt7W0";
    String YOUTUBE_VIDEO_ID = "YOUTUBE_VIDEO_ID";
    String YOUTUBE_VIDEO_CURRENT_SECONDS = "YOUTUBE_VIDEO_CURRENT_SECONDS";
    String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    String YOUTUBE_SHARE_URL = "https://youtu.be/";
    String INTRO_DELAY = "intro delay";
    String IMAGE_MULTI_SELECT = "IMAGE_MULTI_SELECT";

    String MAIN = "com.dk.project.bowling.MainActivity";


//    String FIX_POSITION = "FIX_POSITION";


    String POST_MODEL = "POST_MODEL";
    String REPLY_MODEL = "REPLY_MODEL";
    String GAME_SCORE_LIST = "GAME_SCORE_LIST";


    int CREATE_MODE = 1;
    int MODEFY_MODE = 2;


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

    int CLUB_MODIFY = 9433;

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


    int EVENT_REFRESH_CLUB_GAME_LIST = 1009; // 클럽 점수 등록했을때 갱신

    int EVENT_CLOSE_CREATE_MODIFY_GAME_ACTIVITY = 1010; // 클럽 점수 수정하고 화면 닫기

    int EVENT_LOGIN_SUCCESS = 8888; // 앱 사용중 로그인 성공

    int EVENT_PROFILE_SUCCESS = 9999; // 프로필 변경 성공


    int EVENT_ALREADY_DELETE_POST = 12324; // 이미 삭제된 포스트


    int EVENT_UPDATE_CLUB_STATUS = 3432; // 클럽의 생태값이 바꼈을떼






    /*
    0 : 가입 완료
    1 : 클럽 주인 (당연 가립된 상태)
    2 : 가입 승인대기
    3 : 탈퇴
    4 : 강퇴
    5 : 가입 거부
    6 : 가입했다가 가입 취소
    */

    int USER_TYPE_JOIN = 0;
    int USER_TYPE_OWNER = 1;
    int USER_TYPE_JOIN_WAIT = 2;
    int USER_TYPE_SECESSION = 3;
    int USER_TYPE_FORCE_SECESSION = 4;
    int USER_TYPE_JOIN_NEGATIVE = 5;
    int USER_TYPE_NOT_JOIN = 6;

}
