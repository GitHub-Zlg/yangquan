package app.com.yangquan.view.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import butterknife.BindView;

public class QQStepActivity extends BaseActivity {
    @BindView(R.id.step_view)
    QQStepView stepView;

    @Override
    protected int getLayout() {
        return R.layout.step;
    }

    @Override
    protected void init() {
        stepView.setStepMax(100);

        //属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 84);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator()); //设置插值器，刚开始快，最后慢
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if(stepView!=null) {
                    stepView.setCurrentStep((int) animatedValue);
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }
}
