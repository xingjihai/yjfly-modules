package ocr.service;

public class OCRConstants {
    public static final String APPID = "qcloud.ocr.service.app.id";
    public static final String SECRETID = "qcloud.ocr.service.secret.id";
    public static final String SECRETKEY = "qcloud.ocr.service.secret.key";
    public static final String RESULT_LIST_KEY = "result_list";
    public static final String BASE_ID_CARD_PATH = "/upload/sys/useridcard/";
    public static final String MODIFY_PASSWORD_PATH = "/upload/sys/modifyinfo/";
//    public static final String IMG_ID_CARD_NAME = "www.jpg";
    public static final String IMG_MODIFY_PASSWORD_NAME = "modify.jpg";
    public static final String DOMAIN = "recognition.image.myqcloud.com";

//    public static final String WX_CORPID = "wx95ccdb210c042cc6";
//    public static final String WX_SECRET = "9a5cb83912cf67116cf9ba145567e6cd";
//    public static final String WX_CORPID = "wxb5ed2d9e07ef8755";
//    public static final String WX_SECRET = "cea6f59eb8201bc429e34bee807d28b1";

//    public static final String DOWNLOAD_FILE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    public static final String DOWNLOAD_FILE_URL = "wechat.service.download.file.url";
    public static final String OPENID_OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APP_ID&secret=SECRET&code=CODE&grant_type=authorization_code";
}