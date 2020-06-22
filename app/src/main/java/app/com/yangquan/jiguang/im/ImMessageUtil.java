package app.com.yangquan.jiguang.im;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.yangquan.App;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.view.MyImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

/**
 * 极光IM注册、登陆等功能
 */

public class ImMessageUtil {
    private static final String TAG = "zlg";
    private static final String PWD_SUFFIX = "PUSH";//注册极光IM的时候，密码是用户id+"PUSH"这个常量构成的
    public static final String PREFIX = "";    //前缀，当uid不够长的时候无法注册
    public static final String IM_KEY = "d7564a40af5ef6dd8d4ace34";
    private Map<String, Long> mMap;
    private MessageSendingOptions mOptions;    //针对消息发送动作的控制选项
    private SimpleDateFormat mSimpleDateFormat;
    private String mImageString;
    private String mVoiceString;
    private String mLocationString;
    private static ImMessageUtil sInstance;

    private ImMessageUtil() {
        mMap = new HashMap<>();
        mOptions = new MessageSendingOptions();
        mOptions.setShowNotification(true);//设置针对本次消息发送，是否需要在消息接收方的通知栏上展示通知
        mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    }

    public static ImMessageUtil getInstance() {
        if (sInstance == null) {
            synchronized (ImMessageUtil.class) {
                if (sInstance == null) {
                    sInstance = new ImMessageUtil();
                }
            }
        }
        return sInstance;
    }


    public void init() {
        //开启消息漫游
        JMessageClient.init(App.getInstance(), true);
    }

    /**
     * 将app中用户的uid转成IM用户的uid
     */
    private String getImUid(String uid) {
        if (uid.length() < 5) {
            return "yangquan" + uid;
        } else {
            return uid;
        }
    }

    /**
     * 将IM用户的uid转成app用户的uid
     */
    private String getAppUid(String from) {
        if (!TextUtils.isEmpty(from) && from.length() > PREFIX.length()) {
            return from.substring(PREFIX.length());
        }
        return "";
    }

    /**
     * 根据极光IM conversation 获取App用户的uid
     */
    private String getAppUid(Conversation conversation) {
        if (conversation == null) {
            return "";
        }
        Object targetInfo = conversation.getTargetInfo();
        if (targetInfo == null) {
            return "";
        }
        if (targetInfo instanceof UserInfo) {
            String userName = ((UserInfo) targetInfo).getUserName();
            return getAppUid(userName);
        }
        return "";
    }


    /**
     * 根据极光IM发的消息 获取App用户的uid
     */
    private String getAppUid(Message msg) {
        if (msg == null) {
            return "";
        }
        UserInfo userInfo = msg.getFromUser();
        if (userInfo == null) {
            return "";
        }
        String userName = userInfo.getUserName();
        return getAppUid(userName);
    }

    /**
     * 登录极光IM
     */
    public void loginJMessage(String uid, Context context) {
       /* if (PreferencesUtils.getSharePreBoolean(context, Const.SharePre.im_login)) {
            Log.e(TAG, "极光IM已经登录了");
            JMessageClient.registerEventReceiver(ImMessageUtil.this);
            //EventBus.getDefault().post(new ImLoginEvent(true));
            Log.e("zlg", "登录刷新未读");
            refreshAllUnReadMsgCount();
            return;
        }*/
        final String imUid = getImUid(uid);
        Log.e(TAG, "登录刷新未读2=====" + imUid);

        JMessageClient.login(imUid, "123456", new BasicCallback() {
            @Override
            public void gotResult(int code, String msg) {
                Log.e(TAG, "登录极光回调---gotResult--->code: " + code + " msg: " + msg);
                if (code == 801003) {//用户不存在
                    Log.e(TAG, "未注册，用户不存在");
                    registerAndLoginJMessage(imUid, context);
                } else if (code == 0) {
                    Log.e(TAG, "极光IM登录成功");
//                    SpUtil.getInstance().setBooleanValue(SpUtil.IM_LOGIN, true);
                    JMessageClient.registerEventReceiver(ImMessageUtil.this);
                    //EventBus.getDefault().post(new ImLoginEvent(true));
                    Log.e(TAG, "登录刷新未读2=====" + imUid);
                    refreshAllUnReadMsgCount();
                }else {
                    Log.e("zlg","登录失败后再次登录");
                    loginJMessage(uid,context);
                }
            }
        });
    }

