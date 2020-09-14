package app.com.yangquan.fragment.person;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.adapter.TagAdapter;
import app.com.yangquan.adapter.TestAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.UserBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.PreferencesUtils;
import butterknife.BindView;

public class PersonFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    private View header;
    private UserBean bean;
    private TextView tvHeight;
    private TextView tvConstellation;
    private TextView tvWork;
    private TextView tvHometown;
    private TextView tvSignature;
    private TextView tvWechat;
    private TextView tvName;
    private TextView tvAge;
    private TagAdapter tagAdapter;
    private TagAdapter sameTagAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_person;
    }

    @Override
    protected void init() {
        initRecycler();
        userInfo();
    }

    //获取用户信息
    private void userInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        post(Const.Config.userInfo, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            bean = new Gson().fromJson(message, UserBean.class);
            if (bean != null) {
                tvName.setText(TextUtils.isEmpty(bean.getData().getNickname()) ? "" : bean.getData().getNickname());
                tvAge.setText(TextUtils.isEmpty(bean.getData().getAge()) ? "" : bean.getData().getAge() + "岁");
                tvWork.setText(TextUtils.isEmpty(bean.getData().getHangye()) ? "" : "职业:" + bean.getData().getHangye());
                tvHeight.setText(TextUtils.isEmpty(bean.getData().getHeight()) ? "" : "身高:" + bean.getData().getHeight());
                tvConstellation.setText(TextUtils.isEmpty(bean.getData().getConstellation()) ? ":" : "星座:" + bean.getData().getConstellation());
                tvHometown.setText(TextUtils.isEmpty(bean.getData().getRegion()) ? "" : bean.getData().getRegion());
                tvSignature.setText(TextUtils.isEmpty(bean.getData().getAutograph()) ? "" : bean.getData().getAutograph());
                tvWechat.setText(bean.getData().getWechat_show().equals("0") ? "用户暂时不对外公开微信号" : bean.getData().getWechat());
                tagAdapter.setNewData(bean.getData().getLabel());
                sameTagAdapter.setNewData(bean.getData().getLabel());
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化recylcer
    private void initRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        header = LayoutInflater.from(mContext).inflate(R.layout.header_person, null);
        TestAdapter adapter = new TestAdapter();
        recycler.setLayoutManager(lm);
        adapter.addHeaderView(header);
        recycler.setAdapter(adapter);
        //----------headerID------------
        tvName = header.findViewById(R.id.tv_name);
        tvAge = header.findViewById(R.id.tv_age);
        tvHeight = header.findViewById(R.id.tv_height);
        tvConstellation = header.findViewById(R.id.tv_constellation);
        tvWork = header.findViewById(R.id.tv_work);
        tvHometown = header.findViewById(R.id.tv_hometown);
        tvSignature = header.findViewById(R.id.tv_signature);
        tvWechat = header.findViewById(R.id.tv_wechat);
        RecyclerView recycler_tag = header.findViewById(R.id.recycler_tag);
        RecyclerView recycler_tag_same = header.findViewById(R.id.recycler_tag_same);
        //谷歌流式布局
        FlexboxLayoutManager manager = new FlexboxLayoutManager(mContext, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //谷歌流式布局
        FlexboxLayoutManager manager2 = new FlexboxLayoutManager(mContext, FlexDirection.ROW, FlexWrap.WRAP) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler_tag.setLayoutManager(manager);
        recycler_tag_same.setLayoutManager(manager2);
        tagAdapter = new TagAdapter(2);
        sameTagAdapter = new TagAdapter(3);
        recycler_tag.setAdapter(tagAdapter);
        recycler_tag_same.setAdapter(sameTagAdapter);
    }
}
