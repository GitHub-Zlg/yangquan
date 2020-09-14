package app.com.yangquan.view.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RatingView extends View {
    public RatingView(Context context) {
        this(context,null);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.max(width,height),Math.max(width,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        Path path = new Path();
        path.moveTo(getWidth()/2,0);
        path.lineTo(0,getHeight()/2);
//        path.lineTo();
//        canvas.drawPath();
    }
}
