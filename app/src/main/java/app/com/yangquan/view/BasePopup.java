package app.com.yangquan.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import app.com.yangquan.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class BasePopup extends BasePopupWindow {
    @BindView(R.id.tv_delete)
    TextView mTvDesc;

    public BasePopup(Context context) {
        super(context);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnife.bind(this, contentView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.base_pop_delete_chat);
    }

    public BasePopup setText(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }

    @OnClick(R.id.tv_delete)
    public void onViewClicked() {
        if (listener != null) {
            listener.onDeleteClick();
        }
        dismiss();
    }

    public interface DeletePopClick {
        void onDeleteClick();
    }

    private DeletePopClick listener;

    public void setDeletePopClick(DeletePopClick listener) {
        this.listener = listener;
    }
}
