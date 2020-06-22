package app.com.yangquan.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.com.yangquan.R;
import app.com.yangquan.jiguang.im.ImDateUtil;
import app.com.yangquan.jiguang.im.ImMessageBean;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.util.BigImageUtil;
import app.com.yangquan.util.TextRender;
import app.com.yangquan.view.MyImageView;
import cn.jpush.im.android.api.content.TextContent;

public class ChatAdapter extends BaseMultiItemQuickAdapter<ImMessageBean> {
    private File imageFile;
    private long mLastMessageTime;
    private String text;
    private List<String> imgList = new ArrayList<>();

    public ChatAdapter() {
        super(null);
        addItemType(ImMessageBean.TYPE_TEXT_R, R.layout.item_text_right);
        addItemType(ImMessageBean.TYPE_TEXT_L, R.layout.item_text_left);
        addItemType(ImMessageBean.TYPE_IMAGE_R, R.layout.item_img_right);
        addItemType(ImMessageBean.TYPE_IMAGE_L, R.layout.item_img_left);
        addItemType(ImMessageBean.TYPE_VOICE_R, R.layout.item_voice_right);
        addItemType(ImMessageBean.TYPE_VOICE_L, R.layout.item_voice_left);
    }

    @Override
    protected void convert(BaseViewHolder holder, ImMessageBean bean) {
        TextView mTime = holder.getView(R.id.time);
        MyImageView mImgs = holder.getView(R.id.img);
        if (mTime != null) {
            if (holder.getAdapterPosition() == 0) {
                mLastMessageTime = bean.getTime();
                if (mTime.getVisibility() != View.VISIBLE) {
                    mTime.setVisibility(View.VISIBLE);
                } else {
                    mTime.setVisibility(View.GONE);
                }
                mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
            } else {
                if (ImDateUtil.isCloseEnough(bean.getTime(), mLastMessageTime)) {
                    if (mTime.getVisibility() == View.VISIBLE) {
                        mTime.setVisibility(View.GONE);
                    }
                } else {
                    mLastMessageTime = bean.getTime();
                    if (mTime.getVisibility() != View.VISIBLE) {
                        mTime.setVisibility(View.VISIBLE);
                    }
                    mTime.setText(ImDateUtil.getTimestampString(mLastMessageTime));
                }
            }
        }

        if(mImgs!=null){
            mImgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigImageUtil.single(mContext,mImgs.getFile().getAbsolutePath());
                }
            });
        }
        switch (holder.getItemViewType()) {
            case ImMessageBean.TYPE_TEXT_L:
                Glide.with(mContext).load(R.mipmap.icon_woman).into((ImageView) holder.getView(R.id.avatar));
                text = ((TextContent) bean.getRawMessage().getContent()).getText();
                holder.setText(R.id.text, TextRender.renderChatMessage(text));
                holder.getView(R.id.text).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
//                        showPopWindows(view, 1, bean, holder.getAdapterPosition());
                        return false;
                    }
                });
                break;
            case ImMessageBean.TYPE_TEXT_R:
                Glide.with(mContext).load(R.mipmap.icon_man).into((ImageView) holder.getView(R.id.avatar));
                text = ((TextContent) bean.getRawMessage().getContent()).getText();
                holder.setText(R.id.text, TextRender.renderChatMessage(text));
                holder.getView(R.id.text).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
//                        showPopWindows(view, 2, bean, holder.getAdapterPosition());
                        return false;
                    }
                });
                if(bean.isLoading()){
                    holder.getView(R.id.loading).setVisibility(View.VISIBLE);
                }else {
                    holder.getView(R.id.loading).setVisibility(View.GONE);
                }
                if (bean.isSendFail()) {
                    holder.setVisible(R.id.icon_fail, true);
                } else {
                    holder.setVisible(R.id.icon_fail, false);
                }
                break;

            case ImMessageBean.TYPE_IMAGE_L:
                Glide.with(mContext).load(R.mipmap.icon_woman).into((ImageView) holder.getView(R.id.avatar));
                imageFile = bean.getImageFile();
                mImgs.setFile(imageFile);
                if (imageFile != null) {
                    Glide.with(mContext).load(imageFile)
                            .apply(new RequestOptions().placeholder(R.drawable.bg_photo_num))
                            .into(mImgs);
                } else {
                    ImMessageUtil.getInstance().displayImageFile(bean, mImgs);
                }
              /*  holder.getView(R.id.riv_img_square).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showPopWindows(view, 3, bean, holder.getAdapterPosition());
                        return false;
                    }
                });*/
                break;
            case ImMessageBean.TYPE_IMAGE_R:
                Glide.with(mContext).load(R.mipmap.icon_man).into((ImageView) holder.getView(R.id.avatar));
                imageFile = bean.getImageFile();
                mImgs.setFile(imageFile);
                if (imageFile != null) {
                    Glide.with(mContext).load(imageFile)
                            .apply(new RequestOptions().placeholder(R.drawable.bg_photo_num))
                            .into(mImgs);
                } else {
                    ImMessageUtil.getInstance().displayImageFile(bean, mImgs);
                }
              /*  holder.getView(R.id.riv_img_square).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showPopWindows(view, 4, bean, holder.getAdapterPosition());
                        return false;
                    }
                });*/

                if(bean.isLoading()){
                    holder.getView(R.id.loading).setVisibility(View.VISIBLE);
                }else {
                    holder.getView(R.id.loading).setVisibility(View.GONE);
                }
                if (bean.isSendFail()) {
                    holder.setVisible(R.id.icon_fail, true);
                } else {
                    holder.setVisible(R.id.icon_fail, false);
                }
                break;
        }
    }
}
