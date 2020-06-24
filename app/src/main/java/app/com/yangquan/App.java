package app.com.yangquan;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.internal.InternalClassics;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import app.com.yangquan.http.Const;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.jiguang.push.ImPushUtil;
import app.com.yangquan.util.PreferencesUtils;

public class App extends MultiDexApplication {
    private static App mInstance;
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.refresh_bg_color, R.color.blue);//全局设置主题颜色
                ClassicsHeader header = new ClassicsHeader(context);
                TextView tvTile = header.findViewById(InternalClassics.ID_TEXT_TITLE);
                TextView tvupdate = header.findViewById(ClassicsHeader.ID_TEXT_UPDATE);
                tvTile.setTextSize(12);
                tvupdate.setTextSize(8);
                return header;
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                ClassicsFooter footer = new ClassicsFooter(context).setDrawableSize(16);
                TextView tvTile = footer.findViewById(InternalClassics.ID_TEXT_TITLE);
                tvTile.setTextSize(12);
                return footer;
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化极光推送
        ImPushUtil.getInstance().init(this);
        //初始化极光IM
        ImMessageUtil.getInstance().init();
        resolute_photo_camera();
    }

    /**
     * 解决7.0相机和选择照片崩溃问题
     */
    private void resolute_photo_camera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    //获取uid
    public String getUid() {
        return PreferencesUtils.getSharePreStr(this, Const.SharePre.userId);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static App getInstance() {
        return mInstance;
    }
}
