package app.com.yangquan.fragment;

import android.Manifest;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.activity.LoginActivity;
import app.com.yangquan.adapter.TestAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.listener.MainAppBarLayoutListener;
import app.com.yangquan.util.BigImageUtil;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.CoustomDialog;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MeFragment extends BaseFragment {
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.avater)
    RoundedImageView avater;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_name_top)
    TextView tvNameTop;
    @BindView(R.id.iv_share_top)
    ImageView ivSettingTop;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.ll_age)
    LinearLayout llAge;
    @BindView(R.id.tv_work)
    TextView tvWork;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private CoustomDialog dialog;
    private Intent intent;
    private String avaterPath;

    @Override
    protected int getLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init() {
        intent = new Intent();
        initListener();
        initRecycler();
    }

    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.userInfo, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                avaterPath = bean.getData().getUser_img();
                Glide.with(mContext).load(bean.getData().getUser_img()).placeholder(R.mipmap.icon_avater).into(avater);
                tvName.setText(bean.getData().getNickname());
                tvNameTop.setText(bean.getData().getNickname());
                tvAge.setText(bean.getData().getAge());
                tvWork.setText("职业：" + bean.getData().getHangye());
                if (bean.getData().getSex().equals("1")) {
                    ivSex.setImageResource(R.mipmap.icon_sex_m);
                } else {
                    ivSex.setImageResource(R.mipmap.icon_sex_w);
                }
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化recycler
    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        TestAdapter adapter = new TestAdapter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        adapter.setNewData(list);
        recycler.setAdapter(adapter);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getUserInfo();
        }
    }

    @Override
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
    }

    //获取权限
    private void getPermission(String path) {
        if (EasyPermissions.hasPermissions(mContext, permissions)) {
            //已经打开权限
            BigImageUtil.single(mContext,path);
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }
    }

    //滑动监听
    private void initListener() {
        mAppBarLayoutListener = new MainAppBarLayoutListener() {
            @Override
            public void onOffsetChanged(float rate) {
                if (rate > 0.020) {
                    rlTop.setVisibility(View.VISIBLE);
                    ivSetting.setVisibility(View.GONE);
                    rlTop.setAlpha(rate);
                } else {
                    rlTop.setVisibility(View.INVISIBLE);
                    ivSetting.setVisibility(View.VISIBLE);
                }
            }
        };
        mNeedDispatch = true;
    }

    @OnClick({R.id.iv_setting, R.id.ll_edit, R.id.avater, R.id.iv_share_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                dialog = new CoustomDialog(mContext)
                        .setTitle("是否退出当前账号？")
                        .setSingle(true)
                        .setOnClickBottomListener(new CoustomDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                PreferencesUtils.putSharePre(mContext, Const.SharePre.userId, "");
                                intent.setClass(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegtiveClick() {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                break;
            case R.id.ll_edit:
                ToastUtil.show("编辑");
                break;
            case R.id.avater:
                getPermission(avaterPath);
                break;
            case R.id.iv_share_top:
                ToastUtil.show("分享");
                break;
        }
    }
}
