package app.com.yangquan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.http.HttpManager;
import app.com.yangquan.util.DateUtils;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.LoadingButton;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class UpHeadActivity extends BaseActivity implements LoadingButton.OnLoadingListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.riv_avater)
    RoundedImageView rivAvater;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_clear_name)
    ImageView ivClearName;
    @BindView(R.id.tv_old)
    TextView tvOld;
    @BindView(R.id.et_work)
    EditText etWork;
    @BindView(R.id.iv_clear_work)
    ImageView ivClearWork;
    @BindView(R.id.tv_confirm)
    LoadingButton tvConfirm;
    @BindView(R.id.tv_sui)
    TextView tvSui;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.rl_selete_age)
    RelativeLayout rlSeleteAge;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgPath;

    @Override
    protected int getLayout() {
        return R.layout.activity_up_head;
    }

    @Override
    protected void init() {
        initListener();
        tvConfirm.setOnLoadingListener(this);
    }

    //上传头像
    private void uplod(String path) {
        Map<String, File> map = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        params.put("uid",PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        File file = new File(path);
        map.put(file.getName(), file);
        HttpManager.up(mContext, "1", Const.Config.uploadImage, 1,"img", map,params, this);
    }

    //设置昵称
    private void setNikeName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", name);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setNickname, 2, map);
    }

    //设置职业
    private void setWork(String hangye) {
        Map<String, Object> map = new HashMap<>();
        map.put("hangye", hangye);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setHangye, 3, map);
    }

    //设置年龄
    private void setAge(String age) {
        Map<String, Object> map = new HashMap<>();
        map.put("birthday", age);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setAge, 4, map);
    }

    //登录
    private void login() {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.mobile));
        map.put("pwd", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.pwd));
        post(Const.Config.login, 5, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) { //头像
            ToastUtil.show("头像上传成功");
        } else if (flag == 2) { //昵称
            setAge(tvOld.getText().toString().trim());
        } else if (flag == 3) { //职业
            login();
        } else if (flag == 4) { //年龄
            if (!TextUtils.isEmpty(etWork.getText().toString().trim())) {
                setWork(etWork.getText().toString().trim());
            } else {
                login();
            }
        } else if (flag == 5) {
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                PreferencesUtils.putSharePre(mContext, Const.SharePre.userId, bean.getData().getId());
                tvConfirm.complete();
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        tvConfirm.fail();
    }

    //选择年龄弹窗
    private void showAgePop() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.showAnimation(true);
        datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
            @Override
            public void onDateChoose(int year, int month, int day) {
                String date1 = year + "-" + month + "-" + day;
                String date2 = getDate();
                boolean date2Bigger = DateUtils.isDate2Bigger(date1, date2);
                if (date2Bigger) {
                    tvOld.setText(date1);
                } else {
                    tvOld.setText("");
                    ToastUtil.show("您的年龄是负数吗？");
                }
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }

    //获取当前时间
    private String getDate() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        return mYear + "-" + mMonth + "-" + mDay;
    }

    //隐藏软键盘
    private void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            showPop();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }
    }

    private void initListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    ivClearName.setVisibility(View.VISIBLE);
                    if (tvOld.getText().toString().trim().length() > 0 && !TextUtils.isEmpty(imgPath)&&etWork.getText().toString().trim().length()>0) {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                    } else {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                    }
                } else {
                    ivClearName.setVisibility(View.GONE);
                }
            }
        });

        etWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    ivClearWork.setVisibility(View.VISIBLE);
                    if (tvOld.getText().toString().trim().length() > 0 && !TextUtils.isEmpty(imgPath)&&etName.getText().toString().trim().length()>0) {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                    } else {
                        tvConfirm.setBackgroundResource(R.drawable.selector_btn_un);
                    }
                } else {
                    ivClearWork.setVisibility(View.GONE);
                }
            }
        });
    }


    //选择图片弹窗
    private void showPop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_up_avater, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)//显示的布局，还可以通过设置一个View
                .enableBackgroundDark(true)
                // .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAtLocation(rlRoot, Gravity.BOTTOM, 0, 50);
        //从相册选择
        contentView.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtil.picture(mContext, 1);
                popWindow.dissmiss();
            }
        });

        //从相册选择
        contentView.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtil.pictureCamera(mContext, 1);
                popWindow.dissmiss();
            }
        });

        //取消
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dissmiss();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.riv_avater, R.id.iv_clear_name, R.id.iv_clear_work, R.id.tv_confirm, R.id.rl_selete_age})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_selete_age:
                hideSoftKeyboard(mContext);
                showAgePop();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.riv_avater:
                hideSoftKeyboard(mContext);
                getPermission();
                break;
            case R.id.iv_clear_name:
                etName.setText("");
                ivClearName.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_work:
                etWork.setText("");
                ivClearWork.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                if (TextUtils.isEmpty(imgPath)) {
                    ToastUtil.show("请上传你喜欢的头像吧！");
                    return;
                }

                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    ToastUtil.show("请输入一个你喜欢的名字吧！");
                    return;
                }

                if (TextUtils.isEmpty(tvOld.getText().toString().trim())) {
                    ToastUtil.show("请填写您的年龄！");
                    return;
                }

                tvConfirm.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setNikeName(etName.getText().toString().trim());
                    }
                }, 500);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    Glide.with(mContext).load(localMedia.get(0).getCutPath()).into(rivAvater);
                    imgPath = localMedia.get(0).getCompressPath();
                    uplod(localMedia.get(0).getCompressPath());
                    break;
            }
        }
    }

    @Override
    public void onCompleted() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onCanceled() {

    }
}
