package app.com.yangquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLineatLayout extends LinearLayout {
    private int mMsgId;
    public MyLineatLayout(Context context) {
        super(context);
    }

    public MyLineatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getMsgId() {
        return mMsgId;
    }

    public void setMsgId(int msgId) {
        mMsgId = msgId;
    }
}
