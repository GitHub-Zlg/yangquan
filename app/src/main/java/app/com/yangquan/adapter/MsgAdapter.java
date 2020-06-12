package app.com.yangquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import app.com.yangquan.R;
import app.com.yangquan.jiguang.im.ImUserMsgEvent;

public class MsgAdapter extends BaseQuickAdapter<ImUserMsgEvent> {
    public MsgAdapter() {
        super(R.layout.item_msg,null);
    }

    @Override
    protected void convert(BaseViewHolder holder, ImUserMsgEvent bean) {

    }
}
