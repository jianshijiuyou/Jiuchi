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

import java.util.LinkedList;

/**
 * author ：jianshijiuyou@gmail.com
 * create date ：2017/10/16 0016  14:35
 * des ：
 */
@Deprecated
public class JiuChi2 extends View {

    private Paint mPaint;
    private int mWidth;
    private int SCALE_SIZE=20;
    private LinkedList<ChiItem> list;


    private float lastX;
//    private float offset;
//    private float startP;
//
//    private ChiQueue chiQueue;


    public JiuChi2(Context context) {
        this(context, null);
    }

    public JiuChi2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiuChi2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        list=new LinkedList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;

        int length=100;
        for (int i=0;i<length;i++){
            if(i%10==0){
                list.addLast(new ChiItem(1,i*SCALE_SIZE));
            }else {
                list.addLast(new ChiItem(0,i*SCALE_SIZE));
            }
        }



        Log.d("wang","=====onSizeChanged===");
    }

    @Override
    protected void onDraw(Canvas canvas) {


        // 绘制背景
        canvas.drawColor(0xffefefef);
        // 开始体重
        int k = 30;

        for (ChiItem item: list) {

            switch (item.getType()){
                case 0:
                    mPaint.setColor(Color.GRAY);
                    mPaint.setStrokeWidth(2);
                    canvas.drawLine(item.getX(), 0, item.getX(), 30, mPaint);
                    break;
                case 1:
                    mPaint.setColor(Color.GRAY);
                    mPaint.setStrokeWidth(4);
                    canvas.drawLine(item.getX(), 0, item.getX(), 60, mPaint);
                    //绘制数字
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextSize(25);
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText((k++) + "", item.getX(), 120, mPaint);
                    break;
            }

        }
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);
    }

    private void offsetX(float offset){
        Log.d("wang","=====offset==="+offset);
        while (offset>SCALE_SIZE*10){
            list.getFirst().offsetX(offset);
            ChiItem chiItem = list.removeFirst();
            chiItem.setX(list.getLast().getX()+SCALE_SIZE);
            list.addLast(chiItem);
        }
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

