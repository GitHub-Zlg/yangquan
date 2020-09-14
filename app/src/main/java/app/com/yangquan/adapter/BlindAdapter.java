package app.com.yangquan.adapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.com.yangquan.R;

public class BlindAdapter extends BaseQuickAdapter<String> {
    private long mLastClickBackTime;//上次点击的时间
    public BlindAdapter() {
        super(R.layout.item_blind, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        Glide.with(mContext).load(s).into((ImageView) holder.getView(R.id.img_bg));
        holder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止多次点击
                long curTime = System.currentTimeMillis();
                if (curTime - mLastClickBackTime > 1500) {
                    mLastClickBackTime = curTime;
                    if(listener!=null){
                        listener.onItemClick(holder.getView(R.id.img_bg),holder.getView(R.id.iv_heart),holder.getAdapterPosition(),s);
                    }
                    return;
                }
            }
        });

        holder.getView(R.id.iv_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation((ImageView) v);
            }
        });
    }

    private void animation(ImageView iv) {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        /**
         *
         * @param fromX 起始x轴位置，0为最小，1为原始，float形
         * @param toX 同上
         * @param fromY 同上T
         * @param toY 同上
         * @param pivotXType 用来约束pivotXValue的取值。取值有三种：Animation.ABSOLUTE，Animation.RELATIVE_TO_SELF，Animation.RELATIVE_TO_PARENT
         * Type：Animation.ABSOLUTE：绝对，如果设置这种类型，后面pivotXValue取值就必须是像素点；比如：控件X方向上的中心点，pivotXValue的取值mIvScale.getWidth() / 2f
         *      Animation.RELATIVE_TO_SELF：相对于控件自己，设置这种类型，后面pivotXValue取值就会去拿这个取值是乘上控件本身的宽度；比如：控件X方向上的中心点，pivotXValue的取值0.5f
         *      Animation.RELATIVE_TO_PARENT：相对于它父容器（这个父容器是指包括这个这个做动画控件的外一层控件）， 原理同上，
         * @param pivotXValue  配合pivotXType使用，原理在上面
         * @param pivotYType 同from/to
         * @param pivotYValue 原理同上
         */
        animation.setDuration(200);//设置持续时间
        animation.setFillAfter(true);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setRepeatCount(0);
        //设置循环次数，0为1次
        iv.startAnimation(animation);
    }

    public interface onBlindClickListener{
       void onItemClick(ImageView imageView,ImageView heart,int positon,String s);
    }

    public onBlindClickListener listener;

    public void setonBlindClickListener(onBlindClickListener listener){
        this.listener = listener;
    }

}
