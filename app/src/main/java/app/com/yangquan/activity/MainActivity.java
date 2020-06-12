package app.com.yangquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseFragmentActivity;
import app.com.yangquan.fragment.BlindFragemnt;
import app.com.yangquan.fragment.MeFragment;
import app.com.yangquan.fragment.MsgFragment;
import app.com.yangquan.fragment.TrendsFragment;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import liang.lollipop.ltabview.LTabHelper;
import liang.lollipop.ltabview.LTabView;
import liang.lollipop.ltabview.builder.ExpandBuilder;
import liang.lollipop.ltabview.item.ExpandItem;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends BaseFragmentActivity {
    @BindView(R.id.tabView)
    LTabView tabView;
    @BindView(R.id.fl_main_container)
    FrameLayout flMainContainer;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.bottom_bar)
    AnimatedBottomBar bottomBar;
    private BlindFragemnt blindFragemnt; //相亲页
    private static final String TAG_BLIND = "tag_blind_fragment";
    private TrendsFragment trendsFragment;//广场页
    private static final String TAG_TREND = "tag_trend_fragment";
    private MsgFragment msgFragment;//消息页
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
        initTab(tabView);
        resetButton(0);
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化底部导航栏
    public void initTab(final LTabView tabView) {
        ExpandBuilder helper = LTabHelper.INSTANCE.withExpandItem(tabView)
                .addItem(new Function1<ExpandItem, Unit>() {
                    @Override
                    public Unit invoke(ExpandItem expandItem) {
                        int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.pink);
                        expandItem.setExpandColor(color & 0x60FFFFFF);
                        expandItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        expandItem.setSelectedIconColor(color);
                        expandItem.setUnselectedIconColor(Color.BLACK);
                        expandItem.setText("相亲");
                        expandItem.setTextColor(color);
                        return null;
                    }
                }).addItem(new Function1<ExpandItem, Unit>() {
                    @Override
                    public Unit invoke(ExpandItem expandItem) {
                        int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.orange);
                        expandItem.setExpandColor(color & 0x60FFFFFF);
                        expandItem.setIcon(R.drawable.ic_whatshot_black_24dp);
                        expandItem.setSelectedIconColor(color);
                        expandItem.setUnselectedIconColor(Color.BLACK);
                        expandItem.setText("广场");
                        expandItem.setTextColor(color);
                        return null;
                    }
                }).addItem(new Function1<ExpandItem, Unit>() {
                    @Override
                    public Unit invoke(ExpandItem expandItem) {
                        int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.purple);
                        expandItem.setExpandColor(color & 0x60FFFFFF);
                        expandItem.setIcon(R.drawable.ic_home_black_24dp);
                        expandItem.setSelectedIconColor(color);
                        expandItem.setUnselectedIconColor(Color.BLACK);
                        expandItem.setText("消息");
                        expandItem.setTextColor(color);
                        return null;
                    }
                }).addItem(new Function1<ExpandItem, Unit>() {
                    @Override
                    public Unit invoke(ExpandItem expandItem) {
                        int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.blue);
                        expandItem.setExpandColor(color & 0x60FFFFFF);
                        expandItem.setIcon(R.drawable.ic_person_black_24dp);
                        expandItem.setSelectedIconColor(color);
                        expandItem.setUnselectedIconColor(Color.BLACK);
                        expandItem.setText("我的");
                        expandItem.setTextColor(color);
                        return null;
                    }
                });
        helper.setLayoutStyle(LTabView.Style.Fit);
        helper.addOnSelectedListener(new LTabView.OnSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                switch (index) {
                    case 0:
                        resetButton(0);
                        break;
                    case 1:
                        resetButton(1);
                        break;
                    case 2:
                        resetButton(2);
                        break;
                    case 3:
                        resetButton(3);
                        break;
                }
            }
        });

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                resetButton(i1);
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    //重置底部导航
    private void resetButton(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        blindFragemnt = (BlindFragemnt) fragmentManager.findFragmentByTag(TAG_BLIND);
        trendsFragment = (TrendsFragment) fragmentManager.findFragmentByTag(TAG_TREND);
        msgFragment = (MsgFragment) fragmentManager.findFragmentByTag(TAG_MSG);
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
                    msgFragment = new MsgFragment();
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
