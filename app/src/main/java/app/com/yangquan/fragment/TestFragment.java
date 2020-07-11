package app.com.yangquan.fragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.TestAdapter;
import app.com.yangquan.base.BaseFragment;
import butterknife.BindView;

public class TestFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected int getLayout() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init() {
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        TestAdapter adapter = new TestAdapter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("测试");
        }
        adapter.setNewData(list);
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }
}
