package app.com.yangquan.fragment;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.activity.ChatActivity;
import app.com.yangquan.adapter.BlindAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.BlindBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.view.slide.ItemConfig;
import app.com.yangquan.view.slide.ItemTouchHelperCallback;
import app.com.yangquan.view.slide.OnSlideListener;
import app.com.yangquan.view.slide.SlideLayoutManager;
import app.com.yangquan.view.slide.SmileView;
import butterknife.BindView;
import butterknife.OnClick;

public class BlindFragemnt extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.smile_view)
    SmileView smileView;
    @BindView(R.id.tv_test)
    TextView tvTest;
    private ItemTouchHelperCallback mItemTouchHelperCallback;
    private int mLikeCount = 50;
    private int mDislikeCount = 50;
    private BlindAdapter adapter;
    private SlideLayoutManager mSlideLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<BlindBean.DataBean> list = new ArrayList<>();
    private int page = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_blind;
    }

    @Override
    protected void init() {
        initView();
        initListener();
        tablelist(page);
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
        smileView.setLike(mLikeCount);
        smileView.setDisLike(mDislikeCount);
        adapter = new BlindAdapter();
        recycler.setAdapter(adapter);
        mItemTouchHelperCallback = new ItemTouchHelperCallback(recycler.getAdapter(), list);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mSlideLayoutManager = new SlideLayoutManager(recycler, mItemTouchHelper);
        mItemTouchHelper.attachToRecyclerView(recycler);
        recycler.setLayoutManager(mSlideLayoutManager);
    }

    private void initListener() {
        mItemTouchHelperCallback.setOnSlideListener(new OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                if (direction == ItemConfig.SLIDING_LEFT) {
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                }
            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    mDislikeCount--;
                    smileView.setDisLike(mDislikeCount);
                    smileView.disLikeAnimation();
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    mLikeCount++;
                    smileView.setLike(mLikeCount);
                    smileView.likeAnimation();
                }
                int position = viewHolder.getAdapterPosition();
                Log.e("zlg", "onSlided--position:" + position);
            }

            @Override
            public void onClear() {
                tablelist(page++);
            }
        });
    }

    @OnClick(R.id.tv_test)
    public void onViewClicked() {
        startActivity(new Intent(mContext, ChatActivity.class));
    }
}
