package app.com.yangquan.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import app.com.yangquan.test.LinedRelativeLayout;
import app.com.yangquan.util.BigImageUtil;
import app.com.yangquan.util.PhotoUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

public class UpDateProfileActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate{
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
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.wechat)
    EditText wechat;
    @BindView(R.id.signature)
    EditText signature;
    @BindView(R.id.layout_edit)
    LinedRelativeLayout layoutEdit;

    @Override
    protected int getLayout() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {
        avatars.setDelegate(this);
        if(null != layoutEdit) {
            layoutEdit.setIgnoreFirstFocus(true);
            layoutEdit.setLineWidth(3);
            layoutEdit.setLineColor(getResources().getColor(R.color.blue_active));
            layoutEdit.setBendLength(70);
            layoutEdit.setLinePaddingBottom(5);
            layoutEdit.setLinePaddingLeft(0);
            layoutEdit.setLinePaddingRight(0);
            layoutEdit.setAnimDuration(500);
        }
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        PhotoUtil.picture(mContext, 4-avatars.getItemCount());
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
}