    //注册并登录极光IM
    private void registerAndLoginJMessage(final String uid, Context context) {
        JMessageClient.register(uid, "123456", new BasicCallback() {
            @Override
            public void gotResult(int code, String msg) {
                Log.e(TAG, "注册极光回调---gotResult--->code: " + code + " msg: " + msg);
                if (code == 0) {
                    Log.e(TAG, "极光IM注册成功");
                    loginJMessage(uid, context);
                }
            }
        });
    }

    /**
     * 登出极光IM
     */
    public void logoutEMClient() {
        JMessageClient.unRegisterEventReceiver(ImMessageUtil.this);
        JMessageClient.logout();
        //EventBus.getDefault().post(new ImLoginEvent(false));
        Log.e(TAG, "极光IM登出");
    }

    /**
     * 获取会话列表用户的uid，多个uid以逗号分隔
     */
    public String getConversationUids() {
        List<Conversation> conversationList = JMessageClient.getConversationList();
        String uids = "";
        if (conversationList != null) {
            for (Conversation conversation : conversationList) {
                Object targetInfo = conversation.getTargetInfo();
                if (targetInfo == null || !(targetInfo instanceof UserInfo)) {
                    continue;
                }
                List<Message> messages = conversation.getAllMessage();
                if (messages == null || messages.size() == 0) {
                    String userName = ((UserInfo) targetInfo).getUserName();
                    JMessageClient.deleteSingleConversation(userName);
                    continue;
                }
                String from = getAppUid(conversation);
                if (!TextUtils.isEmpty(from)) {
                    uids += from;
                    uids += ",";
                }
            }
        }
        if (uids.endsWith(",")) {
            uids = uids.substring(0, uids.length() - 1);
        }
        return uids;
    }

