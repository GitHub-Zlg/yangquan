package app.com.yangquan.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;

public class TagAdapter extends BaseQuickAdapter<String> {
    private int mType;

    public TagAdapter(int type) {
        super(R.layout.item_tag, null);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        LinearLayout llRoot = holder.getView(R.id.ll_root);
        TextView tvTag = holder.getView(R.id.tv_tag);
        switch (mType) {
            case 1: //设置标签背景
                llRoot.setBackgroundResource(R.drawable.selector_tag);
                // 错误写法，此方法即使使用了selector，返回的也只是一种颜色，也不会有点击时改变字体颜色的效果
                //btn.setTextColor(getResources().getColor(R.color.seat_memory_font_selector));
                //正确写法，
                //btn.setTextColor(getResources().getColorStateList(R.color.seat_memory_font_selector));
                //getColorStateList查看源码：大体意思就是：返回一个ColorStateList对象，该对象包含一个单色或多个颜色，这些颜色可以基于状态进行选择
                tvTag.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tag_text));
                break;
            case 2: //用户的标签背景
                llRoot.setBackgroundResource(R.drawable.bg_tag_green);
                tvTag.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 3: //共同标签背景
                llRoot.setBackgroundResource(R.drawable.bg_tag_blue);
                tvTag.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
        }
        tvTag.setText(s);
        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(s, holder.getAdapterPosition());
                }
            }
        });
    }

    public interface TagClickListener {
        void onItemClick(String tag, int position);
    }

    public TagClickListener listener;

    public void setOnTagClickLinsener(TagClickListener linsener) {
        this.listener = linsener;
    }
}
