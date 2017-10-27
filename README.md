---
title: 自定义 view 之薄荷 app 的卷尺效果
date: 2017-10-26 13:14:15
categories: android
thumbnail: http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/dbc69bab-08c2-4b1c-8ebf-54fd1a29b896.png
tags:
- 自定义view
---
前几天看到 [HenCoder「仿写酷界面」活动——征稿](http://hencoder.com/activity-mock-1/) ，扔物线大大找了几个很酷炫的效果让读者们仿写，最后选出的作品效果也很好，但是没有讲解实现思路，很多人还是不知道怎么做，所以我决定自己动手撸一篇文章记录下学习过程，光看别人写的，自己不动手永远也学不会啊是吧。

我看了几个薄荷卷尺的源码，这里记录下一种实现思路，不一定是最优的方法，仅供学习参考。

先看效果吧，

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/7e2f7811-fb4a-4dd4-919d-b66238bb0495.gif)

## 分析
先看张我画的张图，

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/32a2b2e9-696b-4f37-8973-8f2c167581da.png)

可以想想成有一把尺子在屏幕上面左右滑动，需要绘制的部分就是尺子和屏幕重合的部分，那么绘制的关键点就是找到第一个刻度线的位置。

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/e179bd03-aa41-4bc9-a52b-163b957edaaa.png)

那么这里就是计算线段 E 的长度了，思路如下：

E=F-D
D=C%F
C=A-B

所以化简下就是：

E=F-((A-B)%F)

F: 最小刻度的宽度，已知条件。
A: 卷尺的偏移量，其实就是滑动偏移量，可以动态计算出结果。
B: 屏幕的一半，已知条件。

当然，也可以这样算: 
C 除以 F 向上取整就能得出第一个画的刻度是第几个刻度（比如这里得出是第 N 个 刻度），然后再 N*F-C 就是 第一个刻度的坐标了。

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/dbc69bab-08c2-4b1c-8ebf-54fd1a29b896.png)

第一个刻度位置找到了，那后面的自然也就出来了，现在问题是一共画多少个刻度呢，肯定只会画卷尺落在屏幕上的部分对不对，思路有两种:

- 屏幕宽度 除以 最小刻度的宽度，结果就是需要绘制的刻度个数。
- 在绘制每一个刻度的时候判断横坐标是否超出了屏幕宽度，超出了则结束绘制。


好，分析的差不多了，下面开始计算。

## 计算偏移量

