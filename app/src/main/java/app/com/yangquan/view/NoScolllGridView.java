package app.com.yangquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class NoScolllGridView extends GridView {
    public NoScolllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScolllGridView(Context context) {
        super(context);
    }

    public NoScolllGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }
}
