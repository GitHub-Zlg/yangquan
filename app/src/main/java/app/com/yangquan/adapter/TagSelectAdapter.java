package app.com.yangquan.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;

public class TagSelectAdapter extends BaseQuickAdapter<String> {
    public TagSelectAdapter() {
        super(R.layout.item_tag_select, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.tv_tag,s);
        holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onDeleteClick(s,holder.getAdapterPosition());
                }
            }
        });
    }

    public interface TagDeleteClickListener{
        void onDeleteClick(String tag,int position);
    }

    public TagDeleteClickListener listener;

    public void setOnTagDeleteClickListener(TagDeleteClickListener linsener){
        this.listener = linsener;
    }
}
