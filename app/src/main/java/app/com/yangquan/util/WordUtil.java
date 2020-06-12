package app.com.yangquan.util;

import android.content.res.Resources;

import app.com.yangquan.App;

/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {

    private static Resources sResources;

    static {
        sResources = App.getInstance().getResources();
    }

    public static String getString(int res) {
        return sResources.getString(res);
    }
}
