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
    private float lastX;
    private float startP;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(0xffefefef);
        int k = 30;
        int size = (mWidth - (int) startP) / 20+1;

        if(size>(mWidth/20+1)){
            size=mWidth/20+1;
        }

        Log.d("wang","========"+size);
        for (int i = 0; i < size; i++) {

            if (i % 10 == 0) {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(i * 20 + startP, 0, i * 20 + startP, 60, mPaint);

                //绘制数字
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(25);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((k++) + "", i * 20 + startP, 120, mPaint);
            } else {
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(i * 20 + startP, 0, i * 20 + startP, 30, mPaint);
            }

        }
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float d = lastX - x;
                lastX = x;
                startP += -d;
                invalidate();
                break;
        }

        return true;
    }

}

