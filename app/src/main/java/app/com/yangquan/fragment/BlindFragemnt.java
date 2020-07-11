package app.com.yangquan.fragment;

import android.content.Intent;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import app.com.yangquan.R;
import app.com.yangquan.activity.PersonHomeActivity;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.BlindBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.view.CustPagerTransformer;
import app.com.yangquan.view.DragLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class BlindFragemnt extends BaseFragment implements DragLayout.GotoDetailListener {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.card)
    CardView card;
    private int page = 1;
    private List<CommonFragment> fragments = new ArrayList<>(); // 供ViewPager使用

    @Override
    protected int getLayout() {
        return R.layout.fragment_blind;
    }

    @Override
    protected void init() {
        initView();
        // 3. 填充ViewPager
        fillViewPager();
//        tablelist(page);
    }

    /**
     * 填充ViewPager
     */
    private void fillViewPager() {
        viewpager.setPageTransformer(false, new CustPagerTransformer(mContext));

        // 2. viewPager添加adapter
        for (int i = 0; i < 10; i++) {
            // 预先准备10个fragment
            fragments.add(new CommonFragment());
        }
        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position);
                fragment.bindData(getResources().getDrawable(R.mipmap.head));
                return fragment;
            }

            @Override
            public int getItemPosition(Object object) {
                //TODO:这里换一批的更新数据的时候会有将近1000ms的卡顿！方法其实是最差劲的方法，因为把所有的fragment都销毁了，
                // 然后进行重建，十个，所以造成了UI界面的卡顿现象。所以必须想出一种优化方法
                return POSITION_NONE;
            }
        };
        viewpager.setAdapter(adapter);


        // 3. viewPager滑动时，调整指示器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void tablelist(int page) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        map.put("page", page + "");
        get(Const.Config.tablelist, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            BlindBean bean = new Gson().fromJson(message, BlindBean.class);
            if (bean != null) {
//                list.addAll(bean.getData());
//                adapter.setNewData(list);
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    private void initView() {

    }

    @Override
    public void gotoDetail() {
      /*  Activity activity = (Activity) getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(imageView, DetailActivity.IMAGE_TRANSITION_NAME),
                new Pair(address1, DetailActivity.ADDRESS1_TRANSITION_NAME),
                new Pair(address2, DetailActivity.ADDRESS2_TRANSITION_NAME),
                new Pair(address3, DetailActivity.ADDRESS3_TRANSITION_NAME),
                new Pair(address4, DetailActivity.ADDRESS4_TRANSITION_NAME),
                new Pair(address5, DetailActivity.ADDRESS5_TRANSITION_NAME),
                new Pair(ratingBar, DetailActivity.RATINGBAR_TRANSITION_NAME),
                new Pair(head1, DetailActivity.HEAD1_TRANSITION_NAME),
                new Pair(head2, DetailActivity.HEAD2_TRANSITION_NAME),
                new Pair(head3, DetailActivity.HEAD3_TRANSITION_NAME),
                new Pair(head4, DetailActivity.HEAD4_TRANSITION_NAME)
        );
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        ActivityCompat.startActivity(activity, intent, options.toBundle());*/
    }

    @OnClick(R.id.card)
    public void onViewClicked() {
        startActivity(new Intent(mContext, PersonHomeActivity.class));

    }
}
