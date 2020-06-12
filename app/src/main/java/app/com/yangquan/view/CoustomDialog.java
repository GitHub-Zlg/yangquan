package app.com.yangquan.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.yangquan.R;

public class CoustomDialog extends Dialog {
    /**
     * 显示的图片
     */
    private ImageView imageIv;

    /**
     * 显示的标题
     */
    private TextView titleTv;

    /**
     * 显示的消息
     */
    private TextView messageTv;

    /**
     * 确认和取消按钮
     */
    private TextView negtiveBn, positiveBn;
    private TextView cancel;

    /**
     * 按钮之间的分割线
     */
    public CoustomDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    /**
     * 都是内容数据
     */
    private String title;
    private String positive, negtive;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coustom_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });

        //设置取消按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        positiveBn.setText("是");
        cancel.setText("否");

    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        positiveBn = (TextView) findViewById(R.id.positive);
        cancel = (TextView) findViewById(R.id.cancel);
        titleTv = (TextView) findViewById(R.id.title);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public CoustomDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }


    public String getTitle() {
        return title;
    }

    public CoustomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public CoustomDialog setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegtive() {
        return negtive;
    }

    public CoustomDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public CoustomDialog setSingle(boolean single) {
        isSingle = single;
        return this;
    }

    public CoustomDialog setImageResId(int imageResId) {
        return this;
    }
}
