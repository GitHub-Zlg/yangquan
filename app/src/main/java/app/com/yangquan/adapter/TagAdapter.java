package app.com.yangquan.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;

public class TagAdapter extends BaseQuickAdapter<String> {
    public TagAdapter() {
        super(R.layout.item_tag, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.tv_tag,s);
        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(s,holder.getAdapterPosition());
                }
            }
        });
    }

    public interface TagClickListener{
        void onItemClick(String tag,int position);
    }

    public TagClickListener listener;

    public void setOnTagClickLinsener(TagClickListener linsener){
        this.listener = linsener;
    }
}
