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
    private int SCALE_SIZE = 20;

    private int curLength;

    private int curIndex;
    private int lastIndex;

    private float lastX;


    private float offsetX;


    private int mLength;


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

        mLength = mWidth / SCALE_SIZE + 1;


        Log.d("wang", "=====onSizeChanged===");
    }

    @Override
    protected void onDraw(Canvas canvas) {


        // 绘制背景
        canvas.drawColor(0xffefefef);
        // 开始体重
        int k = 30;

        for (int i = 0; i < mLength; i++) {

            if ((i + curIndex) % 10 == 0) {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(i * 20 + offsetX, 0, i * 20 + offsetX, 60, mPaint);

                //绘制数字
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(25);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((k++) + "", i * 20 + offsetX, 120, mPaint);
            } else {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(i * 20 + offsetX, 0, i * 20 + offsetX, 30, mPaint);
            }

        }


        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);
    }

    private void offsetX(float offset) {


        curLength += offset;
        curIndex = curLength / SCALE_SIZE;


        offsetX-=offset;
        if(lastIndex!=curIndex){
            offsetX=0;
            lastIndex=curIndex;
        }
//        if(Math.abs(offsetX)>SCALE_SIZE){
//            offsetX=0;
//        }

        //Log.d("wang", "=====offsetX==="+offsetX);
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
        }

        return true;
    }


}

