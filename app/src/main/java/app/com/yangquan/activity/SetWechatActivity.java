package app.com.yangquan.activity;

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
import butterknife.OnClick;

public class SetWechatActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.loading)
    SpinKitView loading;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.et_wechat)
    EditText etWechat;

    @Override
    protected int getLayout() {
        return R.layout.activity_selete_wechat;
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

    //修改昵称
    private void setWechat(String wechat) {
        Map<String, Object> map = new HashMap<>();
        map.put("wechat", wechat);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setWechat, 2, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                etWechat.setText(bean.getData().getWechat());
                etWechat.setSelection(bean.getData().getWechat().length());
                etWechat.requestFocus();
            }
        } else if (flag == 2) {
            EventBus.getDefault().post(new UpDateUserInfoEvent());
            finish();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        if (flag == 1) {
            ToastUtil.show("获取信息失败请重试");
        } else if (flag == 2) {
            loading.setVisibility(View.GONE);
            ivRight.setVisibility(View.VISIBLE);
            ToastUtil.show("设置微信号失败请重试");
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                if (TextUtils.isEmpty(etWechat.getText().toString().trim())) {
                    ToastUtil.show("请输入微信号");
                    return;
                }
                ivRight.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                setWechat(etWechat.getText().toString().trim());
                break;
        }
    }
}
