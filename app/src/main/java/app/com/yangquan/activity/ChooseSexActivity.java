package app.com.yangquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

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

public class ChooseSexActivity extends BaseActivity implements LoadingButton.OnLoadingListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.riv_man)
    RoundedImageView rivMan;
    @BindView(R.id.riv_woman)
    RoundedImageView rivWoman;
    @BindView(R.id.tv_confirm)
    LoadingButton tvConfirm;
    private int sex = 0; //1男性   2女性


    @Override
    protected int getLayout() {
        return R.layout.activity_choose_sex;
    }

    @Override
    protected void init() {
        tvConfirm.setOnLoadingListener(this);
    }

    private void setSex(int sex){
        Map<String,String> map = new HashMap<>();
        map.put("sex",sex+"");
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        get(Const.Config.setSex,1,map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if(flag == 1){
            tvConfirm.complete();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        tvConfirm.fail();
    }

    @OnClick({R.id.iv_back, R.id.riv_man, R.id.riv_woman, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.riv_man:
                rivMan.setBorderColor(Color.parseColor("#5DA8F9"));
                rivWoman.setBorderColor(Color.parseColor("#00000000"));
                tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                sex = 1;
                break;
            case R.id.riv_woman:
                rivWoman.setBorderColor(Color.parseColor("#FFABCE"));
                rivMan.setBorderColor(Color.parseColor("#00000000"));
                tvConfirm.setBackgroundResource(R.drawable.selector_btn);
                sex = 2;
                break;
            case R.id.tv_confirm:
                if(sex == 0){
                    ToastUtil.show("请选择性别后继续");
                    return;
                }

                tvConfirm.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       setSex(sex);
                    }
                },500);
                break;
        }
    }

    @Override
    public void onCompleted() {
        startActivity(new Intent(mContext,UpHeadActivity.class));
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onCanceled() {

    }
}
