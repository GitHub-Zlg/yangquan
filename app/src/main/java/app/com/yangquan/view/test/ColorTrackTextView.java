package app.com.yangquan.view.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import app.com.yangquan.R;

/*
 * 字体变色View练习
 * */
public class ColorTrackTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int mOriginColor = Color.BLACK;//原始颜色
    private int mChangeColor = Color.RED;//变化颜色
    //实现一个文字两种颜色 - 绘制不变色字体的画笔
    private Paint mOriginPaint;
    //实现一个文字两种颜色 - 绘制变色字体的画笔
    private Paint mChangePaint;

    //变色的朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    public enum Direction{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }

    //变色的占比
    private float mCurrentProgress = 0.0f;

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        mOriginColor = array.getColor(R.styleable.ColorTrackTextView_originColor, Color.BLACK);
        mChangeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, Color.RED);
        //回收
        array.recycle();
        mOriginPaint = getPaintByColor(mOriginColor);
        mChangePaint = getPaintByColor(mChangeColor);
    }

    /*
     * 1.根据颜色获取画笔
     * */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        //设置字体大小，就是TextView的字体大小
        paint.setTextSize(getTextSize());
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //设置颜色
        paint.setColor(color);
        return paint;
    }


    //一个文字两种颜色：
    //利用canvas.clipRect()的API，可以剪裁区域  左边用一个画笔去画，右边用另一个画笔去画，其实就是不断的去改变中间值
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);   不用系统的onDraw方法，自己画文字
        int middle = (int) (mCurrentProgress * getWidth());
        if(mDirection==Direction.LEFT_TO_RIGHT) { //从左(红色)变到右(黑色)
            //绘制不变色区域
            drawText(canvas, mOriginPaint, middle, getWidth());
            //绘制变色区域
            drawText(canvas, mChangePaint, 0, middle);
        }else {                                   //从右(红色)变到左(红色)
            //绘制不变色区域
            drawText(canvas, mOriginPaint, 0, getWidth()-middle);
            //绘制变色区域
            drawText(canvas, mChangePaint, getWidth()-middle, getWidth());
        }
    }

    /*
    * 绘制text
    * */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        String text = getText().toString();
        canvas.save(); //保存画布
        canvas.clipRect(start, 0, end, getHeight());    //裁剪区域
        //起始位置，控件宽度的一半减去字体宽度的一半
        Rect bounds = new Rect();
        paint.getTextBounds(text , 0, text.length(), bounds);
        int dx = getWidth() / 2 - bounds.width() / 2;
        //基线
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, paint);
        canvas.restore(); //释放画布
    }


    /*
    * 设置变色朝向
    * */
    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    /*
    * 设置变色进度
    * */
    public void setmCurrentProgress(float progress){
        this.mCurrentProgress = progress;
        invalidate();
    }
}
