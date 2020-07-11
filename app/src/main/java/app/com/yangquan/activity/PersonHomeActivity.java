package app.com.yangquan.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.FragmentAdapter;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.FragmentInfo;
import app.com.yangquan.fragment.TestFragment;
import app.com.yangquan.view.AppBarLayoutOverScrollViewBehavior;
import app.com.yangquan.view.DisInterceptNestedScrollView;
import app.com.yangquan.view.NoScrollViewPager;
import app.com.yangquan.view.RoundProgressBar;
import butterknife.BindView;
import butterknife.OnClick;

public class PersonHomeActivity extends BaseActivity {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.user_head_container)
    RelativeLayout userHeadContainer;
    @BindView(R.id.frag_uc_nickname_tv)
    TextView fragUcNicknameTv;
    @BindView(R.id.frag_uc_msg_tv)
    TextView fragUcMsgTv;
    @BindView(R.id.frag_uc_follow_tv)
    TextView fragUcFollowTv;
    @BindView(R.id.frag_uc_interest_tv)
    TextView fragUcInterestTv;
    @BindView(R.id.func_order)
    TextView funcOrder;
    @BindView(R.id.func_integral)
    TextView funcIntegral;
    @BindView(R.id.func_payment)
    TextView funcPayment;
    @BindView(R.id.func_invite)
    TextView funcInvite;
    @BindView(R.id.func_stages)
    TextView funcStages;
    @BindView(R.id.func_insurance)
    TextView funcInsurance;
    @BindView(R.id.func_ticket)
    TextView funcTicket;
    @BindView(R.id.func_container)
    LinearLayout funcContainer;
    @BindView(R.id.middle_layout)
    DisInterceptNestedScrollView middleLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.avater_title)
    RoundedImageView avaterTitle;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_center_layout)
    LinearLayout titleCenterLayout;
    @BindView(R.id.uc_progressbar)
    RoundProgressBar ucProgressbar;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.tab)
    SmartTabLayout tab;
    @BindView(R.id.tag_recyclerview)
    RecyclerView tagRecyclerview;
    @BindView(R.id.tag_ocreate_ll)
    LinearLayout tagOcreateLl;
    @BindView(R.id.scroll)
    DisInterceptNestedScrollView scroll;
    @BindView(R.id.uc_viewpager)
    NoScrollViewPager ucViewpager;
    @BindView(R.id.avater)
    RoundedImageView avater;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private int lastState = 1;
    ArrayList<FragmentInfo> pages = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_person_home;
    }

    @Override
    protected void init() {
        initTab();
        initListener();
        ImmersionBar.with(this).statusBarDarkFont(false).init();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化tab+ViewPager
    private void initTab() {
        for (int i = 0; i < 3; i++) {
            FragmentInfo titleBean = null;
            titleBean = new FragmentInfo(new TestFragment(), "title" + i);
            pages.add(titleBean);
        }
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), pages);
        ucViewpager.setAdapter(adapter);
        ucViewpager.setOffscreenPageLimit(2);
        tab.setViewPager(ucViewpager);
        ucViewpager.setCurrentItem(0);
    }

    //初始化手势滑动监听
    private void initListener() {
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (titleCenterLayout != null && avater != null && ivSetting != null && ivShare != null) {
                    titleCenterLayout.setAlpha(percent);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        if (avater.getVisibility() != View.GONE) {
                            avater.setVisibility(View.GONE);
                        }
                        groupChange(1f, 2);
                    } else {
                        if (avater.getVisibility() != View.VISIBLE) {
                            avater.setVisibility(View.VISIBLE);
                        }
                        groupChange(percent, 0);
                    }

                }
            }
        });
        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) appbarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener(new AppBarLayoutOverScrollViewBehavior.onProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
                ucProgressbar.setProgress((int) (progress * 360));
                if (progress == 1 && !ucProgressbar.isSpinning && isRelease) {
                    // 刷新viewpager里的fragment
                }
                if (ivShare != null) {
                    if (progress == 0 && !ucProgressbar.isSpinning) {
                        ivShare.setVisibility(View.VISIBLE);
                    } else if (progress > 0 && ivSetting.getVisibility() == View.VISIBLE) {
                        ivShare.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    /**
     * @param alpha
     * @param state 0-正在变化 1展开 2 关闭
     */
    public void groupChange(float alpha, int state) {
        lastState = state;
        ivSetting.setAlpha(alpha);
        ivShare.setAlpha(alpha);

        switch (state) {
            case 1://完全展开 显示白色
                ucViewpager.setNoScroll(false);
                break;
            case 2://完全关闭 显示黑色
                ucViewpager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                ucViewpager.setNoScroll(true);
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.avater_title, R.id.iv_setting, R.id.iv_share, R.id.avater})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.avater_title:
                break;
            case R.id.iv_setting:
                break;
            case R.id.iv_share:
                break;
            case R.id.avater:
                break;
        }
    }
}
