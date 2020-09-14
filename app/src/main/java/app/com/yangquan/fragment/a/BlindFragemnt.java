package app.com.yangquan.fragment.a;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import app.com.yangquan.R;
import app.com.yangquan.activity.PersonHomeActivity;
import app.com.yangquan.adapter.BlindAdapter;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.bean.BlindBean;
import app.com.yangquan.http.Const;
import app.com.yangquan.util.BlurBitmapUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.view.GalleryLayoutManager;
import app.com.yangquan.view.Transformer;
import butterknife.BindView;

public class BlindFragemnt extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.like_me)
    ImageView likeMe;
    private int page = 1;
    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    private int selectPosition;

    @Override
    protected int getLayout() {
        return R.layout.fragment_blind;
    }

    @Override
    protected void init() {
        initRecycler();
        Glide.with(mContext).asGif().load("http://img95.699pic.com/photo/40139/8349.gif_wh860.gif").into(likeMe);
    }

    //数据
    private void tablelist(int page) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        map.put("page", page + "");
        get(Const.Config.tablelist, 1, map);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if (flag == 1) {
            BlindBean bean = new Gson().fromJson(message, BlindBean.class);
            if (bean != null) {
//                list.addAll(bean.getData());
//                adapter.setNewData(list);
            }
        }
    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    //初始化
    private void initRecycler() {
        GalleryLayoutManager manager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        manager.attach(recycler, 0);
        BlindAdapter adapter = new BlindAdapter();
        List<String> list = new ArrayList<>();
        list.add("https://pic4.zhimg.com/v2-2d34c7021a3164007d0aa38795954918_r.jpg?source=1940ef5c");
        list.add("https://pic1.zhimg.com/80/v2-37fc9e7a7c5193a6c68f63c4363e090f_720w.jpg?source=1940ef5c");
        list.add("https://pic1.zhimg.com/80/v2-99063b3ae65ffd0bb440e6ea6e626170_720w.jpg?source=1940ef5c");
        list.add("https://pic3.zhimg.com/80/v2-5ccd847abf5109f9f5c3ff93f5944580_720w.jpg?source=1940ef5c");
        list.add("https://pic3.zhimg.com/80/v2-dfc0d94d3504eebd6cf6e9502668ae12_720w.jpg?source=1940ef5c");
        adapter.setNewData(list);
        recycler.setAdapter(adapter);
        // 设置滑动缩放效果
        manager.setItemTransformer(new Transformer());
        manager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectPosition = position;
                //背景模糊
                Glide.with(mContext)
                        .asBitmap()
                        .load(adapter.getData().get(position))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mContext, resource, 15f);
                                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
                                // 获取前一页的Drawable
                                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);
                                //以下为淡入淡出效果
                                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
                                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                                ivBg.setImageDrawable(transitionDrawable);
                                transitionDrawable.startTransition(500);
                                // 存入到cache中
                                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
                            }
                        });
            }
        });

        adapter.setonBlindClickListener(new BlindAdapter.onBlindClickListener() {
            @Override
            public void onItemClick(ImageView imageView, ImageView heart, int positon, String url) {
                //判断点击的item是否是选中的item,如果是选中的(中间的)，就进入个人主页。如果不是，就移动到中间
                if (positon == selectPosition) {
                    Activity activity = (Activity) mContext;
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            new Pair(imageView, PersonHomeActivity.IMG_AVATAR),
                            new Pair(heart, PersonHomeActivity.IMG_HEART)
                    );
                    Intent intent = new Intent(activity, PersonHomeActivity.class);
                    intent.putExtra("head", url);
                    ActivityCompat.startActivity(activity, intent, options.toBundle());
                } else {
                    recycler.smoothScrollToPosition(positon);
                }
            }
        });
    }
}
