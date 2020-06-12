package app.com.yangquan.jiguang.push;

import android.content.Context;
import android.util.Log;

import app.com.yangquan.App;
import cn.jpush.android.api.JPushInterface;

public class ImPushUtil {
    public static final String TAG = "zlg";
    private static ImPushUtil sInstance;
    private boolean mClickNotification;
    private int mNotificationType;

    private ImPushUtil() {

    }

    public static ImPushUtil getInstance() {
        if (sInstance == null) {
            synchronized (ImPushUtil.class) {
                if (sInstance == null) {
                    sInstance = new ImPushUtil();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        JPushInterface.init(context);
    }


    public void logout() {
        stopPush();
    }

    public void resumePush() {
        if (JPushInterface.isPushStopped(App.getInstance())) {
            JPushInterface.resumePush(App.getInstance());
        }
    }

    public void stopPush() {
        JPushInterface.stopPush(App.getInstance());
    }

    public boolean isClickNotification() {
        return mClickNotification;
    }

    public void setClickNotification(boolean clickNotification) {
        mClickNotification = clickNotification;
    }

    public int getNotificationType() {
        return mNotificationType;
    }

    public void setNotificationType(int notificationType) {
        mNotificationType = notificationType;
    }

    /**
     * 获取极光推送 RegistrationID
     */
    public static String getPushID() {
        return JPushInterface.getRegistrationID(App.getInstance());
    }
}
