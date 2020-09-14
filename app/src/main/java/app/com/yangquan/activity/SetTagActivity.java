package app.com.yangquan.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.TagAdapter;
import app.com.yangquan.adapter.TagSelectAdapter;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.bean.UpDateUserInfoEvent;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;


public class SetTagActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.recycler_selete_tag)
    RecyclerView recyclerSeleteTag;
    @BindView(R.id.tv_do)
    TextView tvDo;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.tv_listen)
    TextView tvListen;
    @BindView(R.id.tv_watch)
    TextView tvWatch;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.loading)
    SpinKitView loading;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.recycler_bottom)
    RecyclerView recyclerBottom;
    private Drawable drawable;
    private TagAdapter bottomAdapter;
    private TagSelectAdapter topAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_set_tag;
    }

    @Override
    protected void init() {
        initDrawable();
        initTitle();
        initRecycler();
        initData(0);
        getUserInfo();
    }

    //获取个人信息
    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.userInfo, 1, map);
    }

    private void setLabel(String label) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        map.put("label", label);
        post(Const.Config.setLabel, 2, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if(flag == 1){
            UserBean bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null && bean.getData().getLabel().size()>0) {
               topAdapter.setNewData(bean.getData().getLabel());
            }
        }else if (flag == 2) {
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
            ToastUtil.show("设置标签失败请重试");
        }
    }

    //初始化RecyclerView
    private void initRecycler() {
        //谷歌流式布局
        FlexboxLayoutManager manager = new FlexboxLayoutManager(mContext, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //recycler不能设置同一个manager，否则报错
        FlexboxLayoutManager manager2 = new FlexboxLayoutManager(mContext, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerBottom.setLayoutManager(manager);
        recyclerSeleteTag.setLayoutManager(manager2);
        bottomAdapter = new TagAdapter(1);
        topAdapter = new TagSelectAdapter();
        recyclerBottom.setAdapter(bottomAdapter);
        recyclerSeleteTag.setAdapter(topAdapter);

        bottomAdapter.setOnTagClickLinsener(new TagAdapter.TagClickListener() {
            @Override
            public void onItemClick(String tag, int position) {
                Log.e("zlg","tag======"+tag);
                for (int i = 0; i < topAdapter.getData().size(); i++) {
                    Log.e("zlg","topAdapter.getItem(i)======"+topAdapter.getItem(i));
                    if (topAdapter.getItem(i).equals(tag)) {
                        ToastUtil.show("您已添加过 [" + tag + "] 标签了");
                        return;
                    }
                }

                if (topAdapter.getData().size() >= 15) {
                    ToastUtil.show("最多可添加15个标签");
                    return;
                }
                topAdapter.add(0, tag);
            }
        });

        topAdapter.setOnTagDeleteClickListener(new TagSelectAdapter.TagDeleteClickListener() {
            @Override
            public void onDeleteClick(String tag, int position) {
                topAdapter.remove(position);
            }
        });
    }

    //初始化导航栏
    private void initTitle() {
        tvTitle.setText("标签");
        tvRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.VISIBLE);
    }

    //初始化切换底部蓝色线条
    private void initDrawable() {
        // 使用代码设置drawableleft
        drawable = getResources().getDrawable(R.mipmap.icon_line_blue);
        // 这一步必须要做，否则不会显示。
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    @OnClick({R.id.iv_right, R.id.iv_back, R.id.tv_right, R.id.tv_do, R.id.tv_good, R.id.tv_listen, R.id.tv_watch, R.id.tv_play, R.id.tv_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                if (topAdapter.getData().size() <= 0) {
                    ToastUtil.show("暂未选择任何标签");
                    return;
                }

                List<String> list = new ArrayList<>();
                for (int i = 0; i < topAdapter.getData().size(); i++) {
                    list.add(topAdapter.getItem(i));
                }
                loading.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                String substring = list.toString();
                //去掉tostring之后自带的中括号以及空格
                String labe = substring.replace("[", "").replace("]", "").replace(" ", "");
                setLabel(labe);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.tv_do:
                resetButton(0);
                break;
            case R.id.tv_good:
                resetButton(1);
                break;
            case R.id.tv_listen:
                resetButton(2);
                break;
            case R.id.tv_watch:
                resetButton(3);
                break;
            case R.id.tv_play:
                resetButton(4);
                break;
            case R.id.tv_read:
                resetButton(5);
                break;
        }
    }

    //重置按钮状态
    private void resetButton(int index) {
        initData(index);
        switch (index) {
            case 0:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvDo.setCompoundDrawables(null, null, null, drawable);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvGood.setCompoundDrawables(null, null, null, null);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvListen.setCompoundDrawables(null, null, null, null);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWatch.setCompoundDrawables(null, null, null, null);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPlay.setCompoundDrawables(null, null, null, null);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRead.setCompoundDrawables(null, null, null, null);

                tvTip.setText("你最近在干什么?");
                break;
            case 1:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvDo.setCompoundDrawables(null, null, null, null);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvGood.setCompoundDrawables(null, null, null, drawable);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvListen.setCompoundDrawables(null, null, null, null);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWatch.setCompoundDrawables(null, null, null, null);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPlay.setCompoundDrawables(null, null, null, null);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRead.setCompoundDrawables(null, null, null, null);

                tvTip.setText("给我说说你的拿手绝活");
                break;
            case 2:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvDo.setCompoundDrawables(null, null, null, null);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvGood.setCompoundDrawables(null, null, null, null);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvListen.setCompoundDrawables(null, null, null, drawable);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWatch.setCompoundDrawables(null, null, null, null);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPlay.setCompoundDrawables(null, null, null, null);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRead.setCompoundDrawables(null, null, null, null);

                tvTip.setText("一起听听我的歌单吧");
                break;
            case 3:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvDo.setCompoundDrawables(null, null, null, null);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvGood.setCompoundDrawables(null, null, null, null);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvListen.setCompoundDrawables(null, null, null, null);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWatch.setCompoundDrawables(null, null, null, drawable);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPlay.setCompoundDrawables(null, null, null, null);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRead.setCompoundDrawables(null, null, null, null);

                tvTip.setText("最近在追什么影视剧?");
                break;
            case 4:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvDo.setCompoundDrawables(null, null, null, null);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvGood.setCompoundDrawables(null, null, null, null);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvListen.setCompoundDrawables(null, null, null, null);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWatch.setCompoundDrawables(null, null, null, null);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvPlay.setCompoundDrawables(null, null, null, drawable);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvRead.setCompoundDrawables(null, null, null, null);

                tvTip.setText("一起开黑吗?");
                break;
            case 5:
                tvDo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvDo.setCompoundDrawables(null, null, null, null);

                tvGood.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvGood.setCompoundDrawables(null, null, null, null);

                tvListen.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvListen.setCompoundDrawables(null, null, null, null);

                tvWatch.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWatch.setCompoundDrawables(null, null, null, null);

                tvPlay.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPlay.setCompoundDrawables(null, null, null, null);

                tvRead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvRead.setCompoundDrawables(null, null, null, drawable);

                tvTip.setText("静下心来，一起读一本书");
                break;
        }
    }

    //初始化一些tag，应该是从接口获取的，暂时先本地写死
    private void initData(int index) {
        List<String> list = new ArrayList<>();
        switch (index) {
            case 0:
                list.clear();
                list.add("上班族");
                list.add("工程师");
                list.add("发传单");
                list.add("律师");
                list.add("搬砖");
                list.add("模特");
                list.add("飞行员");
                list.add("销售");
                list.add("保险");
                list.add("甜品师");
                list.add("美容师");
                list.add("会计");
                list.add("旅游从业者");
                list.add("留学生");
                list.add("自由职业");
                list.add("学生");
                list.add("铲屎官");
                list.add("法律从业者");
                list.add("程序员");
                list.add("产品狗");
                list.add("HR");
                bottomAdapter.setNewData(list);
                break;
            case 1:
                list.clear();
                list.add("写作");
                list.add("篮球");
                list.add("收藏");
                list.add("健身");
                list.add("古风");
                list.add("汉服");
                list.add("cosplay");
                list.add("摄影");
                list.add("足球");
                list.add("跳舞");
                list.add("绘画");
                list.add("旅行");
                list.add("滑板");
                list.add("手工");
                list.add("唱歌");
                list.add("球鞋爱好者");
                bottomAdapter.setNewData(list);
                break;
            case 2:
                list.clear();
                list.add("尤克里里");
                list.add("吉他");
                list.add("钢琴");
                list.add("纯音乐");
                list.add("轻音乐");
                list.add("喊麦");
                list.add("摇滚");
                list.add("说唱");
                list.add("电音");
                list.add("朋克");
                list.add("古筝");
                list.add("古琴");
                list.add("周杰伦");
                list.add("小提琴");
                list.add("爵士");
                list.add("流行乐");
                bottomAdapter.setNewData(list);
                break;
            case 3:
                list.clear();
                list.add("隐秘的角落");
                list.add("漫威");
                list.add("海贼王");
                list.add("动漫");
                list.add("综艺");
                list.add("美剧");
                list.add("日剧");
                list.add("韩剧");
                list.add("肖申克的救赎");
                list.add("英剧");
                list.add("国产剧");
                list.add("鬼吹灯");
                list.add("网剧");
                list.add("宫崎骏");
                list.add("话剧");
                list.add("天空之城");
                list.add("话剧");
                bottomAdapter.setNewData(list);
                break;
            case 4:
                list.clear();
                list.add("LOL");
                list.add("DNF");
                list.add("王者荣耀");
                list.add("守望先锋");
                list.add("PUBG");
                list.add("第五人格");
                list.add("黎明杀机");
                list.add("狼人杀");
                list.add("Dota");
                list.add("我的世界");
                list.add("港诡实录");
                list.add("魔兽世界");
                list.add("csgo");
                list.add("QQ飞车");
                list.add("剑三");
                list.add("逆水寒");
                list.add("大话西游");
                bottomAdapter.setNewData(list);
                break;
            case 5:
                list.clear();
                list.add("村上春树");
                list.add("国学");
                list.add("历史");
                list.add("小说");
                list.add("极品家丁");
                list.add("文学");
                list.add("盗墓笔记");
                list.add("散文");
                list.add("推理");
                list.add("科幻");
                list.add("国漫");
                list.add("东野圭吾");
                list.add("哈利波特");
                list.add("哈利波特");
                list.add("言情");
                list.add("诗歌");
                list.add("韩寒");
                bottomAdapter.setNewData(list);
                break;

        }

    }
}
