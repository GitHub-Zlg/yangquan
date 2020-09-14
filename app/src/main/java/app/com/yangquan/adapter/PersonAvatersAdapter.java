package app.com.yangquan.adapter;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import app.com.yangquan.R;

/*
 * 用户主页的头像adapter
 * */
public class PersonAvatersAdapter extends BaseQuickAdapter<String> {
    public PersonAvatersAdapter() {
        super(R.layout.item_avaters, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        Glide.with(mContext).load(s).placeholder(R.mipmap.icon_avater).into((ImageView) holder.getView(R.id.avatars));
        holder.getView(R.id.avatars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAvatarClick(s, holder.getAdapterPosition());
                }
            }
        });
    }

    public interface onAvatarClickListener {
        void onAvatarClick(String s, int position);
    }

    public onAvatarClickListener listener;

    public void setonAvatarClickListener(onAvatarClickListener listener) {
        this.listener = listener;
    }
}
