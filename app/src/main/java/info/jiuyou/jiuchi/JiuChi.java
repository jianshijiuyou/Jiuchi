package info.jiuyou.jiuchi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

/**
 * author ：jianshijiuyou@gmail.com
 * create date ：2017/10/16 0016  14:35
 * des ：
 */
public class JiuChi extends View {

    private Paint mPaint;
    private int mWidth;
    private int gapWidth = 50;

    private float offset;
    private int minValue = 30;
    private int maxValue = 50;
    private float perValue = 0.1f;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mLength;

    private float lastX;
    private int maximumFlingVelocity;
    private int minimumFlingVelocity;
    private int minOffset;
    private int maxOffset;


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
        mScroller = new OverScroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();

        maximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        minimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;

        mLength = mWidth / gapWidth + 1;
        offset = 0;
        minOffset = (int) (minValue * gapWidth / 0.1f);
        maxOffset = (int) (maxValue * gapWidth / 0.1f);
        Log.d("wang", "=====onSizeChanged===");
        //curIndex=-mLength/2;
        //curOffset=curIndex*SCALE_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        // 绘制背景
        canvas.drawColor(0xffefefef);


        if (offset<minOffset) {
            offset = minOffset;
        }

        if (offset>maxOffset) {
            offset = maxOffset;
        }

        float v = offset - mWidth / 2.0f;
        float fn = v / gapWidth;
        int n = (int) Math.ceil(fn);
        float startX = gapWidth * n - v;


        for (int i = 0; i < mLength; i++) {

            float x = startX + (i * gapWidth);

            if (n % 10 == 0) {

                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(x, 0, x, 60, mPaint);

                //绘制数字
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(25);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText((n * perValue) + "", x, 120, mPaint);
            } else {

                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(x, 0, x, 30, mPaint);
            }

            n++;
        }


        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);
    }

    private void offsetX(float o) {
        offset += o;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                offsetX(lastX - x);
                lastX = x;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                mVelocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > minimumFlingVelocity) {
                    mScroller.fling((int) offset, 0, -xVelocity / 4, 0, minOffset, maxOffset, 0, 0);
                    invalidate();

                } else {
                    int des = Math.round(offset / gapWidth) * gapWidth;

                    mScroller.startScroll((int) offset, 0, (int) (des - offset), 0);
                    invalidate();
                }

                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (!mScroller.computeScrollOffset()) {
                int des = Math.round(offset / gapWidth) * gapWidth;
                mScroller.startScroll((int) offset, 0, (int) (des - offset), 0);
            }
            offset = mScroller.getCurrX();
            invalidate();
        }
    }
}

