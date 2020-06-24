package app.com.yangquan.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.ChatAdapter;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.jiguang.im.ImMessageBean;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.jiguang.im.ImUserMsgEvent;
import app.com.yangquan.listener.SoftKeyBoardListener;
import app.com.yangquan.util.ChatUiHelper;
import app.com.yangquan.util.DateUtils;
import app.com.yangquan.util.DpUtil;
import app.com.yangquan.util.FileSaveUtil;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.util.WordUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.EasyPermissions;

public class ChatActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ivAudio)
    ImageView ivAudio;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.ivEmo)
    ImageView ivEmo;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.btn_send)
    TextView btnSend;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    LinearLayout homeEmoji;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.rlPhoto)
    RelativeLayout rlPhoto;
    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.rlCamera)
    RelativeLayout rlCamera;
    @BindView(R.id.ivFile)
    ImageView ivFile;
    @BindView(R.id.rlFile)
    RelativeLayout rlFile;
    @BindView(R.id.ivLocation)
    ImageView ivLocation;
    @BindView(R.id.rlLocation)
    RelativeLayout rlLocation;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @BindView(R.id.llAdd)
    LinearLayout llAdd;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.llFace)
    LinearLayout llFace;
    private String uid;
    private String name;
    private ChatAdapter adapter;
    private boolean isVoice = false;
    private String[] voice_permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String[] photo_permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ChatUiHelper mUiHelper;//底部功能栏
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        initChatUi();
        EventBus.getDefault().register(this);
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");
        tvRight.setText("设置");
        tvTitle.setText(name);
        initRecycler();
    }

    private void initRecycler() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter();
        recycler.setAdapter(adapter);
        List<ImMessageBean> chatMessageList = ImMessageUtil.getInstance().getChatMessageList(uid); //获取该会话所有消息
        adapter.setNewData(chatMessageList);
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, -DpUtil.dp2px(20));
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //收到消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImMessageBean event) {
        Log.e("zlg", "聊天页面");
        insertItem(event);
        ImMessageUtil.getInstance().markAllMessagesAsRead(uid);

    }

    //设置底部按钮绑定
    private void initChatUi() {
        mUiHelper = ChatUiHelper.with(mContext);
        mUiHelper.bindContentLayout(llContent)
                .bindttToSendButton(btnSend)
                .bindEditText(etContent)
                .bindBottomLayout(bottomLayout)
                .bindEmojiLayout(llFace)
                .bindAddLayout(llAdd)
                .bindToAddButton(ivAdd)
                .bindToEmojiButton(ivEmo)
                .bindEmojiData();
        //底部布局弹出,聊天列表上滑
        recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            ivAudio.setImageResource(R.mipmap.ic_audio);
                            etContent.requestFocus();
                            etContent.setVisibility(View.VISIBLE);
                            tvVoice.setVisibility(View.GONE);
                            isVoice = false;
                            if (adapter.getItemCount() > 0) {
                                recycler.smoothScrollToPosition(adapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });
        //点击空白区域关闭键盘
        recycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                etContent.clearFocus();
                ivEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });
    }

    private void clickAudio() {
        mUiHelper.hideBottomLayout(false);
        mUiHelper.hideSoftInput();
        etContent.clearFocus();
        ivEmo.setImageResource(R.mipmap.ic_emoji);
        if (isVoice) {
            ivAudio.setImageResource(R.mipmap.ic_audio);
            etContent.setVisibility(View.VISIBLE);
            tvVoice.setVisibility(View.GONE);
            isVoice = false;
        } else {
            if (getVoicePermission()) {
                ivAudio.setImageResource(R.mipmap.ic_keyboard);
                etContent.setVisibility(View.GONE);
                tvVoice.setVisibility(View.VISIBLE);
                isVoice = true;
            }
        }
    }

    //请求录音储存权限
    private boolean getVoicePermission() {
        if (EasyPermissions.hasPermissions(this, voice_permissions)) {
            //已经打开权限
            return true;
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的录音、储存使用权限", 2, voice_permissions);
            return false;
        }
    }

    //请求录音储存权限
    private boolean getPhotoPermission() {
        if (EasyPermissions.hasPermissions(this, photo_permissions)) {
            //已经打开权限
            return true;
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的拍照、储存使用权限", 3, photo_permissions);
            return false;
        }
    }

    //发送文字消息
    private void sendText() {
        String mcgContent = etContent.getText().toString();
        if (TextUtils.isEmpty(mcgContent)) {
            ToastUtil.show("怎么也得说两个字吧");
            return;
        }
        ImMessageBean textMessage = ImMessageUtil.getInstance().createTextMessage(uid, mcgContent); //创建文本消息
        if (textMessage != null) {
            sendMsg(textMessage);
        }
    }

    //发送消息
    private void sendMsg(ImMessageBean messageBean) {
        Message rawMessage = messageBean.getRawMessage();
        messageBean.setLoading(true);
        insertItem(messageBean);
        if (rawMessage != null) {
            ImMessageUtil.getInstance().sendMessage(rawMessage, uid);
            rawMessage.setOnSendCompleteCallback(new BasicCallback() {
                int position = getPosition(rawMessage.getId());

                @Override
                public void gotResult(int i, String s) {
                    if (i != 0) {
                        messageBean.setLoading(false);
                        etContent.setText("");
                        messageBean.setSendFail(true);
                        adapter.notifyItemChanged(position);
                        if (i == 803008) {
                            ToastUtil.show("您已被对方拉黑");
                        } else {
                            ToastUtil.show("消息发送失败" + s + i);
                        }
                    } else {
                        messageBean.setLoading(false);
                        messageBean.setSendFail(false);
                        etContent.setText("");
                        adapter.notifyItemChanged(position);
                        Log.e("zlg", "消息发送成功");
                    }
                }
            });
        }
    }

    //插入新消息
    private void insertItem(ImMessageBean event) {
        ImUserMsgEvent imUserMsgEvent = new ImUserMsgEvent();
        imUserMsgEvent.setUid(uid);
        imUserMsgEvent.setLastTime(ImMessageUtil.getInstance().getMessageTimeString(event.getRawMessage().getCreateTime()));
        if (event.getItemType() == 3 || event.getItemType() == 4) {
            imUserMsgEvent.setLastMessage("[图片]");
        } else if (event.getItemType() == 5 || event.getItemType() == 6) {
            imUserMsgEvent.setLastMessage("[语音]");
        } else if (event.getItemType() == 10 || event.getItemType() == 11 || event.getItemType() == 12 || event.getItemType() == 13) {
            imUserMsgEvent.setLastMessage("[分享]");
        } else {
            imUserMsgEvent.setLastMessage(((TextContent) event.getRawMessage().getContent()).getText());
        }
        Message msg = event.getRawMessage();
        UserInfo userInfo = (UserInfo) msg.getTargetInfo();
        String nickname = userInfo.getNickname();
        String avatar = userInfo.getAvatar();
        imUserMsgEvent.setHeadImg(avatar);
        imUserMsgEvent.setNikeName(nickname);
        imUserMsgEvent.setUnReadCount(0);
        if (event.isFromSelf()) {
            EventBus.getDefault().post(imUserMsgEvent);
        }
        adapter.add(adapter.getItemCount(), event);
        adapter.notifyItemChanged(adapter.getItemCount());
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, -DpUtil.dp2px(20));
    }


    //判断是否存在该会话
    public int getPosition(int id) {
        for (int i = 0, size = adapter.getData().size(); i < size; i++) {
            ImMessageBean item = (ImMessageBean) adapter.getItem(i);
            if (item.getRawMessage().getId() == id) {
                return i;
            }
        }
        return -1;
    }


    @OnClick({R.id.ivAudio, R.id.btn_send, R.id.rlPhoto, R.id.rlCamera, R.id.iv_back, R.id.tv_right, R.id.tv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.tv_voice:
                break;
            case R.id.ivAudio:
                clickAudio();
                break;
            case R.id.btn_send:
                sendText();
                break;
            case R.id.rlPhoto:
                ToastUtil.show("相册");
                break;
            case R.id.rlCamera:
                if (getPhotoPermission()) {
                    PhotoUtil.pictureCamera(mContext, 1);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedia.size(); i++) {
                        ImMessageBean messageBean = ImMessageUtil.getInstance().createImageMessage(uid, localMedia.get(i).getCompressPath().contains("file://")
                                ? localMedia.get(i).getCompressPath().replace("file://", "") : localMedia.get(i).getCompressPath());
                        if (messageBean != null) {
                            sendMsg(messageBean);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter.getItemCount() > 0) {
            ImMessageUtil.getInstance().refreshLastMessage(uid, (ImMessageBean) adapter.getItem(adapter.getItemCount() - 1));
        }
        EventBus.getDefault().unregister(this);
    }
}
