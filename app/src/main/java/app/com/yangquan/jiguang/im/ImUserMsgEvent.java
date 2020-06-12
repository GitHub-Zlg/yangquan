package app.com.yangquan.jiguang.im;

import java.io.Serializable;

/**
 * 消息列表对象
 */

public class ImUserMsgEvent implements Serializable {

    private String uid;
    private String lastMessage;
    private int unReadCount;
    private String lastTime;
    private String nikeName;
    private String headImg;
    private String JpshImg;
    private String JpshName;
    private int type;  //0 == 好友   其他===临时

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getJpshImg() {
        return JpshImg;
    }

    public void setJpshImg(String jpshImg) {
        JpshImg = jpshImg;
    }

    public String getJpshName() {
        return JpshName;
    }

    public void setJpshName(String jpshName) {
        JpshName = jpshName;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
