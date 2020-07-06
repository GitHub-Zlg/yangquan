package app.com.yangquan.activity;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnEditFocusChangeListener;
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.interfaces.listener.OnViewClickListener;
import com.effective.android.panel.view.PanelSwitchLayout;
import com.effective.android.panel.view.content.LinearContentContainer;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelContainer;
import com.effective.android.panel.view.panel.PanelView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import app.com.yangquan.R;
import app.com.yangquan.adapter.ChatAdapter;
import app.com.yangquan.adapter.ImChatFacePagerAdapter;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.jiguang.im.ImMessageBean;
import app.com.yangquan.jiguang.im.ImMessageUtil;
import app.com.yangquan.jiguang.im.ImUserMsgEvent;
import app.com.yangquan.listener.OnFaceClickListener;
import app.com.yangquan.util.DpUtil;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.TextRender;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.EasyPermissions;

public class ChatActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_btn)
    ImageView addBtn;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.emotion_btn)
    ImageView emotionBtn;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.bottom_action)
    LinearLayout bottomAction;
    @BindView(R.id.content_view)
    LinearContentContainer contentView;
    @BindView(R.id.panel_add)
    PanelView panelAdd;
    @BindView(R.id.panel_container)
    PanelContainer panelContainer;
    @BindView(R.id.panel_switch_layout)
    PanelSwitchLayout panelSwitchLayout;
    @BindView(R.id.panel_emj)
    PanelView panelEmj;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.loading)
    SpinKitView loading;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    private PanelSwitchHelper mHelper;
    private String uid;
    private String name;
    private ChatAdapter adapter;
    private String[] voice_permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String[] photo_permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPanelSwitchHelper();
        initFaceView();
        initAddView();
    }


    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        initData();
        initTitle();
        initRecycler();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化数据
    private void initData() {
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");
    }

    //初始化title
    private void initTitle() {
        tvRight.setText("设置");
        tvTitle.setText(name);
    }

    //初始化recycler
    private void initRecycler() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);
        List<ImMessageBean> chatMessageList = ImMessageUtil.getInstance().getChatMessageList(uid); //获取该会话所有消息
        adapter.setNewData(chatMessageList);
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, -DpUtil.dp2px(20));
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHelper.resetState();
                        break;
                }
                return false;
            }
        });
    }

    //收到消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImMessageBean event) {
        insertItem(event);
        ImMessageUtil.getInstance().markAllMessagesAsRead(uid);
    }

    //发送文字消息
    private void sendText() {
        String mcgContent = editText.getText().toString().trim();
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
                        editText.setText("");
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
                        editText.setText("");
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

    //初始化add功能面板
    private void initAddView() {
        View v = panelAdd.getRootView();
        RelativeLayout rlPhotot = v.findViewById(R.id.rlPhoto);
        RelativeLayout rlCamera = v.findViewById(R.id.rlCamera);
        rlPhotot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("相册");
            }
        });

        rlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPhotoPermission()) {
                    PhotoUtil.pictureCamera(mContext, 1);
                }
            }
        });
    }

    //初始化表情控件
    private View initFaceView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = panelEmj.getRootView();
        v.measure(0, 0);
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);
        ImChatFacePagerAdapter adapter = new ImChatFacePagerAdapter(mContext, new OnFaceClickListener() {
            @Override
            public void onFaceClick(String str, int faceImageRes) {
                if (editText != null) {
                    Editable editable = editText.getText();
                    editable.insert(editText.getSelectionStart(), TextRender.getFaceImageSpan(str, faceImageRes));
                }
            }

            @Override
            public void onFaceDeleteClick() {
                if (editText != null) {
                    int selection = editText.getSelectionStart();
                    String text = editText.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1, selection);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[", selection);
                            if (start >= 0) {
                                editText.getText().delete(start, selection);
                            } else {
                                editText.getText().delete(selection - 1, selection);
                            }
                        } else {
                            editText.getText().delete(selection - 1, selection);
                        }
                    }
                }
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0, pageCount = adapter.getCount(); i < pageCount; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_chat_indicator, radioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        return v;
    }

    //初始化功能面板监听
    private void initPanelSwitchHelper() {
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this).addKeyboardStateListener(new OnKeyboardStateListener() {
                @Override
                public void onKeyboardChange(boolean b, int i) {//可选实现，监听输入法变化

                }
            }).addEditTextFocusChangeListener(new OnEditFocusChangeListener() {
                @Override
                public void onFocusChange(@Nullable View view, boolean b) { //可选实现，监听输入框焦点变化

                }
            }).addViewClickListener(new OnViewClickListener() {
                @Override
                public void onClickBefore(@Nullable View view) {//可选实现，监听触发器的点击

                }
            }).addPanelChangeListener(new OnPanelChangeListener() {
                @Override
                public void onKeyboard() {//可选实现，输入法显示回调

                }

                @Override
                public void onNone() {//可选实现，默认状态回调

                }

                @Override
                public void onPanel(@Nullable IPanelView iPanelView) {//可选实现，面板显示回调

                }

                @Override//可选实现，输入法动态调整时引起的面板高度变化动态回调
                public void onPanelSizeChange(@Nullable IPanelView iPanelView, boolean b, int i, int i1, int i2, int i3) {

                }
            }).contentCanScrollOutside(true)
                    .logTrack(true)
                    .build(true);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    send.setVisibility(View.VISIBLE);
                } else {
                    send.setVisibility(View.GONE);
                }
            }
        });
    }

    //请求拍照，相册权限
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
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

    @OnClick({R.id.iv_back, R.id.send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.send:
                sendText();
                break;
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
