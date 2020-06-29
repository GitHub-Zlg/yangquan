package app.com.yangquan.http;

public class Const {
    public static final class SharePre {
        public static final String userId = "userId";
        public static final String mobile = "mobile";
        public static final String pwd = "pwd";
    }

    public static final class Config {
        public final static int IMG_MAX = 6; //最大选择图片数
        public static final String base = "http://www.xiangyingchun.cn/api/";
        public static final String codesms = base + "codesms/index";//注册
        public static final String Pwd = base + "Pwd/index";//重置密码
        public static final String uploadImage = base + "user/uploadImage";//上传头像
        public static final String setSex = base + "user/setSex";//设置性别
        public static final String setNickname = base + "user/setNickname";//设置昵称
        public static final String setAge = base + "user/setAge";//设置年龄
        public static final String setHangye = base + "user/setHangye";//设置职业
        public static final String login = base + "login/index";//登录
        public static final String userInfo = base + "user/info";//用户信息
        public static final String tablelist = base + "lists/tablelist";//首页卡片列表
        public static final String pub = base + "post/index";//发布
        public static final String trendslist = base + "post/tablelist";//广场列表
        public static final String setAutograph = base + "user/setAutograph";//设置个性签名
        public static final String setWechat = base + "user/setWechat";//设置微信号
        public static final String setWechatShow = base + "user/setWechatShow";//设置微信是否显示
        public static final String setRegion = base + "user/setRegion";//设置地区
        public static final String setHeight = base + "user/setHeight";//设置身高
        public static final String setLabel = base + "user/setLabel";//设置标签
    }
}
