package app.com.yangquan.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.activity.PubActivity;
import app.com.yangquan.adapter.TrendsAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.TrendBean;
import app.com.yangquan.http.Const;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class RecommFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private TrendsAdapter adapter;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int pageNum = 1;
    private boolean isRefresh;
    private Drawable[] drawables;

    @Override
    protected int getLayout() {
        return R.layout.fragment_recomm;
    }

    @Override
    protected void init() {
        initRecycer();
        initRefresh();
        refreshData();
    }

    //刷新
    private void refreshData() {
        isRefresh = true;
        pageNum = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        post(Const.Config.trendslist, 1, map);
    }

    //加载
    private void loadData() {
        isRefresh = false;
        pageNum++;
        Map<String, Object> map = new HashMap<>();
        map.put("page", pageNum + "");
        post(Const.Config.trendslist, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            TrendBean bean = new Gson().fromJson(message, TrendBean.class);
            if (bean != null) {
                if (isRefresh) {
                    adapter.setNewData(bean.getData());
                    refresh.finishRefresh();
                } else {
                    adapter.addData(bean.getData());
                    refresh.finishLoadMore();
                }
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        if (isRefresh) {
            refresh.finishRefresh();
        } else {
            refresh.finishLoadMore();
        }
    }

    //初始化recycler
    private void initRecycer() {
        // 创建StaggeredGridLayoutManager实例
        adapter = new TrendsAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(mContext, permissions)) {
            //已经打开权限
            Intent intent = new Intent(mContext, PubActivity.class);
            startActivity(intent);
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }
    }

    private void initRefresh() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });

        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }
}
