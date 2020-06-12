package app.com.yangquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.LoadingButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPwdActivity extends BaseActivity implements LoadingButton.OnLoadingListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_confirm)
    LoadingButton tvConfirm;
    @BindView(R.id.iv_clear_pwd)
    ImageView ivClearPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.iv_clear_confirm_pwd)
    ImageView ivClearConfirmPwd;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    protected int getLayout() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void init() {
        initListener();
        tvConfirm.setOnLoadingListener(this);
    }

    private void setPwd(String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("pwd", pwd);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.Pwd, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            //设置密码成功
            PreferencesUtils.putSharePre(mContext, Const.SharePre.pwd,etConfirmPwd.getText().toString().trim());
            tvConfirm.complete();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        tvConfirm.fail();
    }

    //输入监听
    private void initListener() {
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() >= 6 && etConfirmPwd.getText().toString().trim().length() >= 6) {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                } else {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                }

                if (s.toString().trim().length() > 0) {
                    ivClearPwd.setVisibility(View.VISIBLE);
                } else {
                    ivClearPwd.setVisibility(View.GONE);
                }
            }
        });

        etConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() >= 6 && etPwd.getText().toString().trim().length() >= 6) {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                } else {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                }

                if (s.toString().trim().length() > 0) {
                    ivClearConfirmPwd.setVisibility(View.VISIBLE);
                } else {
                    ivClearConfirmPwd.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.iv_clear_pwd, R.id.iv_clear_confirm_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                if (etPwd.getText().toString().trim().length() < 6) {
                    ToastUtil.show("请输入6-12位密码");
                    return;
                }

                if (!etPwd.getText().toString().trim().equals(etConfirmPwd.getText().toString().trim())) {
                    ToastUtil.show("两次输入的密码不一致，请重试");
                    return;
                }

                //设置密码接口调用完毕后进入下一个设置性别界面
                tvConfirm.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPwd(etPwd.getText().toString().trim());
                    }
                }, 500);
                break;
            case R.id.iv_clear_pwd:
                etPwd.setText("");
                ivClearPwd.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_confirm_pwd:
                etConfirmPwd.setText("");
                ivClearConfirmPwd.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCompleted() {
        startActivity(new Intent(mContext, ChooseSexActivity.class));
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onCanceled() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
