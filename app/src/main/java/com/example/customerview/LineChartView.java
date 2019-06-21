package com.example.customerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class LineChartView extends View {
    Paint linePaint = new Paint();
    Paint baselinePaint = new Paint();
    Paint pointPaint = new Paint();
    Paint textPaint = new Paint();
    Paint gridPaint = new Paint();
    private final int orginalX = 50;
    private final int orginalY = 550;
    private int textSize = 50;
    private int ChartTextSize = 0;
    private int lineColor = 0;
    private final int maxWidth = 600;
    private final int maxHeight = 550;
    private final int textPadding = 30;
    private int scaleX = 0; //縮放比例
    private int perscaleX = 0; //縮放比例
    private int scaleY = 0; //縮放比例
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private int defultMaxX = 600;
    private int defultMaxY = 600;
    private int highlightPoint = -1; //-1 表示無用的值
    private int animatingPoint = -1;
    int highlightStrikeWidth = 20;
    GestureDetector detector = null;
    int lastscaleX = 0;

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        ChartTextSize = typedArray.getDimensionPixelSize(R.styleable.MyView_chart_textSize, 14);
        lineColor = typedArray.getColor(R.styleable.MyView_lineColor, Color.BLUE);

        typedArray.recycle();
        init();

    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getLineChartView(maxWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        int height = getLineChartView(maxHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
//正方形
//        if (width < height) {
//            height = width;
//        } else {
//            width = height;
//        }

        setMeasuredDimension(width, height);
    }

    private int getLineChartView(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {  //如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {  //如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.EXACTLY: {  //如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    private void init() {
        initDetector();
        textSize = ChartTextSize;
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        baselinePaint.setStrokeWidth(10);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(7);
        Log.i("132", "pointPaint.getColor  1  :   " + pointPaint.getColor());
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(20);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(3);

        pointList.add(new Point(50, 50));
        pointList.add(new Point(100, 190));
        pointList.add(new Point(200, 180));
        pointList.add(new Point(300, 300));
    }


    public void setMyScaleX(int scaleX) {
        if (scaleX < 0) {
            scaleX = Math.abs(scaleX); // scaleX 必須為正數
        }
        this.scaleX = scaleX;
        invalidate();
    }

    public void setScollScaleX(int scaleX) {
        if (scaleX < 0) {
            scaleX = Math.abs(scaleX); // scaleX 必須為正數
        }

        if (this.scaleX > 0) {
            //第二次後要抓上一次的scaleX = scaleX - 變動值
            Log.i("132", "setScollScaleX if lastscaleX : " + scaleX);
            Log.i("132", "setScollScaleX if scaleX : " + scaleX);
            if(  lastscaleX - scaleX < 0){
                this.scaleX = 0;
            }else{
                this.scaleX = lastscaleX - scaleX;
            }

        } else {
            //第一次走傳進來的scaleX 即可
            this.scaleX = scaleX;
            Log.i("132", "setScollScaleX else scaleX : " + scaleX);
        }

        lastscaleX = this.scaleX; // 記上一次的scaleX
        Log.i("132", "setScollScaleX scaleX : " + scaleX);
        invalidate();
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
        invalidate();
    }

    public void setPoints(List<Point> pointList) {
        this.pointList.clear();
        this.pointList.addAll(pointList);

        invalidate(); //更新view
    }



    public void initDetector() {
        // detector = new GestureDetector(getContext(),new MyGestureListener()); //用自訂的監聽器
        detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                for (int i = 0; i < pointList.size(); i++) {
                    int pointX = pointList.get(i).x * (scaleX + 1) + orginalX + getPaddingLeft();
                    int pointY = orginalY - pointList.get(i).y * (scaleX + 1) + getPaddingTop();
                    int range = 30; // 點擊的範圍
                    //取得點擊的i
                    //i = highlightPoint
                    //當 highlightPoint = i 時，將point 變成紅色
                    if (e.getX() > pointX - range && e.getX() < pointX + range && e.getY() > pointY - range && e.getY() < pointY + range) {
                        Toast.makeText(getContext(), "點到了 - " + i + " point", Toast.LENGTH_SHORT).show();
                        // i == touch point index
                        handleHighlightPoint(i);
                        doAnimation(i);
                    }
                }
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                perscaleX = (int) (e2.getX() - e1.getX()) / 100;
//                Log.i("132", "onScroll distanceX : " + distanceX);
                Log.i("132", "onScroll scale : " + perscaleX);
                setScollScaleX(perscaleX);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                final float FLING_MIN_DISTANCE = 100;
                final float FLING_MIN_VELOCITY = 150;
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    //X轴上的移动速度去绝对值进行比较
                    //判断x轴坐标如果第一次按下时的坐标减去第二次离开屏幕时的坐标大于我们设置的位移，就说明此时是向左滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getContext(), "向左滑动", Toast.LENGTH_SHORT).show();
                    int scale = (int) (e2.getX() - e1.getX()) / 100;
//                    Log.i("132", "left scale : " + scale);

                }
                if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    //判断x轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向右滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getContext(), "向右滑动", Toast.LENGTH_SHORT).show();
                    int scale = (int) (e2.getX() - e1.getX()) / 100;
//                    Log.i("132", "right scale: " + scale);


                }
                if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                        && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    //判断Y轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向下滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getContext(), "向下滑动", Toast.LENGTH_SHORT).show();
                }
                if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                        && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    //判断Y轴坐标如果第一次按下屏幕时的坐标减去第二次离开屏幕时的坐标大于我们设置的位移，就说明此时是向上滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getContext(), "向上滑动", Toast.LENGTH_SHORT).show();
                }
                return true;//【注】返回ture，意思为该触摸事件被该回调函数消耗掉了，不会再返回给在我们拦截的方法中
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;  //一定要設為true，因為第一個動作一定是點擊
            }
        });

    }

    private void drawGrid(Canvas canvas) {
        //抓x軸的長度
        //x軸的長度/10 = 需要有多少條直線,分10等分
        int xLineCount = defultMaxX / 50;
        for (int i = 1; i < xLineCount; i++) {
            int drawGridX = orginalX + getPaddingLeft() + 50 * (scaleX + 1) * i;
            //作法邏輯:當畫的線超過x軸的最大值時，就不畫背景的網線
            if (drawGridX > defultMaxX + getPaddingLeft()) {
                break;
            }
            canvas.drawLine(drawGridX,
                    getPaddingTop(),
                    drawGridX,
                    maxHeight + getPaddingTop(),
                    gridPaint);
        }

        //抓y軸的長度
        int yLineCount = defultMaxY / 50;
        for (int i = 1; i < yLineCount; i++) {
            int drawGridy = maxHeight + getPaddingTop() - 50 * (scaleY + 1) * i;
            //作法邏輯:當畫的線超過y軸的最小值時，就不畫背景的網線
            if (drawGridy < getPaddingTop()) {
                break;
            }
            canvas.drawLine(orginalX + getPaddingLeft(),
                    drawGridy,
                    maxWidth + getPaddingLeft(),
                    drawGridy,
                    gridPaint);
        }

    }

    private void drawDots(Canvas canvas) {
        for (int i = 0; i <= pointList.size() - 1; i++) {
            //作法邏輯:當畫的線超過x軸的最大值時，就不畫點
            int drawDotsX = pointList.get(i).x * (scaleX + 1) + orginalX + getPaddingLeft();
            int drawDotsY = orginalY - pointList.get(i).y * (scaleY + 1) + getPaddingTop();
            if (drawDotsX > defultMaxX + getPaddingLeft()) {
                break;
            }
            //作法邏輯:當畫的線超過y軸的最小值時，就不畫點
            if (drawDotsY < getPaddingTop()) {
                continue;
            }
            if (i == animatingPoint) {
                pointPaint.setStrokeWidth(highlightStrikeWidth);
            } else {
                pointPaint.setStrokeWidth(20);
            }
            if (i == highlightPoint) {
                pointPaint.setColor(Color.RED);
            } else {
                pointPaint.setColor(Color.BLUE);
            }
            canvas.drawPoint(drawDotsX, drawDotsY, pointPaint);
        }
    }

    //畫軸
    private void drawAxis(Canvas canvas) {
        int currentMaxX = defultMaxX;
        int currentMaxY = defultMaxY;
        float[] baseLinePts = {
                orginalX + getPaddingLeft(), getPaddingTop(), orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), //由上往畫
                orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop(), //
        };
        canvas.drawLines(baseLinePts, baselinePaint);
        canvas.drawText("0", orginalX + getPaddingLeft() - textPadding, orginalY + getPaddingTop(), textPaint);
        if (scaleX <= 10) {
            currentMaxX = defultMaxX / (scaleX + 1);
        }
        if (scaleY <= 10) {
            currentMaxY = defultMaxY / (scaleY + 1);
        }
        canvas.drawText(String.valueOf(currentMaxX), maxWidth + getPaddingLeft() + textPadding, currentMaxY, textPaint);
    }

    //畫折線
    private void drawLine(Canvas canvas) {
        for (int i = 0; i <= pointList.size() - 2; i++) {
            int startX = pointList.get(i).x * (scaleX + 1) + orginalX + getPaddingLeft();
            int stopX = pointList.get(i + 1).x * (scaleX + 1) + orginalX + getPaddingLeft();
            int startY = orginalY - pointList.get(i).y * (scaleY + 1) + getPaddingTop();
            int stopY = orginalY - pointList.get(i + 1).y * (scaleY + 1) + getPaddingTop();

            if (stopX > defultMaxX + getPaddingLeft()) {
                stopX = 600 + getPaddingLeft();
            } else {
                stopX = pointList.get(i + 1).x * (scaleX + 1) + orginalX + getPaddingLeft();
            }
            if (startX > defultMaxX + getPaddingLeft()) {
                return;
            }
            if (stopY < getPaddingTop()) {
                stopY = getPaddingTop();
            } else {
                stopY = orginalY - pointList.get(i + 1).y * (scaleY + 1) + getPaddingTop();
            }

            if (startY < getPaddingTop()) {
                return;
            }
            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAxis(canvas);
        drawGrid(canvas);
        drawLine(canvas);
        drawDots(canvas);
    }

    public int convertDpTodefultMaxX(int dpValue) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction() & MotionEvent.ACTION_MASK;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                //用迴圈取得點擊時第N個點
//                for (int i = 0; i < pointList.size(); i++) {
//                    int pointX = pointList.get(i).x * (scaleX + 1) + orginalX + getPaddingLeft();
//                    int pointY = orginalY - pointList.get(i).y * (scaleX + 1) + getPaddingTop();
//                    int range = 30; // 點擊的範圍
//                    //取得點擊的i
//                    //i = highlightPoint
//                    //當 highlightPoint = i 時，將point 變成紅色
//                    if (event.getX() > pointX - range && event.getX() < pointX + range && event.getY() > pointY - range && event.getY() < pointY + range) {
//                        Toast.makeText(getContext(), "點到了 - " + i + " point", Toast.LENGTH_SHORT).show();
//                        // i == touch point index
//                        handleHighlightPoint(i);
//                        doAnimation(i);
//                    }
//                }
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//
//            }
//            case MotionEvent.ACTION_MOVE: {
//                break;
//            }
//        }
//        invalidate();
//        return true;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    private void handleHighlightPoint(int touchPoint) {
        if (highlightPoint == touchPoint) {
            highlightPoint = -1;
        } else {
            highlightPoint = touchPoint;
        }
    }

    private void doAnimation(final int point) {
        ValueAnimator animator = ValueAnimator.ofInt(20, 30);
        animator.setDuration(60);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                highlightStrikeWidth = (int) animation.getAnimatedValue();
                invalidate(); // onDraw
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatingPoint = point;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //每次動畫跑完，animatingPoint 就改回初始值
                animatingPoint = -1;
            }
        });

        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE); //要先設setRepeatCount 才會有效果
        animator.start();
    }

}
