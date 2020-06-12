package app.com.yangquan.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import app.com.yangquan.http.HttpCallBack;
import app.com.yangquan.http.HttpManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragmentActivity extends FragmentActivity implements HttpCallBack {
    protected abstract int getLayout();//布局

    protected abstract void init();//初始化

    protected abstract void onSuccess(int flag, String message);//请求服务区成功

    protected abstract void onFailure(int flag, String error);//请求服务器失败或异常

    private Unbinder unbinder;
    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        setStatusBar();
        unbinder = ButterKnife.bind(this);/*注解框架绑定*/
        mContext = this;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        DensityHelper.resetDensity(mContext, 750f);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        DensityHelper.resetDensity(mContext, 750f);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
        System.gc();
        System.runFinalization();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 设置透明状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isStatusBarWhite()) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }

    protected boolean isStatusBarWhite() {
        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        OkHttpUtils.getInstance().cancelTag(getName());//取消当前页面所有的网络请求
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    /**
     * @return 实现类的简单名称
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 网络请求
     *
     * @param url   请求地址
     * @param flag  区分是当前页面哪一个网络请求
     * @param parms 入参
     */
    protected void post(String url, final int flag, Map<String, Object> parms) {
        HttpManager.post(mContext, getName(), url, flag, parms, this);
    }


    protected void get(String url, final int flag, Map<String, String> parms) {
        HttpManager.get(mContext, getName(), url, flag, parms, this);
    }


    /**
     * 请求成功的回调
     *
     * @param flag    与网络请求的入参flag对应。表述一个网络的请求与回调
     * @param message 服务器返回的内容
     */
    @Override
    public void onHttpSuccess(int flag, String message) {
        if (!isDestroy) {
            onSuccess(flag, message);
        }

    }

    /**
     * 请求失败或异常的回调
     *
     * @param flag          与网络请求的入参flag对应。表述一个网络的请求与回调
     * @param error_message 服务器返回的错误信息
     */
    @Override
    public void onHttpFail(int flag, String error_message) {
        if (!isDestroy) {
            onFailure(flag, error_message);
        }
    }

    private boolean isDestroy;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