    /**
     * 获取会话的最后一条消息的信息
     */
    public List<ImUserBean> getLastMsgInfoList(List<ImUserBean> list) {
        if (list == null) {
            return null;
        }
        for (ImUserBean bean : list) {
            Conversation conversation = JMessageClient.getSingleConversation(getImUid(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId)));
            if (conversation != null) {
                bean.setHasConversation(true);
                Message msg = conversation.getLatestMessage();
                if (msg != null) {

                    bean.setLastTime(getMessageTimeString(msg));
                    bean.setUnReadCount(conversation.getUnReadMsgCnt());
                    bean.setMsgType(getMessageType(msg));
                    bean.setLastMessage(getMessageString(msg));
                }
            } else {
                bean.setHasConversation(false);
            }
        }
        return list;
    }

    /**
     * 获取消息列表
     */
    public List<ImMessageBean> getChatMessageList(String toUid) {
        List<ImMessageBean> result = new ArrayList<>();
        Conversation conversation = JMessageClient.getSingleConversation(PREFIX + toUid);
        if (conversation == null) {
            return result;
        }
        List<Message> msgList = conversation.getAllMessage();
        if (msgList == null) {
            return result;
        }
        int size = msgList.size();
        if (size < 20) {
            Message latestMessage = conversation.getLatestMessage();
            if (latestMessage == null) {
                return result;
            }
            List<Message> list = conversation.getMessagesFromNewest(latestMessage.getId(), 20 - size);
            if (list == null) {
                return result;
            }
            list.addAll(msgList);
            msgList = list;
        }
        String uid = "yangquan"+PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId);
        for (Message msg : msgList) {
            String from = getAppUid(msg);
            int type = getMessageType(msg);
            if (!TextUtils.isEmpty(from) && type != 0) {
                boolean self = from.equals(uid);
                ImMessageBean bean = new ImMessageBean(from, msg, type, self);
                if (self && msg.getServerMessageId() == 0 || msg.getStatus() == MessageStatus.send_fail) {
                    bean.setSendFail(true);
                }
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * 获取某个会话的未读消息数
     */
    public int getUnReadMsgCount(String uid) {
        Conversation conversation = JMessageClient.getSingleConversation(getImUid(uid));
        if (conversation != null) {
            return conversation.getUnReadMsgCnt();
        }
        return 0;
    }

    /**
     * 刷新全部未读消息的总数
     */
    public void refreshAllUnReadMsgCount() {
        EventBus.getDefault().post(new ImUnReadCountEvent(getAllUnReadMsgCount()));
    }

    /**
     * 获取全部未读消息的总数
     */
    public String getAllUnReadMsgCount() {
        int unReadCount = JMessageClient.getAllUnReadMsgCount();
        Log.e(TAG, "未读消息总数----->" + unReadCount);
        String res = "";
        if (unReadCount > 99) {
            res = "99+";
        } else {
            if (unReadCount < 0) {
                unReadCount = 0;
            }
            res = String.valueOf(unReadCount);
        }
        Log.e(TAG, "res====" + res);
        return res;
    }

    /**
     * 设置某个会话的消息为已读
     *
     * @param toUid 对方uid
     */
    public boolean markAllMessagesAsRead(String toUid) {
        if (!TextUtils.isEmpty(toUid)) {
            Conversation conversation = JMessageClient.getSingleConversation(getImUid(toUid));
            if (conversation != null) {
                conversation.resetUnreadCount();
                Log.e(TAG, "已读");
                return true;
            }
        }
        return false;
    }

    /**
     * 标记所有会话为已读  即忽略所有未读
     */
    public void markAllConversationAsRead() {
        List<Conversation> list = JMessageClient.getConversationList();
        if (list == null) {
            return;
        }
        for (Conversation conversation : list) {
            conversation.resetUnreadCount();
        }
        EventBus.getDefault().post(new ImUnReadCountEvent("0"));
    }


    /**
     * 传透消息
     * 消息透传发送的内容后台不会为其离线保存，只会在对方用户在线的前提下将内容推送给对方。
     * SDK 收到命令之后也不会本地保存，不发送通知栏通知，整体快速响应。
     */
//    public void onEvent(CommandNotificationEvent e){
//        L.e("onCommandNotificationEvent-------->"+e.getMsg());
//    }

    /**
     * 接收消息 目前是在子线程接收的
     */
    public void onEvent(MessageEvent event) {
        //收到消息
        Message msg = event.getMessage();
        if (msg == null) {
            return;
        }
        String uid = getAppUid(msg);
        Log.e("zlg", "收到来自消息uid====" + uid);
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        int type = getMessageType(msg);
        if (type == 0) {
            return;
        }
        boolean canShow = true;
        Long lastTime = mMap.get(uid);
        long curTime = System.currentTimeMillis();
        if (lastTime != null) {
            if (curTime - lastTime < 1000) {
                //同一个人，上条消息距离这条消息间隔不到1秒，则不显示这条消息
                msg.setHaveRead(null);//设置为已读
                canShow = false;
            } else {
                mMap.put(uid, curTime);
            }
        } else {
            //说明sMap内没有保存这个人的信息，则是首次收到这人的信息，可以显示
            mMap.put(uid, curTime);
        }
        if (canShow) {
            Log.e(TAG, "显示消息--->" + getUnReadMsgCount(uid));
            EventBus.getDefault().post(new ImMessageBean(uid, msg, type, false));
            ImUserMsgEvent imUserMsgEvent = new ImUserMsgEvent();
            imUserMsgEvent.setUid(uid);
            int messageType = getMessageType(msg); //如果收到的是图片消息，就在消息列表中的最后一条消息改为图片
            if (messageType == 3 || messageType == 4) {
                imUserMsgEvent.setLastMessage("[图片]");
            } else if (messageType == 5 || messageType == 6) {
                imUserMsgEvent.setLastMessage("[语音]");
            } else if (messageType == 10 || messageType == 11 || messageType == 12 || messageType == 13) {
                imUserMsgEvent.setLastMessage("[分享]");
            } else {
                imUserMsgEvent.setLastMessage(getMessageString(msg));
            }
            imUserMsgEvent.setUnReadCount(getUnReadMsgCount(uid));
            imUserMsgEvent.setLastTime(getMessageTimeString(msg));
            UserInfo userInfo = (UserInfo) msg.getTargetInfo();
//            String avatar = msg.getFromUser().getAvatar();
//            String nickname = msg.getFromUser().getNickname();
            String nickname = userInfo.getNickname();
            String avatar = userInfo.getAvatar();
            imUserMsgEvent.setNikeName(nickname);
            imUserMsgEvent.setHeadImg(avatar);
            EventBus.getDefault().post(imUserMsgEvent);
            Log.e("zlg", "头像====" + avatar);
            Log.e("zlg", "昵称====" + nickname);
            refreshAllUnReadMsgCount();
        }
    }

    public void onEvent(CommandNotificationEvent event){
        EventBus.getDefault().post(event);
    }

    /*
     * 接受聊天室消息
     * */
    public void onEvent(ChatRoomMessageEvent event) {
        List<Message> messages = event.getMessages();
        EventBus.getDefault().post(event);
        Log.e("zlg", "收到聊天室消息====" + messages.size());
       /* List<ChatRoomMyBean> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            ChatRoomMyBean bean = new ChatRoomMyBean();
            String text = ((TextContent) messages.get(i).getContent()).getText();
            bean.setContent(text);
            bean.setNike_name(messages.get(i).getFromUser().getNickname());
            bean.setPhoto(messages.get(i).getFromUser().getAvatar());
            bean.setUid(messages.get(i).getFromUser().getUserName());
            list.add(bean);
            EventBus.getDefault().post(bean);
        }*/
         /*   first = 1;
        } else {
            ChatRoomMyBean bean = new ChatRoomMyBean();
            String text = ((TextContent) messages.get(messages.size() - 1).getContent()).getText();
            bean.setContent(text);
            bean.setNike_name(messages.get(messages.size() - 1).getFromUser().getNickname());
            bean.setPhoto(messages.get(messages.size() - 1).getFromUser().getAvatar());
            bean.setUid(messages.get(messages.size() - 1).getFromUser().getUserName());
            EventBus.getDefault().post(bean);
        }*/
    }

    /**
     * 接收离线消息
     */
    public void onEvent(OfflineMessageEvent event) {
        String from = getAppUid(event.getConversation());
        Log.e(TAG, "接收到离线消息-------->来自：" + from);
        if (!TextUtils.isEmpty(from) && !from.equals(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId))) {
            List<Message> list = event.getOfflineMessageList();
            if (list != null && list.size() > 0) {
                ImUserBean bean = new ImUserBean();
//                bean.setId(from);
                Message message = list.get(list.size() - 1);
                bean.setLastTime(getMessageTimeString(message));
                bean.setUnReadCount(list.size());
                bean.setMsgType(getMessageType(message));
                bean.setLastMessage(getMessageString(message));
                EventBus.getDefault().post(new ImOffLineMsgEvent(bean));
                Log.e("zlg", "接受到离线消息刷新未读");
                refreshAllUnReadMsgCount();
            }
        }
    }

    /*
     * 被撤回方通知事件MessageRetractEvent
     * */
    public void onEvent(MessageRetractEvent event) {
        Message retractedMessage = event.getRetractedMessage();
        RetractEvent retractEvent = new RetractEvent();
        String userName = retractedMessage.getFromUser().getUserName();
        retractEvent.setMessage(retractedMessage);
        if (userName.equals(App.getInstance().getUid())) { //自己撤回
            retractEvent.setMySelf(true);
            EventBus.getDefault().post(retractEvent);
        } else { //对方撤回
            retractEvent.setMySelf(false);
            EventBus.getDefault().post(retractEvent);
        }
    }

    /**
     * 获取消息的类型
     */
    public int getMessageType(Message msg) {
        int type = 0;
        if (msg == null) {
            return type;
        }
        String uid = "yangquan"+App.getInstance().getUid();
        MessageContent content = msg.getContent();
        if (content == null) {
            return type;
        }
        switch (content.getContentType()) {
            case text://文字
                if (getAppUid(msg).equals(uid)) {
                    type = ImMessageBean.TYPE_TEXT_R;
                } else {
                    type = ImMessageBean.TYPE_TEXT_L;
                }
                break;
            case image://图片
                if (getAppUid(msg).equals(uid)) {
                    type = ImMessageBean.TYPE_IMAGE_R;
                } else {
                    type = ImMessageBean.TYPE_IMAGE_L;
                }
                break;
            case voice://语音
//                type = ImMessageBean.TYPE_VOICE;
                if (getAppUid(msg).equals(uid)) {
                    type = ImMessageBean.TYPE_VOICE_R;
                } else {
                    type = ImMessageBean.TYPE_VOICE_L;
                }
                break;
            case location://位置
//                type = ImMessageBean.TYPE_LOCATION;
                break;
            case custom:
                CustomContent content1 = (CustomContent) msg.getContent();
                if (getAppUid(msg).equals(uid)) {
                    if (TextUtils.isEmpty(content1.getStringValue("thPositiveId"))) {
                        type = ImMessageBean.TYPE_CUSTOM_HOME_R;
                    } else {
                        type = ImMessageBean.TYPE_CUSTOM_R;
                    }
                } else {
                    if (TextUtils.isEmpty(content1.getStringValue("thPositiveId"))) {
                        type = ImMessageBean.TYPE_CUSTOM_HOME_L;
                    } else {
                        type = ImMessageBean.TYPE_CUSTOM_L;
                    }
                }
                break;
        }
        return type;
    }


    /**
     * 接收漫游消息
     */
    public void onEvent(ConversationRefreshEvent event) {
        Conversation conversation = event.getConversation();
        String from = getAppUid(conversation);
        Log.e(TAG, "接收到漫游消息-------->来自：" + from);
        if (!TextUtils.isEmpty(from) && !from.equals(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId))) {
            Message message = conversation.getLatestMessage();
            ImUserBean bean = new ImUserBean();
//            bean.setId(from);
            bean.setLastTime(getMessageTimeString(message));
            bean.setUnReadCount(conversation.getUnReadMsgCnt());
            bean.setMsgType(getMessageType(message));
            bean.setLastMessage(getMessageString(message));
            EventBus.getDefault().post(new ImRoamMsgEvent(bean));
            Log.e("zlg", "漫游消息刷新未读");
            refreshAllUnReadMsgCount();
        }
    }

    /**
     * 返回消息的字符串描述
     */
    public String getMessageString(Message message) {
        String result = "";
        MessageContent content = message.getContent();
        if (content == null) {
            return result;
        }
        switch (content.getContentType()) {
            case text://文字
                result = ((TextContent) content).getText();
                break;
            case image://图片
                result = mImageString;
                break;
            case voice://语音
                result = mVoiceString;
                break;
            case location://位置
                result = mLocationString;
                break;
        }
        return result;
    }

    public String getMessageTimeString(Message message) {
        long createTime = message.getCreateTime();
        Log.e("zlg", "时间戳====" + createTime);
        return mSimpleDateFormat.format(new Date(message.getCreateTime()));
    }

    public String getMessageTimeString(long time) {
        return mSimpleDateFormat.format(new Date(time));
    }

    /**
     * 创建文本消息
     *
     * @param toUid
     * @param content
     * @return
     */
    public ImMessageBean createTextMessage(String toUid, String content) {
        Message message = JMessageClient.createSingleTextMessage(PREFIX + toUid, content);
        if (message == null) {
            return null;
        }
        return new ImMessageBean(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId), message, ImMessageBean.TYPE_TEXT_R, true);
    }

    /*
     * 创建动态自定义消息
     * */
    public ImMessageBean createCustomMessage(String toUid, Map<String, String> map) {
        Message customMessage = JMessageClient.createSingleCustomMessage(toUid, ImMessageUtil.IM_KEY, map);
        if (customMessage == null) {
            return null;
        }
        return new ImMessageBean(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId), customMessage, ImMessageBean.TYPE_CUSTOM_R, true);
    }

    /*
     * 创建个人主页自定义消息
     * */
    public ImMessageBean createCustomHomeMessage(String toUid, Map<String, String> map) {
        Message customMessage = JMessageClient.createSingleCustomMessage(toUid, ImMessageUtil.IM_KEY, map);
        if (customMessage == null) {
            return null;
        }
        return new ImMessageBean(PreferencesUtils.getSharePreStr(App.getInstance(), Const.SharePre.userId), customMessage, ImMessageBean.TYPE_CUSTOM_HOME_R, true);
    }

    /**
     * 创建图片消息
     *
     * @param toUid 对方的id
     * @param path  图片路径
     * @return
     */
    public ImMessageBean createImageMessage(String toUid, String path) {
        App appConfig = App.getInstance();
        String appKey = IM_KEY;
        try {
            Log.e("zlg", "PREFIX + toUid====" + PREFIX + toUid);
            Log.e("zlg", "path====" + path);
            Log.e("zlg", "appKey====" + appKey);
            Message message = JMessageClient.createSingleImageMessage(PREFIX + toUid, appKey, new File(path));
            ImMessageBean imMessageBean = new ImMessageBean(App.getInstance().getUid(), message, ImMessageBean.TYPE_IMAGE_R, true);
            imMessageBean.setImageFile(new File(path));
            return imMessageBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片文件
     */
    public void displayImageFile(final ImMessageBean bean, final ImageView imageViews) {
        if (bean != null) {
            Message msg = bean.getRawMessage();
            if (msg != null) {
                MessageContent messageContent = msg.getContent();
                if (messageContent instanceof ImageContent) {
                    final ImageContent imageContent = (ImageContent) messageContent;
                    String localPath = imageContent.getLocalPath();
                    if (!TextUtils.isEmpty(localPath)) {
                        File file = new File(localPath);
                        if (file.exists()) {
                            bean.setImageFile(file);
                            if (imageViews instanceof MyImageView) {
                                ((MyImageView) imageViews).setFile(file);
                            }

                            ImgLoader.display(file, imageViews);
                            return;
                        }
                    }
                    imageContent.downloadOriginImage(msg, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            bean.setImageFile(file);
                            if (imageViews instanceof MyImageView) {
                                ((MyImageView) imageViews).setFile(file);
                            }

                            ImgLoader.display(file, imageViews);
                        }
                    });
                }
            }
        }
    }

    /**
     * 获取语音文件
     */
    public void getVoiceFile(final ImMessageBean bean, final CommonCallback<File> commonCallback) {
        if (bean != null && commonCallback != null) {
            Message msg = bean.getRawMessage();
            if (msg != null) {
                MessageContent messageContent = msg.getContent();
                if (messageContent instanceof VoiceContent) {
                    final VoiceContent voiceContent = (VoiceContent) messageContent;
                    String localPath = voiceContent.getLocalPath();
                    if (!TextUtils.isEmpty(localPath)) {
                        File file = new File(localPath);
                        if (file.exists()) {
                            commonCallback.callback(file);
                            return;
                        }
                    }
                    voiceContent.downloadVoiceFile(msg, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            if (file != null && file.exists()) {
                                commonCallback.callback(file);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * 创建位置消息
     * @param toUid
     * @param lat     纬度
     * @param lng     经度
     * @param scale   缩放比例
     * @param address 位置详细地址
     * @return
     */
    public ImMessageBean createLocationMessage(String toUid, double lat, double lng, int scale, String address) {
        String appKey = IM_KEY;
        Message message = JMessageClient.createSingleLocationMessage(PREFIX + toUid, appKey, lat, lng, scale, address);
        return new ImMessageBean(App.getInstance().getUid(), message, ImMessageBean.TYPE_LOCATION, true);
    }

    /**
     * 创建语音消息
     *
     * @param toUid
     * @param voiceFile 语音文件
     * @param duration  语音时长
     * @return
     */
    public ImMessageBean createVoiceMessage(String toUid, File voiceFile, long duration) {
        String appKey = IM_KEY;
        try {
            Message message = JMessageClient.createSingleVoiceMessage(PREFIX + toUid, appKey, voiceFile, (int) (duration / 1000));
            return new ImMessageBean(App.getInstance().getUid(), message, ImMessageBean.TYPE_VOICE_R, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送消息
     */
    public void sendMessage(Message msg, String target) {
        JMessageClient.sendMessage(msg, mOptions);
    }

    public void removeMessage(String toUid, Message message) {
        if (!TextUtils.isEmpty(toUid) && message != null) {
            Conversation conversation = JMessageClient.getSingleConversation(getImUid(toUid));
            if (conversation != null) {
                conversation.deleteMessage(message.getId());
            }
        }
    }

    /**
     * 删除所有会话记录
     */
    public void removeAllConversation() {
        List<Conversation> list = JMessageClient.getConversationList();
        for (Conversation conversation : list) {
            Object targetInfo = conversation.getTargetInfo();
            JMessageClient.deleteSingleConversation(((UserInfo) targetInfo).getUserName());
        }
    }

    /**
     * 删除会话记录
     */
    public void removeConversation(String uid) {
        JMessageClient.deleteSingleConversation(getImUid(uid));
        Log.e("zlg", "删除刷新未读");
        refreshAllUnReadMsgCount();
    }


    /**
     * 刷新聊天列表的最后一条消息
     */
    public void refreshLastMessage(String uid, ImMessageBean bean) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        if (bean == null) {
            return;
        }
        Message msg = bean.getRawMessage();
        if (msg == null) {
            return;
        }
        ImUserMsgEvent imUserMsgEvent = new ImUserMsgEvent();
        imUserMsgEvent.setUid(uid);
        int messageType = getMessageType(msg); //如果收到的是图片消息，就在消息列表中的最后一条消息改为图片
        Log.e("zlg", "messageType===" + messageType);
        if (messageType == 3 || messageType == 4) {
            imUserMsgEvent.setLastMessage("[图片]");
        } else if (messageType == 5 || messageType == 6) {
            imUserMsgEvent.setLastMessage("[语音]");
        } else if (messageType == 10 || messageType == 11 || messageType == 12 || messageType == 13) {
            imUserMsgEvent.setLastMessage("[分享]");
        } else {
            imUserMsgEvent.setLastMessage(getMessageString(msg));
        }
        UserInfo userInfo = (UserInfo) msg.getTargetInfo();
//            String avatar = msg.getFromUser().getAvatar();
//            String nickname = msg.getFromUser().getNickname();
        String nickname = userInfo.getNickname();
        String avatar = userInfo.getAvatar();
        imUserMsgEvent.setNikeName(nickname);
        imUserMsgEvent.setHeadImg(avatar);
        imUserMsgEvent.setUnReadCount(getUnReadMsgCount(uid));
        imUserMsgEvent.setLastTime(getMessageTimeString(msg));
        EventBus.getDefault().post(imUserMsgEvent);
    }

    //消息通知栏点击处理
    public void onEvent(NotificationClickEvent event) {
//        Intent notificationIntent = new Intent(App.getInstance(), ChatActivity.class);
//        notificationIntent.putExtra(App.CONV_TITLE, event.getMessage().getFromUser().getNickname());
//        notificationIntent.putExtra(App.TARGET_ID, event.getMessage().getFromUser().getUserName());
//        App.getInstance().startActivity(notificationIntent);//自定义跳转到指定页面
    }
}
