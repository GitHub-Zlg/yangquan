package app.com.yangquan.jiguang.im;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;

import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by cxf on 2018/7/12.
 * IM 消息实体类
 */

public class ImMessageBean extends MultiItemEntity {
    public static final int TYPE_TEXT_L = 1;
    public static final int TYPE_TEXT_R = 2;
    public static final int TYPE_IMAGE_L = 3;
    public static final int TYPE_IMAGE_R = 4;
    public static final int TYPE_VOICE_L = 5;
    public static final int TYPE_VOICE_R = 6;
    public static final int TYPE_LOCATION = 7;
    public static final int TYPE_RETRACT_L = 8;
    public static final int TYPE_RETRACT_R = 9;
    public static final int TYPE_CUSTOM_L = 10;
    public static final int TYPE_CUSTOM_R = 11;
    public static final int TYPE_CUSTOM_HOME_L = 12;
    public static final int TYPE_CUSTOM_HOME_R = 13;

    private String uid;
    private Message rawMessage;
    private int type;
    private boolean fromSelf;
    private long time;
    private File imageFile;
    private boolean loading;
    private boolean sendFail;

    public ImMessageBean(String uid, Message rawMessage, int type, boolean fromSelf) {
        this.uid = uid;
        this.rawMessage = rawMessage;
        this.type = type;
        this.fromSelf = fromSelf;
        time = rawMessage.getCreateTime();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Message getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(Message rawMessage) {
        this.rawMessage = rawMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFromSelf() {
        return fromSelf;
    }

    public void setFromSelf(boolean fromSelf) {
        this.fromSelf = fromSelf;
    }

    public long getTime() {
        return time;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isSendFail() {
        return sendFail;
    }

    public void setSendFail(boolean sendFail) {
        this.sendFail = sendFail;
    }

    public int getVoiceDuration() {
        int duration = 0;
        if (rawMessage != null) {
            MessageContent content = rawMessage.getContent();
            if (content != null) {
                VoiceContent voiceContent = (VoiceContent) content;
                duration = voiceContent.getDuration();
            }
        }
        return duration;
    }

    public boolean isRead() {
        return rawMessage != null && rawMessage.haveRead();
    }

    public int getMessageId() {
        return rawMessage != null ? rawMessage.getId() : -1;
    }

    public void hasRead(BasicCallback callback) {
        if (rawMessage != null) {
            rawMessage.setHaveRead(callback);
        }
    }

    @Override
    public int getItemType() {
        return type;
    }
}
