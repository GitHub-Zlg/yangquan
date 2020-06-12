package app.com.yangquan.jiguang.im;


import cn.jpush.im.android.api.model.Message;

public class RetractEvent {
    private Message message;
    private boolean isMySelf;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isMySelf() {
        return isMySelf;
    }

    public void setMySelf(boolean mySelf) {
        isMySelf = mySelf;
    }
}
