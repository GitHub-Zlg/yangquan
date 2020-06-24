package app.com.yangquan.fragment;

import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.BlindAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.BlindBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.view.GalleryLayoutManager;
import app.com.yangquan.view.Transformer;
import butterknife.BindView;

public class BlindFragemnt extends BaseFragment implements GalleryLayoutManager.OnItemSelectedListener{
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private BlindAdapter adapter;
    private int page = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_blind;
    }

    @Override
    protected void init() {
        initView();
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
        adapter = new BlindAdapter();
        recycler.setAdapter(adapter);
        GalleryLayoutManager manager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        manager.attach(recycler, 1);
        // 设置滑动缩放效果
        manager.setItemTransformer(new Transformer());
        manager.setOnItemSelectedListener(this);
        List<BlindBean.DataBean> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            BlindBean.DataBean bean = new BlindBean.DataBean();
            list.add(bean);
        }
        adapter.setNewData(list);
       /* adapter.setmOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                // 支持手动点击滑动切换
                recyclerView.smoothScrollToPosition(Integer.valueOf(data));
            }
        });*/
    }

    @Override
    public void onItemSelected(RecyclerView recyclerView, View item, int position) {

    }
}
