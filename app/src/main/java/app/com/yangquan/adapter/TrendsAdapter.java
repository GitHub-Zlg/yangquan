package app.com.yangquan.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;
import app.com.yangquan.bean.TrendBean;
import app.com.yangquan.view.NoScolllGridView;

public class TrendsAdapter extends BaseQuickAdapter<TrendBean.DataBean> {

    public TrendsAdapter() {
        super(R.layout.item_trend, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, TrendBean.DataBean bean) {
        Glide.with(mContext).load(bean.getUser_img()).placeholder(R.mipmap.icon_avater).into((ImageView) holder.getView(R.id.avater));
        holder.setText(R.id.name, bean.getNickname());
        holder.setText(R.id.time, bean.getTime());
        holder.setText(R.id.content, bean.getContent());
        NoScolllGridView photos = holder.getView(R.id.photos);
        if(bean.getImg().size()>0) {
            photos.setVisibility(View.VISIBLE);
            PhotoAdapter adapter = new PhotoAdapter(mContext, bean.getImg());
            adapter.setNewData(bean.getImg());
            photos.setAdapter(adapter);
        }else {
            photos.setVisibility(View.GONE);
        }
    }
}
