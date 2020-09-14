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
 * 自定义view练习 QQ计步器
 * */

public class QQStepView extends View {
    private int mOuterColor = Color.BLUE;
    private int mInnererColor = Color.RED;
    private int mBordeWidth = 7;
    private int mStepTextSize = 35;
    private int mStepTextColor = Color.BLACK;
    private Paint mOutPaint, mInnerPaint, mTextPaint; //外弧画笔,内弧画笔,文字画笔

    private int mStepMax = 0;//总步数
    private int mCurrentStep = 0;//当前步数

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //1.分析效果
        //2.确定自定义属性，编写attrs.xml
        //3.在布局中，使用
        //4.在自定义View 中获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnererColor = array.getColor(R.styleable.QQStepView_innerColor, mInnererColor);
        mBordeWidth = (int) array.getDimension(R.styleable.QQStepView_bordeWidth, DpUtil.dp2px(mBordeWidth));
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        //初始化外弧画笔
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);//抗锯齿
        mOutPaint.setStrokeWidth(mBordeWidth);
        mOutPaint.setColor(mOuterColor);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND); //设置末端为圆弧
        mOutPaint.setStyle(Paint.Style.STROKE); //画笔空心

        //初始化内弧画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);//抗锯齿
        mInnerPaint.setStrokeWidth(mBordeWidth);
        mInnerPaint.setColor(mInnererColor);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND); //设置末端为圆弧
        mInnerPaint.setStyle(Paint.Style.STROKE); //画笔空心

        //初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    //5.onMeasure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //调用者在布局文件中，可能是wrap_content 也可能宽高不一致
        //获取模式 如果是AT_MOST   就抛出异常，或者给定一个默认值
        //宽高不一致的时候，取一个最小值  因为该控件是一个正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);
        //设置宽高
        setMeasuredDimension(width > heigth ? heigth : width, width > heigth ? heigth : width);

    }

    //6.画外圆弧，内圆弧，文字

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //6.1 画外圆弧  出现：边缘没有显示完整  原因:描边有宽度
        int center = getWidth() / 2;
        int radius = center - mBordeWidth / 2;
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);//不懂---
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);

        //6.2画内圆弧  不能写死  是一个百分比  是用户设置的外面传的
        if (mStepMax == 0) return;
        float sweepAngle = (float) mCurrentStep / mStepMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);

        //6.3 画文字
        String mStep = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(mStep, 0, mStep.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(mStep, dx, baseLine, mTextPaint);

    }

    //7.其他处理 写几个方法让它动起来
    public void setStepMax(int stepMax) {
        this.mStepMax = stepMax;
    }

    public void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        invalidate();//重绘
    }

}
