package com.example.customerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private int scaleY = 0; //縮放比例
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private int defultMaxX = 600;
    private int defultMaxY = 600;
    private int highlightPoint = -1; //-1 表示無用的值
    int highlightStrikeWidth = 20 ;

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
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    private void init() {
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

//        pointList.add(new Point(5, 5));
//        pointList.add(new Point(10, 70));
//        pointList.add(new Point(20, 18));
//        pointList.add(new Point(30, 30));

        pointList.add(new Point(50, 50));
        pointList.add(new Point(100, 190));
        pointList.add(new Point(200, 180));
        pointList.add(new Point(300, 300));
    }


    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
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

            if (i == highlightPoint) {
                pointPaint.setColor(Color.RED);
                pointPaint.setStrokeWidth(highlightStrikeWidth);
            } else {
                pointPaint.setColor(Color.BLUE);
                pointPaint.setStrokeWidth(20);

            }

            canvas.drawPoint(drawDotsX, drawDotsY, pointPaint);
        }
    }

    //畫軸
    private void drawAxis(Canvas canvas) {
        int currentMaxX = defultMaxX;
        int currentMaxY = defultMaxY;
        float[] baseLinePts = {
//                verticalStartX, verticalStartY, verticalEndX, verticalEndY
                orginalX + getPaddingLeft(), getPaddingTop(), orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), //由上往畫
//              horzontalStartX, horzontalStartY, horzontalEndX, horzontalEndY
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
//                Log.i("132", " if pointList.get(i) : " + pointList.get(i));
            } else {
                stopX = pointList.get(i + 1).x * (scaleX + 1) + orginalX + getPaddingLeft();
//                Log.i("132", "else pointList.get(i) : " + pointList.get(i));
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                //用迴圈取得點擊時第N個點
                for (int i = 0; i < pointList.size(); i++) {
                    int pointX = pointList.get(i).x * (scaleX + 1) + orginalX + getPaddingLeft();
                    int pointY = orginalY - pointList.get(i).y * (scaleX + 1) + getPaddingTop();
                    int range = 30; // 點擊的範圍
                    //取得點擊的i
                    //i = highlightPoint
                    //當 highlightPoint = i 時，將point 變成紅色
                    if (event.getX() > pointX - range && event.getX() < pointX + range && event.getY() > pointY - range && event.getY() < pointY + range) {
                        Toast.makeText(getContext(), "點到了 - " + i + " point", Toast.LENGTH_SHORT).show();
                        // i == touch point index
                        handleHighlightPoint(i);
                        doAnimation();
                    }

                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
        }
        invalidate();
        return true;
    }

    private void handleHighlightPoint(int touchPoint) {
        if (highlightPoint == touchPoint) {
            highlightPoint = -1;

        } else {
            highlightPoint = touchPoint;

        }
    }

    private void doAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(20, 30);
        animator.setDuration(60);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                highlightStrikeWidth = (int) animation.getAnimatedValue();
//                Log.i("132", "highlightStrikeWidth : " + highlightStrikeWidth);
                invalidate(); // onDraw
            }
        });

        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE); //要先設setRepeatCount 才會有效果
        animator.start();
    }
}
