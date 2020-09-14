package app.com.yangquan.view.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgressActivity extends BaseActivity {
    @BindView(R.id.my_progress)
    MyProgreeBar myProgress;

    @Override
    protected int getLayout() {
        return R.layout.activity_progress;
    }

    @Override
    protected void init() {
        ObjectAnimator animator = new  ObjectAnimator();
        animator.setDuration(3000);
        animator.setIntValues(0,100);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                if(myProgress!=null) {
                    myProgress.setProgress(animatedValue);
                }
            }
        });
        animator.start();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }
}
