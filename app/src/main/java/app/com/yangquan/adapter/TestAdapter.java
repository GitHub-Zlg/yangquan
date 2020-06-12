package app.com.yangquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import org.jetbrains.annotations.NotNull;


import app.com.yangquan.R;

public class TestAdapter extends BaseQuickAdapter<String> {
    public TestAdapter() {
        super(R.layout.item_test,null);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {

    }
}
