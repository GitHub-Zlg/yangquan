package app.com.yangquan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import app.com.yangquan.R;
import app.com.yangquan.bean.TrendBean;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<TrendBean.DataBean.ImgBean> mList = new ArrayList<>();
    private LayoutInflater inflater;

    public PhotoAdapter(Context mContext, List<TrendBean.DataBean.ImgBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_photo, parent, false);
        RoundedImageView iv = convertView.findViewById(R.id.iv_photo_round);
        TextView tv = convertView.findViewById(R.id.tv_bg);
        Glide.with(mContext)
                .load(mList.get(position).getImg())
                .apply(new RequestOptions().placeholder(R.drawable.bg_photo_num))
                .into(iv);
        if (position == 2 && mList.size() > 3) {
            tv.setVisibility(View.VISIBLE);
            tv.setText("+ " + (mList.size() - 3));
        } else {
            tv.setVisibility(View.GONE);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onImgClick(position, mList.get(position).getImg(), iv);
                }
            }
        });
        return convertView;
    }

    public void setNewData(List<TrendBean.DataBean.ImgBean> mList) {
        if (mList != null) {
            this.mList = mList;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onImgClick(int position, String path, RoundedImageView imageView);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
