/*
 * Created by liyunte on 17-7-20 下午3:18
 * Class Brief Introduction:
 * 倒计时控件
 */

package app.com.yangquan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

@SuppressLint("AppCompatCustomView")
public class RegisterTimeCountTextView extends TextView {
    private int sumTime = 60000;
    private MyTimeCount timeCount;
    public RegisterTimeCountTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RegisterTimeCountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        timeCount =new MyTimeCount(this,sumTime,1000);
    }

    public RegisterTimeCountTextView(Context context) {
        this(context,null);
    }

    static class  MyTimeCount extends CountDownTimer{
      private  WeakReference<RegisterTimeCountTextView> timeCountTextViewWeakReference;
        /**
         *
         * @param millisInFuture 总时间
         * @param countDownInterval 时间单位
         */
        public MyTimeCount(RegisterTimeCountTextView timeCountTextView, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            timeCountTextViewWeakReference = new WeakReference<RegisterTimeCountTextView>(timeCountTextView);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            final RegisterTimeCountTextView timeCountTextView = timeCountTextViewWeakReference.get();
            if (timeCountTextView!=null){
                timeCountTextView.setClickable(false);
                timeCountTextView.setText(millisUntilFinished / 1000+"秒重发");
            }else {
                    cancel();
            }

        }

        @Override
        public void onFinish() {
            final RegisterTimeCountTextView timeCountTextView = timeCountTextViewWeakReference.get();
            if (timeCountTextView!=null){
                timeCountTextView.setText("获取验证码");
                timeCountTextView.setClickable(true);
            }else {
                cancel();
            }

        }
    }
    public void start(){
        timeCount.start();
    }
    public void cancel(){
        timeCount.cancel();
    }

}
