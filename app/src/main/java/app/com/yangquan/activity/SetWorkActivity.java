package app.com.yangquan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UpDateUserInfoEvent;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetWorkActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.et_work)
    EditText etWork;
    @BindView(R.id.loading)
    SpinKitView loading;

    @Override
    protected int getLayout() {
        return R.layout.activity_set_work;
    }

    @Override
    protected void init() {
        getUserInfo();
    }

    //获取个人信息
    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.userInfo, 1, map);
    }

    //设置职业
    private void setWork(String work) {
        Map<String, Object> map = new HashMap<>();
        map.put("hangye", work);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setHangye, 2, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                etWork.setText(bean.getData().getHangye());
                etWork.setSelection(bean.getData().getHangye().length());
                etWork.requestFocus();
            }
        } else if (flag == 2) {
            EventBus.getDefault().post(new UpDateUserInfoEvent());
            finish();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        if(flag == 1){
            ToastUtil.show("获取信息失败请重试");
        }else if(flag == 2){
            loading.setVisibility(View.GONE);
            ivRight.setVisibility(View.VISIBLE);
            ToastUtil.show("修改工作失败请重试");
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                if (TextUtils.isEmpty(etWork.getText().toString().trim())) {
                    ToastUtil.show("请输入您的职业");
                    return;
                }

                ivRight.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                setWork(etWork.getText().toString().trim());
                break;
        }
    }
}
