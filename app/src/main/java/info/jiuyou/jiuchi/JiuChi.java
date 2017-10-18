package info.jiuyou.jiuchi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * author ：jianshijiuyou@gmail.com
 * create date ：2017/10/16 0016  14:35
 * des ：
 */
public class JiuChi extends View {

    private Paint mPaint;
    private int mWidth;
    private int SCALE_SIZE = 50;

    private float curOffset;

    private int curIndex;


    private float lastX;


    private float offsetX;


    private int mLength;
    private float defaultOffset;


    public JiuChi(Context context) {
        this(context, null);
    }

    public JiuChi(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiuChi(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        mLength = mWidth / SCALE_SIZE + 2;
        if(mLength%2==0){
            defaultOffset = (mWidth%SCALE_SIZE-SCALE_SIZE)/2;
        }

        Log.d("wang", "=====onSizeChanged===");
        //curIndex=-mLength/2;
        //curOffset=curIndex*SCALE_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        // 绘制背景
        canvas.drawColor(0xffefefef);

        for (int i = 0; i < mLength; i++) {

            int startIndex=i + curIndex;

            if(startIndex<0){
                continue;
            }

            Log.d("wang", "=====onDraw==="+(i * SCALE_SIZE + offsetX+defaultOffset));
            if (startIndex % 10 == 0) {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(i * SCALE_SIZE + offsetX+defaultOffset, 0, i * SCALE_SIZE + offsetX+defaultOffset, 60, mPaint);

                //绘制数字
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(25);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((startIndex/10)+ "", i * SCALE_SIZE + offsetX+defaultOffset, 120, mPaint);
            } else {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(i * SCALE_SIZE + offsetX+defaultOffset, 0, i * SCALE_SIZE + offsetX+defaultOffset, 30, mPaint);
            }

        }


        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);
    }

    private void offsetX(float offset) {


        curOffset += offset;
        curIndex = (int) curOffset / SCALE_SIZE;

        offsetX=-curOffset%SCALE_SIZE;

        Log.d("wang", "=====curOffset==="+curOffset);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                offsetX(lastX - x);
                lastX = x;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("wang", "=====offsetX==="+offsetX);

                if(offsetX>0){
                    if(offsetX>(SCALE_SIZE/2)){
                        offsetX(offsetX-SCALE_SIZE*1.0f);
                        invalidate();
                    }else{
                        offsetX(offsetX);
                        invalidate();
                    }
                }else {
                    if(-offsetX>(SCALE_SIZE/2)){
                        offsetX(SCALE_SIZE*1.0f+offsetX);
                        invalidate();
                    }else{
                        offsetX(offsetX);
                        invalidate();
                    }
                }

                //Log.d("wang", "=====offsetX=eeeeeeeeeeee=="+offsetX);
                break;
        }

        return true;
    }


}

