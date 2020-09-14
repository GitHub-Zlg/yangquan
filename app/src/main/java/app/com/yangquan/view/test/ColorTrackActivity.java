package app.com.yangquan.view.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.TextView;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class ColorTrackActivity extends BaseActivity {
    @BindView(R.id.tv_color)
    ColorTrackTextView tvColor;
    @BindView(R.id.tv_direction)
    TextView tvDirection;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    private boolean isDefult = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_color_track;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    @OnClick({R.id.tv_direction, R.id.tv_progress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_direction:
                if (isDefult) {
                    animator(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
//                    tvColor.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    isDefult = false;
                } else {
                    animator(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
//                    tvColor.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    isDefult = true;
                }
                break;
            case R.id.tv_progress:
                tvColor.setmCurrentProgress(0.9f);
                break;
        }
    }

    private void animator(ColorTrackTextView.Direction direction) {
        tvColor.setDirection(direction);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                tvColor.setmCurrentProgress(progress);
            }
        });
        valueAnimator.start();
    }
}
