package app.com.yangquan.fragment.c;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.ChatListAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.jiguang.im.ImDateUtil;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.jiguang.im.ImUserMsgEvent;
import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

public class ChatListFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private ChatListAdapter adapter;
    private List<ImUserMsgEvent> list = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        initRecycler();
        getMsgList();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化recycler
    private void initRecycler() {
        refresh.setEnableLoadMore(false);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ChatListAdapter(mContext);
        recycler.setAdapter(adapter);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMsgList();
            }
        });
    }

    //收到消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImUserMsgEvent event) {
        int position = getPosition(event.getUid());
        if(position<0){ //会话列表中没有该会话，是新的会话
            adapter.add(0, event);  //放在第一位
        }else {//会话列表中有该回话，收到新的消息
            ImUserMsgEvent item = adapter.getItem(position);
            item.setLastTime(event.getLastTime());
            item.setUnReadCount(event.getUnReadCount());
            item.setLastMessage(event.getLastMessage());
            item.setNikeName(event.getNikeName());
            item.setHeadImg(event.getHeadImg());
            adapter.notifyItemChanged(position);
        }
    }

    //获取会话列表
    private void getMsgList() {
        List<Conversation>  conversationList = JMessageClient.getConversationList();
        refresh.finishRefresh();
        list.clear();
        if (conversationList != null) {
            for (int i = 0; i < conversationList.size(); i++) {
                if (conversationList.get(i).getType() == ConversationType.single) {
                    ImUserMsgEvent event = new ImUserMsgEvent();
                    Conversation conversation = JMessageClient.getSingleConversation(conversationList.get(i).getTargetId(), ImMessageUtil.IM_KEY);
                    event.setUid(conversationList.get(i).getTargetId());
                    event.setUnReadCount(conversation.getUnReadMsgCnt());
                    UserInfo targetInfo = (UserInfo) conversationList.get(i).getTargetInfo();
                    event.setHeadImg(targetInfo.getAvatar());
                    event.setNikeName(targetInfo.getNickname());
                    event.setLastTime(ImDateUtil.getTimestampString(conversation.getLastMsgDate()));
                    if (conversation.getLatestType() == ContentType.image) { //如果最后一条消息是图片
                        event.setLastMessage("[图片]");
                    } else if (conversation.getLatestType() == ContentType.voice) {
                        event.setLastMessage("[语音]");
                    } else if (conversation.getLatestType() == ContentType.custom) {
                        event.setLastMessage("[分享]");
                    } else {
                        event.setLastMessage(conversation.getLatestText());
                    }
                    list.add(event);
                }
            }
        }
        adapter.setNewData(list);
    }

    //判断是否存在该会话
    public int getPosition(String uid) {
        for (int i = 0, size = adapter.getData().size(); i < size; i++) {
            if (adapter.getItem(i).getUid().equals(uid)) {
                return i;
            }
        }
        return -1;
    }

}
