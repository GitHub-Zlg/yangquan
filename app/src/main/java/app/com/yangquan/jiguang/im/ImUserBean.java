package app.com.yangquan.jiguang.im;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cxf on 2017/8/14.
 * IM 聊天用户 实体类
 */

public class ImUserBean  implements Parcelable {

    private String lastMessage;
    private int unReadCount;
    private String lastTime;
    private int fromType;
    private int msgType;
    private int attent;//我是否关注对方
    private int otherAttent;//对方是否关注我
    private boolean anchorItem;
    private boolean hasConversation;


    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isAnchorItem() {
        return anchorItem;
    }

    public void setAnchorItem(boolean anchorItem) {
        this.anchorItem = anchorItem;
    }

    public boolean isHasConversation() {
        return hasConversation;
    }

    public void setHasConversation(boolean hasConversation) {
        this.hasConversation = hasConversation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastMessage);
        dest.writeInt(this.unReadCount);
        dest.writeString(this.lastTime);
        dest.writeInt(this.fromType);
        dest.writeInt(this.attent);
        dest.writeInt(this.otherAttent);
    }

    public ImUserBean() {

    }

    protected ImUserBean(Parcel in) {
        this.lastMessage = in.readString();
        this.unReadCount = in.readInt();
        this.lastTime = in.readString();
        this.fromType = in.readInt();
        this.attent = in.readInt();
        this.otherAttent = in.readInt();
    }

    public static final Creator<ImUserBean> CREATOR = new Creator<ImUserBean>() {
        @Override
        public ImUserBean[] newArray(int size) {
            return new ImUserBean[size];
        }

        @Override
        public ImUserBean createFromParcel(Parcel in) {
            return new ImUserBean(in);
        }
    };

}
