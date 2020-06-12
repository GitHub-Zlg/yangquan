package app.com.yangquan.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import app.com.yangquan.R;
import app.com.yangquan.http.HttpCallBack;
import app.com.yangquan.http.HttpManager;
import app.com.yangquan.listener.AppBarStateListener;
import app.com.yangquan.listener.MainAppBarExpandListener;
import app.com.yangquan.listener.MainAppBarLayoutListener;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements HttpCallBack {
    protected Activity mContext;//获取依赖的Activity对象
    protected abstract int getLayout();//布局
    protected abstract void init();//初始化
    protected abstract void onSuccess(int flag, String message);//请求服务区成功
    protected abstract void onFailure(int flag, String error);//请求服务器失败或异常

    protected AppBarLayout mAppBarLayout;
    protected MainAppBarLayoutListener mAppBarLayoutListener;
    protected MainAppBarExpandListener mAppBarExpandListener;
    protected boolean mAppBarExpand = true;//AppBarLayout是否展开
    protected boolean mNeedDispatch;//是否需要执行AppBarLayoutListener的回调

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        mAppBarLayout = view.findViewById(R.id.appBarLayout);
        if (mAppBarLayout != null) {
            mAppBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout, int state) {
                    switch (state) {
                        case AppBarStateListener.EXPANDED:
                            mAppBarExpand = true;
                            if (mAppBarExpandListener != null) {
                                mAppBarExpandListener.onExpand(true);
                            }
                            break;
                        case AppBarStateListener.MIDDLE:
                        case AppBarStateListener.COLLAPSED:
                            mAppBarExpand = false;
                            if (mAppBarExpandListener != null) {
                                mAppBarExpandListener.onExpand(false);
                            }
                            break;
                    }
                }
            });
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float totalScrollRange = appBarLayout.getTotalScrollRange();
                    float rate = -1 * verticalOffset / totalScrollRange;
                    if (mNeedDispatch && mAppBarLayoutListener != null) {
                        mAppBarLayoutListener.onOffsetChanged(rate);
                    }
                }
            });
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 设置AppBarLayout滑动监听
     */
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
        mAppBarLayoutListener = appBarLayoutListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    @Override
    public void onStop() {
        super.onStop();
        OkHttpUtils.getInstance().cancelTag(getName());//取消网络请求
    }

    private boolean isDestroy;

    @Override
    public void onDestroyView() {
        isDestroy = true;
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();//注解框架解绑
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    /**
     * post请求
     *
     * @param url   网络url
     * @param flag  请求标志
     * @param parms 请求参数
     */
    protected void post(String url, final int flag, Map<String, Object> parms) {
        HttpManager.post(mContext, getName(), url, flag, parms, this);
    }

    protected void get(String url, final int flag, Map<String, String> parms) {
        HttpManager.get(mContext, getName(), url, flag, parms, this);
    }


    /**
     * @return 获取子类名称
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 请求服务器成功的回调
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
     * 请求服务器失败或异常的回调
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
