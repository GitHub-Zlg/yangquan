package app.com.yangquan.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import app.com.yangquan.R;
import app.com.yangquan.activity.PubActivity;
import app.com.yangquan.adapter.FragmentAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.FragmentInfo;
import butterknife.BindView;
import butterknife.OnClick;

public class TrendsFragment extends BaseFragment {
    @BindView(R.id.tab)
    SmartTabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    ArrayList<FragmentInfo> pages = new ArrayList<>();
    @BindView(R.id.iv_pub)
    ImageView ivPub;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    @Override
    protected int getLayout() {
        return R.layout.fragment_trends;
    }

    @Override
    protected void init() {
        initViewPager();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    private void initViewPager() {
        List<String> title = new ArrayList<>();
        title.add("推荐");
        title.add("关注");

        for (int i = 0; i < title.size(); i++) {
            FragmentInfo titleBean = null;
            if (i == 0) {
                titleBean = new FragmentInfo(new RecommFragment(), title.get(0));
            } else if (i == 1) {
                titleBean = new FragmentInfo(new FollowTrendsFragment(), title.get(1));
            }
            pages.add(titleBean);
        }
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TextView title1 = (TextView) tab.getTabAt(0);
                TextView title2 = (TextView) tab.getTabAt(1);
                switch (position) {
                    case 0:
                        title1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        title2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        break;
                    case 1:
                        title1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        title2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab.setViewPager(viewPager);
        TextView title1 = (TextView) tab.getTabAt(0);
        title1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        viewPager.setCurrentItem(0);
    }

    @OnClick({R.id.iv_pub, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pub:
                startActivity(new Intent(mContext, PubActivity.class));
                break;
            case R.id.ll_search:

                break;
        }
    }
}
