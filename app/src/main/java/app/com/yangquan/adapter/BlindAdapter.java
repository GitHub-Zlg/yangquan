package app.com.yangquan.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import org.jetbrains.annotations.NotNull;

import app.com.yangquan.R;
import app.com.yangquan.bean.BlindBean;
import app.com.yangquan.bean.UserBean;

public class BlindAdapter extends BaseQuickAdapter<BlindBean.DataBean> {
    public BlindAdapter() {
        super(R.layout.item_blind,null);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BlindBean.DataBean bean) {
        /*Glide.with(mContext).load(bean.getUser_img()).placeholder(R.mipmap.icon_avater).into((ImageView)holder.getView(R.id.img_user));
        Glide.with(mContext).load(bean.getHome_img()).into((ImageView) holder.getView(R.id.img_bg));
        holder.setText(R.id.tv_title,bean.getNickname());*/
    }
}
