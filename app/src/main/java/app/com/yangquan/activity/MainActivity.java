package app.com.yangquan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseFragmentActivity;
import app.com.yangquan.fragment.BlindFragemnt;
import app.com.yangquan.fragment.ChatListFragment;
import app.com.yangquan.fragment.MeFragment;
import app.com.yangquan.fragment.TrendsFragment;
import app.com.yangquan.http.Const;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseFragmentActivity {
    @BindView(R.id.fl_main_container)
    FrameLayout flMainContainer;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.l_item_home)
    BubbleToggleView lItemHome;
    @BindView(R.id.l_item_trends)
    BubbleToggleView lItemTrends;
    @BindView(R.id.l_item_notification)
    BubbleToggleView lItemNotification;
    @BindView(R.id.l_item_me)
    BubbleToggleView lItemMe;
    @BindView(R.id.bottom_navigation_view_linear)
    BubbleNavigationLinearView bottomNavigationViewLinear;
    private BlindFragemnt blindFragemnt; //相亲页
    private static final String TAG_BLIND = "tag_blind_fragment";
    private TrendsFragment trendsFragment;//广场页
    private static final String TAG_TREND = "tag_trend_fragment";
    private ChatListFragment msgFragment;//消息页
    private static final String TAG_MSG = "tag_msg_fragment";
    private MeFragment meFragment;//我的页
    private static final String TAG_ME = "tag_me_fragment";

    private boolean mIsExit;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initTab();
        lItemNotification.setBadgeText("20");
        resetButton(0);
        loginIM();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    /**
     * 登录IM
     */
    private void loginIM() {
        String uid = PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId);
        ImMessageUtil.getInstance().loginJMessage(uid, mContext);
    }

    //初始化底部导航栏
    public void initTab() {
        bottomNavigationViewLinear.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                resetButton(position);
            }
        });
    }

    //重置底部导航
    private void resetButton(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        blindFragemnt = (BlindFragemnt) fragmentManager.findFragmentByTag(TAG_BLIND);
        trendsFragment = (TrendsFragment) fragmentManager.findFragmentByTag(TAG_TREND);
        msgFragment = (ChatListFragment) fragmentManager.findFragmentByTag(TAG_MSG);
        meFragment = (MeFragment) fragmentManager.findFragmentByTag(TAG_ME);
        if (blindFragemnt != null) {
            transaction.hide(blindFragemnt);
        }

        if (trendsFragment != null) {
            transaction.hide(trendsFragment);
        }

        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }

        if (meFragment != null) {
            transaction.hide(meFragment);
        }

        switch (index) {
            case 0:
                if (blindFragemnt == null) {
                    blindFragemnt = new BlindFragemnt();
                    transaction.add(R.id.fl_main_container, blindFragemnt, TAG_BLIND);
                } else {
                    transaction.show(blindFragemnt);
                }

                if (trendsFragment == null) {
                    trendsFragment = new TrendsFragment();
                    transaction.add(R.id.fl_main_container, trendsFragment, TAG_TREND);
                    transaction.hide(trendsFragment);
                } else {
                    transaction.hide(trendsFragment);
                }

                if (msgFragment == null) {
                    msgFragment = new ChatListFragment();
                    transaction.add(R.id.fl_main_container, msgFragment, TAG_MSG);
                    transaction.hide(msgFragment);
                } else {
                    transaction.hide(msgFragment);
                }

                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main_container, meFragment, TAG_ME);
                    transaction.hide(meFragment);
                } else {
                    transaction.hide(meFragment);
                }
                break;
            case 1:
                transaction.show(trendsFragment);
                break;
            case 2:
                transaction.show(msgFragment);
                break;
            case 3:
                transaction.show(meFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                mIsExit = true;
                ToastUtil.show("再按一次退出程序");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2500);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
