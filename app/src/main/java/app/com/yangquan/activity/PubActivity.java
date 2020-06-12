package app.com.yangquan.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.http.Const;
import app.com.yangquan.http.HttpManager;
import app.com.yangquan.util.BigImageUtil;
import app.com.yangquan.util.PhotoUtil;
import app.com.yangquan.util.PreferencesUtils;
import app.com.yangquan.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;


public class PubActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.et_pub)
    EditText etPub;
    @BindView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout snplMomentAddPhotos;

    @Override
    protected int getLayout() {
        return R.layout.activity_pub;
    }

    @Override
    protected void init() {
        PhotoUtil.picture(mContext, Const.Config.IMG_MAX-snplMomentAddPhotos.getItemCount());
        // 设置拖拽排序控件的代理
        snplMomentAddPhotos.setDelegate(this);
    }

    private void pub(String content,List<String> list){
        Map<String, File> map = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        params.put("uid",PreferencesUtils.getSharePreStr(mContext, Const.SharePre.userId));
        params.put("content",content);
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            map.put(file.getName(), file);
        }
        HttpManager.up(mContext,"pub", Const.Config.pub,1,"list[]",map,params,this);
    }

    @Override
    protected void onSuccess(int flag, String message) {
        if(flag == 1){
            ToastUtil.show("发布成功！");
            finish();
        }
    }

    @Override
    protected void onFailure(int flag, String error) {

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
                    snplMomentAddPhotos.addMoreData(img);
                    break;
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right: //发布
                pub(etPub.getText().toString().trim(),snplMomentAddPhotos.getData());
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        PhotoUtil.picture(mContext, Const.Config.IMG_MAX-snplMomentAddPhotos.getItemCount());
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        snplMomentAddPhotos.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        BigImageUtil.more(mContext,models,position);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        Log.e("zlg","排序发生变化");
    }
}
