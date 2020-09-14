package app.com.yangquan.view.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestListActivity extends BaseActivity {
    @BindView(R.id.rl_qq_step)
    RelativeLayout rlQqStep;
    @BindView(R.id.rl_color_track)
    RelativeLayout rlColorTrack;
    @BindView(R.id.rl_progress)
    RelativeLayout rlProgress;
    @BindView(R.id.rl_shape)
    RelativeLayout rlShape;

    @Override
    protected int getLayout() {
        return R.layout.test_list;
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


    @OnClick({R.id.rl_qq_step, R.id.rl_color_track, R.id.rl_progress,R.id.rl_shape})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_qq_step: //计步器
                startActivity(new Intent(mContext, QQStepActivity.class));
                break;
            case R.id.rl_color_track: //字体变色
                startActivity(new Intent(mContext, ColorTrackActivity.class));
                break;
            case R.id.rl_progress: //进度条
                startActivity(new Intent(mContext, MyProgressActivity.class));
                break;
            case R.id.rl_shape: //58同城加载动画
                startActivity(new Intent(mContext, ShapeActivity.class));
                break;
        }
    }
}
