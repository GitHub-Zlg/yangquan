package app.com.yangquan.fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.MsgAdapter;
import app.com.yangquan.base.BaseFragment;
import butterknife.BindView;

public class MsgFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    @Override
    protected int getLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init() {
        initRecycler();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        MsgAdapter adapter = new MsgAdapter();
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }
}
