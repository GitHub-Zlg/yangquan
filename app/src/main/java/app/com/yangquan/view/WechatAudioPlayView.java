package app.com.yangquan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import app.com.yangquan.R;


public class WechatAudioPlayView extends View {
    private int color = Color.parseColor("#555555");
    private Paint paint;
    private int width;
    private int height;
    private RectF receiverLeft;
    private RectF receiverCenter;
    private RectF receiverRight;
    private RectF sendLeft;
    private RectF sendCenter;
    private RectF sendRight;
    private int strokeWidth;
    private boolean isReceiver;
    private boolean isAnimationed;
//    public static final AtomicInteger index = new AtomicInteger(0);
    public  int index ;
    private int mMsgId;


    public WechatAudioPlayView(Context context) {
        this(context,null);
    }

    public WechatAudioPlayView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WechatAudioPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WechatAudioPlayView);
        color = a.getColor(R.styleable.WechatAudioPlayView_color, Color.WHITE);
        isReceiver = a.getBoolean(R.styleable.WechatAudioPlayView_isReceiver,false);
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        strokeWidth = width/7;
        paint.setStrokeWidth(strokeWidth);
        messureSendLeft();
        messureSendCenter();
        messureSendRight();
        messureReceiverLeft();
        messureReceiverCenter();
        messureReceiverRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReceiver){
            if (isAnimationed){
                if (index==0){
                    drawReceiverlLeft(canvas);
                    index++;
                }else if (index==1){
                    drawReceiverlLeft(canvas);
                    drawReceiverCenter(canvas);
                    index++;
                }else {
                    drawReceiverlLeft(canvas);
                    drawReceiverCenter(canvas);
                    drawReceiverRight(canvas);
                    index=0;
                }
            }else {
                drawReceiverlLeft(canvas);
                drawReceiverCenter(canvas);
                drawReceiverRight(canvas);
            }

        }else {
            if (isAnimationed){
                if (index==0){
                    drawSendRight(canvas);
                    index++;
                }else if (index==1){
                    drawSendRight(canvas);
                    drawSendCenter(canvas);
                    index++;
                }else {
                    drawSendLeft(canvas);
                    drawSendCenter(canvas);
                    drawSendRight(canvas);
                    index=0;
                }
            }else {
                drawSendLeft(canvas);
                drawSendCenter(canvas);
                drawSendRight(canvas);
            }
        }
    }
    private void drawSendLeft(Canvas canvas){
        canvas.drawArc(sendLeft,-225,90,false,paint);
    }
    private void drawSendCenter(Canvas canvas){
        canvas.drawArc(sendCenter,-225,90,false,paint);
    }
    private void drawSendRight(Canvas canvas){
        canvas.drawCircle(63,height/2,2,paint);
    }

    private void drawReceiverlLeft(Canvas canvas){
        canvas.drawCircle(6,height/2,2,paint);
    }
    private void drawReceiverCenter(Canvas canvas){
        canvas.drawArc(receiverCenter,-45,90,false,paint);
    }
    private void drawReceiverRight(Canvas canvas){
        canvas.drawArc(receiverRight,-45,90,false,paint);
    }

    private void messureSendLeft(){
        sendLeft = new RectF(strokeWidth+strokeWidth*2
                ,-height/2+strokeWidth+strokeWidth*2
                ,width*2-strokeWidth-strokeWidth*2
                ,height+height/2-strokeWidth-strokeWidth*2);
    }
    private void messureSendCenter(){
        sendCenter = new RectF(strokeWidth+strokeWidth*4
                ,-height/2+strokeWidth+strokeWidth*4
                ,width*2-strokeWidth-strokeWidth*4
                ,height+height/2-strokeWidth-strokeWidth*4);
    }
    private void messureSendRight(){
        sendRight = new RectF(strokeWidth+strokeWidth*6
                ,-height/2+strokeWidth+strokeWidth*6
                ,width*2-strokeWidth-strokeWidth*6
                ,height+height/2-strokeWidth-strokeWidth*6);
    }

    private void messureReceiverLeft(){
        receiverLeft = new RectF(-width+strokeWidth+strokeWidth*6
                ,-height/2+strokeWidth+strokeWidth*6
                ,width-strokeWidth-strokeWidth*6
                ,height+height/2-strokeWidth-strokeWidth*6);
    }
    private void messureReceiverCenter(){
        receiverCenter = new RectF(-width+strokeWidth+strokeWidth*4
                ,-height/2+strokeWidth+strokeWidth*4
                ,width-strokeWidth-strokeWidth*4
                ,height+height/2-strokeWidth-strokeWidth*4);
    }
    private void messureReceiverRight(){
        receiverRight = new RectF(-width+strokeWidth+strokeWidth*2
                ,-height/2+strokeWidth+strokeWidth*2
                ,width-strokeWidth-strokeWidth*2
                ,height+height/2-strokeWidth-strokeWidth*2);
    }

    public void startAnimation(){
        isAnimationed = true;
       invalidate();
    }

    public void stopAnimation(){
        isAnimationed = false;
        invalidate();
    }

    public boolean isPlay(){
        if(isAnimationed){
            return true;
        }else {
            return false;
        }
    }

    public int getMsgId() {
        return mMsgId;
    }

    public void setMsgId(int msgId) {
        mMsgId = msgId;
    }
}
