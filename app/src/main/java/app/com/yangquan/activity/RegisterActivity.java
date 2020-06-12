package app.com.yangquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.RegisterBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.LoadingButton;
import app.com.yangquan.view.RegisterTimeCountTextView;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements LoadingButton.OnLoadingListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    RegisterTimeCountTextView tvGetCode;
    @BindView(R.id.tv_confirm)
    LoadingButton tvConfirm;
    @BindView(R.id.iv_clear_phone)
    ImageView ivClearPhone;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        initListener();
        tvConfirm.setOnLoadingListener(this);
    }

    //注册
    private void codesms(String phone, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        post(Const.Config.codesms, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if(flag == 1){
            RegisterBean bean = new Gson().fromJson(message, RegisterBean.class);
            if(bean!=null&&bean.getData()!=null) {
                PreferencesUtils.putSharePre(mContext, Const.SharePre.userId,bean.getData().getUid());
                PreferencesUtils.putSharePre(mContext, Const.SharePre.mobile,etPhone.getText().toString().trim());
                tvConfirm.complete();
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        tvConfirm.fail();
    }

    //监听输入手机号
    private void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11) {
                    tvGetCode.setTextColor(Color.parseColor("#30A1E2"));
                    if (etCode.getText().toString().trim().length() == 4) {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                    } else {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                    }
                } else {
                    tvGetCode.setTextColor(Color.parseColor("#515151"));
                }

                if (s.toString().trim().length() > 0) {
                    ivClearPhone.setVisibility(View.VISIBLE);
                }else {
                    ivClearPhone.setVisibility(View.GONE);
                }
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 4 && etPhone.getText().toString().trim().length() == 11) {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                } else {
                    tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                }
            }
        });
    }

    //检查输入手机号
    private boolean isMobileNO(String mobileNums) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @OnClick({R.id.iv_back, R.id.tv_get_code, R.id.tv_confirm, R.id.iv_clear_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_code:
                if (isMobileNO(etPhone.getText().toString().trim())) {
                    tvGetCode.start();
                    ToastUtil.show("验证码已发送，请注意查收");
                } else {
                    ToastUtil.show("请检查手机号是否正确");
                }
                break;
            case R.id.tv_confirm:
                if (!isMobileNO(etPhone.getText().toString().trim())) {
                    ToastUtil.show("请输入正确手机号");
                    return;
                }

                if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
                    ToastUtil.show("请输入四位数验证码");
                    return;
                }
                tvConfirm.start();
                //调用检验验证码接口成功后进入设置登录密码界面
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        codesms(etPhone.getText().toString().trim(),etCode.getText().toString().trim());
                    }
                }, 500);
                break;
            case R.id.iv_clear_phone:
                etPhone.setText("");
                ivClearPhone.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCompleted() {
        //动画完毕
        startActivity(new Intent(mContext, SetPwdActivity.class));
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onCanceled() {

    }
}
