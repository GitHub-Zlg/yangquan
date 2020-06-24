package app.com.yangquan.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import app.com.yangquan.R;
import app.com.yangquan.bean.TrendBean;
import app.com.yangquan.util.BigImageUtil;
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
//        holder.setText(R.id.content, bean.getContent());
        holder.setText(R.id.content, "你好我是测试文本长度，要多长有多长，但是最多只能显示三行，多余的要给省略号，不知道省略号会不会显示。你好我是测试文本长度，要多长有多长，但是最多只能显示三行，多余的要给省略号，不知道省略号会不会显示。你好我是测试文本长度，要多长有多长，但是最多只能显示三行，多余的要给省略号，不知道省略号会不会显示。你好我是测试文本长度，要多长有多长，但是最多只能显示三行，多余的要给省略号，不知道省略号会不会显示。");
        NoScolllGridView photos = holder.getView(R.id.photos);
        if(bean.getImg().size()>0) {
            photos.setVisibility(View.VISIBLE);
            PhotoAdapter adapter = new PhotoAdapter(mContext, bean.getImg());
            adapter.setNewData(bean.getImg());
            photos.setAdapter(adapter);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < bean.getImg().size(); i++) {
                list.add(bean.getImg().get(i).getImg());
            }
            adapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
                @Override
                public void onImgClick(int position, String path, RoundedImageView imageView) {
                    BigImageUtil.more(mContext,list,position);
                }
            });
        }else {
            photos.setVisibility(View.GONE);
        }
    }
}
