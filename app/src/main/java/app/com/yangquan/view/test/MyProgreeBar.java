package app.com.yangquan.view.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import app.com.yangquan.R;
import app.com.yangquan.util.DpUtil;


/*
 * 炫酷进度条
 * */
public class MyProgreeBar extends View {
    private int mOuterColor = Color.BLUE; //外圈颜色
    private int mInnerColor = Color.RED; //内圈颜色
    private int mTextColor = Color.RED; //文字颜色
    private int mTextSize = 20; //文字大小
    private int mRingWidth = 15; //圆环宽度

    private Paint mOutPaint; //外环画笔
    private Paint mIntPaint; //内环画笔
    private Paint mTextPaint; //文字画笔

    private float progress = 0f; //进度
    private float maxProgress = 100f; //总进度

    public MyProgreeBar(Context context) {
        this(context, null);
    }

    public MyProgreeBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgreeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyProgreeBar);
        mOuterColor = array.getColor(R.styleable.MyProgreeBar_outColor, Color.BLUE);
        mInnerColor = array.getColor(R.styleable.MyProgreeBar_inColor, Color.RED);
        mTextColor = array.getColor(R.styleable.MyProgreeBar_progressTextColor, Color.RED);
        mRingWidth = (int) array.getDimension(R.styleable.MyProgreeBar_ringWidth, 15);
        mTextSize = DpUtil.dp2px((int) array.getDimension(R.styleable.MyProgreeBar_progressTextSize,20));
        array.recycle();

        //外环画笔
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStrokeWidth(mRingWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStyle(Paint.Style.STROKE);

        //内环画笔
        mIntPaint = new Paint();
        mIntPaint.setAntiAlias(true);
        mIntPaint.setStrokeWidth(mRingWidth);
        mIntPaint.setColor(mInnerColor);
        mIntPaint.setStrokeCap(Paint.Cap.ROUND);
        mIntPaint.setStyle(Paint.Style.STROKE);

        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    /*
     * 测量
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.max(width, height), Math.max(width, height));
    }

    /*
     * 画
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        //画外环
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, center-mRingWidth/2, mOutPaint);

        //画内弧
        RectF rectF = new RectF(mRingWidth/2,mRingWidth/2,getWidth()-mRingWidth/2,getHeight()-mRingWidth/2);
        if(progress==0)return;
        float sweepAngle = progress/maxProgress;
        canvas.drawArc(rectF,0f,sweepAngle*360,false,mIntPaint);

        //画文字
        String text = progress+"%";

        //文字起始位置，宽度的一半减去文字的一半
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),rect);
        int x = getWidth()/2-rect.width()/2;

        //基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int i = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int y = getHeight()/2+i;
        canvas.drawText(progress+"%",x,y,mTextPaint);
    }

    public void setProgress(int pro){
        this.progress = pro;
        invalidate();
    }
}
