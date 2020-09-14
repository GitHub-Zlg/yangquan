package app.com.yangquan.view.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/*
 * 仿58同城加载动画
 * */
public class ShapeView extends View {
    private Shape mCurrentShape = Shape.Cicler;
    private Paint mPaint;

    private enum Shape {
        Cicler, Square, Triangle
    }

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.max(width, height), Math.max(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentShape) {
            case Cicler: //画圆
                mPaint.setColor(Color.YELLOW);
                int center = getWidth() / 2;
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square: //画正方形
                mPaint.setColor(Color.RED);
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle: //画三角形  其实是画路径
                mPaint.setColor(Color.GREEN);
                Path path = new Path();
                path.moveTo(getWidth() / 2, 0);
                path.lineTo(0, (float) (getWidth() / 2 * Math.sqrt(3)));
                path.lineTo(getWidth(),(float) (getWidth() / 2 * Math.sqrt(3)));
                path.close();
                canvas.drawPath(path, mPaint);
                break;
        }
    }

    public void change() {
        switch (mCurrentShape) {
            case Cicler: //画圆
                mCurrentShape = Shape.Square;
                break;
            case Square: //画正方形
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle: //画三角形
                mCurrentShape = Shape.Cicler;
                break;
        }
        invalidate();
    }
}
