package app.com.yangquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bitvale.switcher.SwitcherC;
import com.bitvale.switcher.SwitcherX;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UpDateUserInfoEvent;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.BigImageUtil;
import app.com.yangquan.util.DateUtils;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.listeners.OnLinkageListener;
import cn.addapp.pickers.picker.AddressPicker;
import cn.addapp.pickers.picker.NumberPicker;
import cn.addapp.pickers.util.ConvertUtils;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

public class UpDateProfileActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.avatars)
    BGASortableNinePhotoLayout avatars;
    @BindView(R.id.tv_nikename)
    TextView tvNikename;
    @BindView(R.id.rl_nikename)
    RelativeLayout rlNikename;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.rl_age)
    RelativeLayout rlAge;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.rl_tag)
    RelativeLayout rlTag;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.rl_signature)
    RelativeLayout rlSignature;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.rl_height)
    RelativeLayout rlHeight;
    @BindView(R.id.tv_work)
    TextView tvWork;
    @BindView(R.id.rl_work)
    RelativeLayout rlWork;
    @BindView(R.id.tv_hometown)
    TextView tvHometown;
    @BindView(R.id.rl_hometown)
    RelativeLayout rlHometown;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.rl_wechat)
    RelativeLayout rlWechat;
    @BindView(R.id.switcher)
    SwitcherX switcher;
    private String avaterPath;
    private UserBean bean;
    private Intent intent;
    private String date;
    private DatePickerDialogFragment agePicker;
    private NumberPicker picker;

    @Override
    protected int getLayout() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        intent = new Intent();
        initView();
        initHeightPicker();
        initAgePop();
        getUserInfo();
    }

    //获取个人信息
    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.userInfo, 1, map);
    }

    //设置年龄
    private void setAge(String age) {
        Map<String, Object> map = new HashMap<>();
        map.put("birthday", age);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setAge, 2, map);
    }

    //设置地区
    private void setHometown(String province, String city, String county) {
        Map<String, Object> map = new HashMap<>();
        map.put("province", province);
        map.put("city", city);
        map.put("county", county);
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setRegion, 3, map);
    }

    //设置身高
    private void setHeight(int height) {
        Map<String, Object> map = new HashMap<>();
        map.put("height", height + "");
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setHeight, 4, map);
    }

    //设置微信号是否对外可见
    private void setWechatShow(int wechat_show) {
        Map<String, Object> map = new HashMap<>();
        map.put("wechat_show", wechat_show + "");
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.setWechatShow, 5, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                avaterPath = bean.getData().getUser_img();
                ArrayList<String> list = new ArrayList<>();
                list.add(avaterPath);
                avatars.setData(list);
                tvNikename.setText(bean.getData().getNickname());
                tvAge.setText(bean.getData().getBirthday());
                tvWork.setText(bean.getData().getHangye());
                tvHeight.setText(bean.getData().getHeight() + "cm");
                tvHometown.setText(bean.getData().getRegion());
                tvWechat.setText(bean.getData().getWechat());
                tvSignature.setText(TextUtils.isEmpty(bean.getData().getAutograph()) ? "给喜欢你的人留下一句话吧" : bean.getData().getAutograph());
                String[] split = bean.getData().getBirthday().split("-");
                agePicker.setSelectedDate(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                picker.setSelectedItem(Integer.parseInt(bean.getData().getHeight()));
                if(bean.getData().getWechat_show().equals("0")){
                    switcher.setChecked(true,true);
                }else {
                    switcher.setChecked(false,true);
                }
            }
        } else if (flag == 2) {
            getUserInfo();
        } else if (flag == 3) {
            getUserInfo();
        } else if (flag == 4) {
            getUserInfo();
        }else if(flag == 5){
            finish();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {
        if (flag == 1) {
            ToastUtil.show("信息获取失败，请重试");
        }
    }

    private void initView() {
        avatars.setDelegate(this);
        tvTitle.setText("编辑资料");
        tvRight.setVisibility(View.GONE);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        PhotoUtil.picture(mContext, 4 - avatars.getItemCount());
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        avatars.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        BigImageUtil.more(mContext, models, position);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    ArrayList<String> img = new ArrayList<>();
                    for (int i = 0; i < localMedia.size(); i++) {
                        img.add(localMedia.get(i).getPath());
                    }
                    avatars.addMoreData(img);
                    break;
            }
        }
    }

    //获取当前时间
    private String getDate() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        return mYear + "-" + mMonth + "-" + mDay;
    }

    //选择年龄弹窗
    private void initAgePop() {
        agePicker = new DatePickerDialogFragment();
        agePicker.showAnimation(true);
        agePicker.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
            @Override
            public void onDateChoose(int year, int month, int day) {
                date = year + "-" + month + "-" + day;
                String date2 = getDate();
                boolean date2Bigger = DateUtils.isDate2Bigger(date, date2);
                if (date2Bigger) {
                    setAge(date);
                } else {
                    tvAge.setText(bean.getData().getBirthday());
                    ToastUtil.show("你是穿越来的？");
                }
            }
        });
    }

    //选择身高弹窗
    public void initHeightPicker() {
        picker = new NumberPicker(this);
        picker.setSelectedTextColor(Color.parseColor("#1BB2D8"));
        picker.setWidth(picker.getScreenWidthPixels());
        picker.setCanLoop(false);
        picker.setLineVisible(false);
        picker.setOffset(2);//偏移量
        picker.setRange(145, 200, 1);//数字范围
        picker.setItemWidth(100);
        picker.setLabel("cm");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                setHeight(item.intValue());
            }
        });
    }

    //地址选择弹窗
    public void onAddressPicker() {
        ArrayList<Province> data = new ArrayList<>();
        String json = null;
        try {
            json = ConvertUtils.toString(getAssets().open("city.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.addAll(JSON.parseArray(json, Province.class));
        AddressPicker task = new AddressPicker(mContext, data);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setOnLinkageListener(new OnLinkageListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String provinceName = province.getAreaName();
                String cityName = city.getAreaName();
                String countyName = county.getAreaName();
                setHometown(provinceName, cityName, countyName);
            }
        });
        task.show();
    }

    //更新用户信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpDateUserInfoEvent(UpDateUserInfoEvent event) {
        getUserInfo();
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.rl_nikename, R.id.rl_age, R.id.rl_tag,
            R.id.rl_signature, R.id.rl_height, R.id.rl_work, R.id.rl_hometown, R.id.rl_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if(switcher.isChecked()){
                    setWechatShow(0);
                }else {
                    setWechatShow(1);
                }
                break;
            case R.id.tv_right:
                ToastUtil.show("保存");
                break;
            case R.id.rl_nikename:
                intent.setClass(mContext, SetNameActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_age:
                agePicker.show(getFragmentManager(), "DatePickerDialogFragment");
                break;
            case R.id.rl_tag:
                intent.setClass(mContext, SetTagActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_signature:
                intent.setClass(mContext, SetSignatureActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_height:
                picker.show();
                break;
            case R.id.rl_work:
                intent.setClass(mContext, SetWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_hometown:
                onAddressPicker();
                break;
            case R.id.rl_wechat:
                intent.setClass(mContext, SetWechatActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(switcher.isChecked()){
            setWechatShow(0);
        }else {
            setWechatShow(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            intent = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
