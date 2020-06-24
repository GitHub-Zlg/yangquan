package app.com.yangquan.adapter;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;
import app.com.yangquan.activity.ChatActivity;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.jiguang.im.ImUserMsgEvent;
import app.com.yangquan.view.BasePopup;
import razerdp.basepopup.BasePopupWindow;

public class ChatListAdapter extends BaseQuickAdapter<ImUserMsgEvent> {
    public ChatListAdapter() {
        super(R.layout.item_msg, null);
    }
    private BasePopup mDemoPopup;
    public boolean blur = false;
    float x, y;

    @Override
    protected void convert(BaseViewHolder holder, ImUserMsgEvent bean) {
        Glide.with(mContext).load(bean.getHeadImg()).placeholder(R.mipmap.icon_avater).into((ImageView) holder.getView(R.id.avater));
        holder.setText(R.id.tv_time,bean.getLastTime()+"");
        holder.setText(R.id.tv_content,bean.getLastMessage()+"");
        holder.setText(R.id.tv_name,bean.getNikeName()+"");
        if(bean.getUnReadCount()>0){
            holder.setText(R.id.tv_unread_num,bean.getUnReadCount()+"");
            holder.setVisible(R.id.tv_unread_num,true);
        }else {
            holder.setVisible(R.id.tv_unread_num,false);
        }

        holder.getView(R.id.ll_root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getRawX();
                        y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x = y = 0;
                        break;
                }
                return false;
            }
        });
        holder.getView(R.id.ll_root).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                showPopup(v,holder.getAdapterPosition());
                return false;
            }
        });

        holder.getView(R.id.ll_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImMessageUtil.getInstance().removeConversation(bean.getUid());
                remove(holder.getAdapterPosition());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.getView(R.id.ll_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImMessageUtil.getInstance().markAllMessagesAsRead(bean.getUid());
                bean.setUnReadCount(0);
                notifyItemChanged(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("uid",bean.getUid());
                intent.putExtra("name",bean.getNikeName());
                mContext.startActivity(intent);
            }
        });
    }

    //长按弹出pop删除，为了增加好友模块，侧滑删除手势冲突，但是pop删除有一点小问题，暂时使用侧滑，以后处理
    private void showPopup(View v, int position) {
        if (mDemoPopup == null) {
            mDemoPopup = new BasePopup(mContext);
            mDemoPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismissAnimationStart() {

                }

                @Override
                public void onDismiss() {

                }
            });
            mDemoPopup.setDeletePopClick(new BasePopup.DeletePopClick() {
                @Override
                public void onDeleteClick() {

                }
            });
        }
//        mDemoPopup.setBackgroundColor(Color.TRANSPARENT);
        mDemoPopup.setBlurBackgroundEnable(blur).showPopupWindow((int) x, (int) y);
    }

    public interface ChatItemClickLinstener {
        void onItemClick(int position);
    }

    private ChatItemClickLinstener listener;

    public void ChatItemClickLinstener(ChatItemClickLinstener listener) {
        this.listener = listener;
    }
}
