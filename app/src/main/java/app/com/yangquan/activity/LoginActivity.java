package app.com.yangquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flod.drawabletextview.DrawableTextView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.CustomVideoView;
import app.com.yangquan.view.LoadingButton;
import app.com.yangquan.view.RegisterTimeCountTextView;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoadingButton.OnLoadingListener {
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_login)
    LoadingButton tvLogin;
    @BindView(R.id.videoview)
    CustomVideoView videoview;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.iv_clear_account)
    ImageView ivClearAccount;
    @BindView(R.id.iv_clear_pwd)
    ImageView ivClearPwd;
    @BindView(R.id.tv_get_code)
    RegisterTimeCountTextView tvGetCode;
    @BindView(R.id.tv_login_type)
    TextView tvLoginType;
    private int LOGING_TYPE = 1; //1 验证码登录  2账号密码登录
    private Intent intent;
    private boolean mIsExit;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        intent = new Intent();
        if(!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId))){
            intent.setClass(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        initListener();
        initVideo();
        tvLogin.setOnLoadingListener(this);
    }

    //登录
    private void login(String mobile,String pwd){
        Map<String,Object> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("pwd",pwd);
        post(Const.Config.login,1,map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if(bean!=null){
                PreferencesUtils.putSharePre(mContext, Const.SharePre.userId,bean.getData().getId());
                tvLogin.complete();
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
            tvLogin.fail();
    }

    //检查输入手机号
    private boolean isMobileNO(String mobileNums) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    //初始化登录背景视频
    private void initVideo() {
        //找VideoView控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //加载视频文件
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_video_bg));
        //静音
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0f, 0f);
            }
        });
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    //监听
    private void initListener() {
        //账号密码焦点监听
        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivSign.setImageResource(R.mipmap.login_1);
                }
            }
        });

        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivSign.setImageResource(R.mipmap.login_2);
                }
            }
        });

        //账号密码输入监听
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    ivClearAccount.setVisibility(View.VISIBLE);
                    if (etPwd.getText().toString().trim().length() > 0) {
                        tvLogin.setBackgroundResource(R.drawable.selector_btn);
                    } else {
                        tvLogin.setBackgroundResource(R.drawable.selector_btn_un);
                    }
                } else {
                    ivClearAccount.setVisibility(View.GONE);
                }
            }
        });

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    ivClearPwd.setVisibility(View.VISIBLE);
                    if (etAccount.getText().toString().trim().length() > 0) {
                        tvLogin.setBackgroundResource(R.drawable.selector_btn);
                    } else {
                        tvLogin.setBackgroundResource(R.drawable.selector_btn_un);
                    }
                } else {
                    ivClearPwd.setVisibility(View.GONE);
                }
            }
        });
    }


    //点击事件
    @OnClick({R.id.register, R.id.tv_login, R.id.iv_clear_account, R.id.iv_clear_pwd, R.id.tv_get_code, R.id.tv_login_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:  //注册
                startActivity(new Intent(mContext, RegisterActivity.class));
//                startActivity(new Intent(mContext, ChatActivity.class));

                break;
            case R.id.tv_login: //登录
                if (!isMobileNO(etAccount.getText().toString().trim())) {
                    ToastUtil.show("请输入正确的手机号");
                    return;
                }

                if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtil.show("请输入密码");
                    return;
                }

                tvLogin.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login(etAccount.getText().toString().trim(),etPwd.getText().toString().trim());
                    }
                }, 500);
                break;
            case R.id.iv_clear_account:
                etAccount.setText("");
                ivClearAccount.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_pwd:
                etPwd.setText("");
                ivClearPwd.setVisibility(View.GONE);
                break;
            case R.id.tv_get_code:
                if(isMobileNO(etAccount.getText().toString().trim())){
                    tvGetCode.start();
                }else {
                    ToastUtil.show("请输入正确的手机号");
                }
                break;
            case R.id.tv_login_type:
                if (LOGING_TYPE == 1) {
                    tvGetCode.setVisibility(View.GONE);
                    LOGING_TYPE = 2;
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    etPwd.setText("");
                    etPwd.setHint("请输入密码");
                    tvLoginType.setText("验证码登录");
                } else {
                    tvGetCode.setVisibility(View.VISIBLE);
                    LOGING_TYPE = 1;
                    etPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    etPwd.setText("");
                    etPwd.setHint("请输入验证码");
                    tvLoginType.setText("密码登录");
                }
                break;
        }
    }

    @Override
    public void onCompleted() {
        intent.setClass(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onCanceled() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        initVideo();
    }

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                mIsExit = true;
                ToastUtil.show("再按一次退出程序");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2500);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