重写 onTouchEvent 方法

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
  switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      lastX = event.getX();
      break;
    case MotionEvent.ACTION_MOVE:
      float x = event.getX();
      offset+=(lastX - x);
      lastX = x;
      //偏移量改变后记得重新绘制刻度
      invalidate();
      break;
  }
  return true;
}
```

接下来就可以画刻度了

## 绘制刻度

```java
@Override
protected void onDraw(Canvas canvas) {
  // 绘制背景
  canvas.drawColor(0xffefefef);

  // 计算第一个刻度的坐标
  float c = offset - mWidth / 2.0f;
  float fn = c / gapWidth;
  int n = (int) Math.ceil(fn);
  float startX = gapWidth * n - c;


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
      canvas.drawText((n ) + "", x, 120, mPaint);
    } else {

      mPaint.setColor(Color.GRAY);
      mPaint.setStrokeWidth(2);
      canvas.drawLine(x, 0, x, 30, mPaint);
    }

    n++;
  }

  // 绘制中线上的指针
  mPaint.setColor(Color.GREEN);
  mPaint.setStrokeWidth(8);
  canvas.drawLine(mWidth / 2, 0, mWidth / 2, 80, mPaint);

}
```

现在就可以初步看到效果了

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/0878144f-274e-4980-a46e-57f4c4aa2a9c.gif)



## 滑动优化处理

现在虽然可以滑动，但是很生硬，当手指离开屏幕立马就停止了。

需要让它有惯性的效果，就算手指松开，它也能自己滑动一段距离，这里就要用到 Scroller 和 VelocityTracker 。至于它们怎么用，网上一堆资料。

具体步骤:

- 初始化 Scroller ，VelocityTracker。
- 在手指松开时用 VelocityTracker 计算出速度。
- 用得到的速度结合 Scroller 滑动一段距离（记得重写 computeScroll ）。

> Scroller 是用来辅助计算滑动距离的，并不是直接操作滑动，就是说你告诉 Scroller 滑动的目标距离或者速度，Scroller 告诉你每个时间点应该滑动的距离，你再不断的根据这个距离重绘 view，就产生了滑动的效果。



初始化

```java
mScroller = new OverScroller(getContext());
mVelocityTracker = VelocityTracker.obtain();
//获得允许执行一个fling手势动作的最大速度值
maximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
//获得允许执行一个fling手势动作的最小速度值
minimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
```

手指松开时计算速度

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
  // 把事件交给 VelocityTracker 处理
  mVelocityTracker.addMovement(event);
  switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      lastX = event.getX();
		// 当手指按下的时候，让滑动停止
      if (!mScroller.isFinished()) {
        mScroller.abortAnimation();
      }
      //手指按下的时 重新进行速度追踪
      mVelocityTracker.clear();
      mVelocityTracker.addMovement(event);
      break;
    case MotionEvent.ACTION_MOVE:
      float x = event.getX();
      offset += (lastX - x);
      lastX = x;
      invalidate();
      break;
    case MotionEvent.ACTION_UP:
		// 计算 1000 毫秒内的速度，maximumFlingVelocity 用来限定速度的最大值
      mVelocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
      // 获取速度
      int xVelocity = (int) mVelocityTracker.getXVelocity();
      // 大于最小速度才滑动
      if (Math.abs(xVelocity) > minimumFlingVelocity) {
        // 让 Scroller 开始计算滑动
        mScroller.fling((int) offset, 0, -xVelocity / 2, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        invalidate();

      }
      break;
  }
  return true;
}
```

这里我把计算出来的速度除了2，不然感觉太快了，后面 MIN_VALUE 和 MAX_VALUE，是滑动的最小和最大偏移量，这里我没做限制，所以用的 int 类型的最大最小值，记住了，调用完mScroller.fling()方法后一定要invalidate() 方法，Scroller 是不会自己帮你刷新的。

重写 computeScroll 方法

```java
@Override
public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
    	//获取当前计算出的偏移量
        offset = mScroller.getCurrX();
        invalidate();
    }
}
```

再来看看效果

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/47debdd3-2efd-469f-a452-b20621c48865.gif)



接下来要在滑动停止时让指针和最近的刻度对齐，也就是在停止时再让卷尺自动滑动一段距离。

这里有两个地方要处理:

- 手指离开后触发了 fling，在 fling 结束后需要处理。
- 手指离开后速度不够，没有触发 fling ，也需要处理。

不多说，请看图

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/109043c2-fc49-40fe-acf9-a1ef43abe4ad.png)

需要滑动的距离 = Math.round（当前便偏移量 / 最小刻度）* 最小刻度 - 当前便偏移量



下面看代码

```java
int des = Math.round(offset / gapWidth) * (int) gapWidth;
mScroller.startScroll((int) offset, 0, (int) (des - offset), 0);
invalidate();
```

具体位置大家可以去查看整个源码。这里就不在全部贴出来了。

看看效果吧

![](http://os6ycxx7w.bkt.clouddn.com/github/blog/customviewjiuchi/4f649b41-98a8-4845-8157-c202782902c4.gif)

最后就是最大值和最小值的限制了，这个在绘制的时候判断一下偏移量就可以了，大家可以自己动手试试哦。

哦，还有滑动时候的监听，这个在偏移量改变的时候调用自定义的回调函数就好了。

源码地址:[https://github.com/jianshijiuyou/Jiuchi](https://github.com/jianshijiuyou/Jiuchi) 。

源码仅供学习使用，如果要在项目中使用不妨看看下面这个:

[https://github.com/totond/BooheeRuler](https://github.com/totond/BooheeRuler) 

同时，实现思路也参考了下面这篇博客:

[http://www.jianshu.com/p/06e65ef3f3f1](http://www.jianshu.com/p/06e65ef3f3f1) 