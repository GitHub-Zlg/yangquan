package app.com.yangquan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.FragmentAdapter;
import app.com.yangquan.adapter.PersonAvatersAdapter;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.FragmentInfo;
import app.com.yangquan.fragment.TestFragment;
import app.com.yangquan.fragment.person.PersonFragment;
import app.com.yangquan.view.AppBarLayoutOverScrollViewBehavior;
import app.com.yangquan.view.DisInterceptNestedScrollView;
import app.com.yangquan.view.NoScrollViewPager;
import app.com.yangquan.view.RoundProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonHomeActivity extends BaseActivity {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.user_head_container)
    RelativeLayout userHeadContainer;
    TextView fragUcInterestTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.avater_title)
    RoundedImageView avatarTitle;
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
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.recycler_photos)
    RecyclerView recyclerPhotos;
    @BindView(R.id.iv_heart)
    ImageView ivHeart;
    private int lastState = 1;
    public static final String IMG_AVATAR = "imgAvatar";
    public static final String IMG_HEART = "imgHeart";
    ArrayList<FragmentInfo> pages = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_person_home;
    }

    @Override
    protected void init() {
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        initViewPager();
        initListener();
        initRecycler();
        Glide.with(mContext).load(getIntent().getStringExtra("head")).into(ivBg);
        ViewCompat.setTransitionName(ivBg, IMG_AVATAR);
        ViewCompat.setTransitionName(ivHeart, IMG_HEART);
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化头像recycler
    private void initRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(RecyclerView.HORIZONTAL);
        recyclerPhotos.setLayoutManager(lm);
        PersonAvatersAdapter adapter = new PersonAvatersAdapter();
        recyclerPhotos.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        list.add("https://pic1.zhimg.com/80/v2-37fc9e7a7c5193a6c68f63c4363e090f_720w.jpg?source=1940ef5c");
        list.add("https://pic1.zhimg.com/80/v2-99063b3ae65ffd0bb440e6ea6e626170_720w.jpg?source=1940ef5c");
        list.add("https://pic3.zhimg.com/80/v2-5ccd847abf5109f9f5c3ff93f5944580_720w.jpg?source=1940ef5c");
        list.add("https://pic3.zhimg.com/80/v2-dfc0d94d3504eebd6cf6e9502668ae12_720w.jpg?source=1940ef5c");
        adapter.setNewData(list);
        adapter.setonAvatarClickListener(new PersonAvatersAdapter.onAvatarClickListener() {
            @Override
            public void onAvatarClick(String s, int position) {
                Glide.with(mContext).load(s).into(ivBg);
            }
        });
    }

    //初始化tab+ViewPager
    private void initViewPager() {
        FragmentInfo personBean = new FragmentInfo(new PersonFragment(), "关于他");
        FragmentInfo TrendsBean = new FragmentInfo(new TestFragment(), "日常");
        pages.add(personBean);
        pages.add(TrendsBean);
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
                if (titleCenterLayout != null && ivSetting != null && ivShare != null) {
                    titleCenterLayout.setAlpha(percent);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        groupChange(1f, 2);
                    } else {
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
//                rlPersonData.setAlpha(1);
                break;
            case 2://完全关闭 显示黑色
                ucViewpager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                ucViewpager.setNoScroll(true);
               /* if(alpha<0.5){
                    rlPersonData.setAlpha(alpha);
                }*/
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.avater_title, R.id.iv_setting, R.id.iv_share, R.id.tv_follow, R.id.rl_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //如果是完全展开的状态下返回，可以走退回转场动画，如果不是完全展开状态，直接finish掉就可以，不然返回动画会很丑
                if (lastState == 1) {
                    onBackPressed();
                } else {
                    finish();
                }
                break;
            case R.id.avater_title:
                break;
            case R.id.iv_setting:
                break;
            case R.id.iv_share:
                break;
            case R.id.tv_follow:

                break;
            case R.id.rl_chat:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        //如果是完全展开的状态下返回，可以走退回转场动画，如果不是完全展开状态，直接finish掉就可以，不然返回动画会很丑
        if (lastState == 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
