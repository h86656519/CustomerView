package com.example.customerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    Paint linePaint = new Paint();
    Paint baselinePaint = new Paint();
    Paint pointPaint = new Paint();
    Paint textPaint = new Paint();
    Paint LatticePaint = new Paint();
    private final int orginalX = 50;
    private final int orginalY = 550;
    private int textSize = 50;
    private int ChartTextSize = 0;
    private int lineColor = 0;
    private final int maxWidth = 600;
    private final int maxHeight = 550;
    private final int textPadding = 30;
    private int scale = 0; //縮放比例
    ArrayList<Point> pointList = new ArrayList<Point>();

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
        int width = getMySize(maxWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        int height = getMySize(maxHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
//正方形
//        if (width < height) {
//            height = width;
//        } else {
//            width = height;
//        }

        setMeasuredDimension(width, height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
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
        textSize = convertDpTodefultMaxX(ChartTextSize);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        baselinePaint.setStrokeWidth(10);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(7);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(20);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        LatticePaint.setColor(Color.GRAY);
        LatticePaint.setStrokeWidth(7);

        pointList.add(new Point(50, 50));
        pointList.add(new Point(100, 190));
        pointList.add(new Point(200, 180));
        pointList.add(new Point(300, 300));
    }


    public void setScale(int scale) {
        this.scale = scale;
        invalidate();
    }

    public void setPoints(List<Point> pointList) {
        this.pointList.clear();
        this.pointList.addAll(pointList);

        invalidate(); //更新view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int defultMaxX = 600;
        //baseLinePts
        float[] baseLinePts = {
//                verticalStartX, verticalStartY, verticalEndX, verticalEndY
                orginalX + getPaddingLeft(), getPaddingTop(), orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), //由上往畫
//              horzontalStartX, horzontalStartY, horzontalEndX, horzontalEndY
                orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop(), //
        };

        float[] LatticePaintPts = {
                orginalX + getPaddingLeft() + 10, maxHeight + getPaddingTop() - 50 * (scale + 1), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop() - 50 * (scale + 1), //
                orginalX + getPaddingLeft() + 10, maxHeight + getPaddingTop() - 100 * (scale + 1), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop() - 100 * (scale + 1), //
                orginalX + getPaddingLeft() + 10, maxHeight + getPaddingTop() - 150 * (scale + 1), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop() - 150 * (scale + 1), //
                orginalX + getPaddingLeft() + 10, maxHeight + getPaddingTop() - 200 * (scale + 1), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop() - 200 * (scale + 1), //
                orginalX + getPaddingLeft() + 50 * (scale + 1), getPaddingTop() + 10, orginalX + getPaddingLeft() + 50 * (scale + 1), maxHeight + getPaddingTop() + 10,
                orginalX + getPaddingLeft() + 100 * (scale + 1), getPaddingTop() + 10, orginalX + getPaddingLeft() + 100 * (scale + 1), maxHeight + getPaddingTop() + 10,
                orginalX + getPaddingLeft() + 150 * (scale + 1), getPaddingTop() + 10, orginalX + getPaddingLeft() + 150 * (scale + 1), maxHeight + getPaddingTop() + 10,
                orginalX + getPaddingLeft() + 200 * (scale + 1), getPaddingTop() + 10, orginalX + getPaddingLeft() + 200 * (scale + 1), maxHeight + getPaddingTop() + 10,
        };
        Log.i("123", "LatticePaintPts[0] : " + LatticePaintPts[0]);

        canvas.drawLines(LatticePaintPts, LatticePaint);

        for (int i = 0; i <= pointList.size() - 2; i++) {
            canvas.drawLine(pointList.get(i).x * (scale + 1) + orginalX + getPaddingLeft(),
                    orginalY - pointList.get(i).y + getPaddingTop(),
                    pointList.get(i + 1).x * (scale + 1) + orginalX + getPaddingLeft(),
                    orginalY - pointList.get(i + 1).y + getPaddingTop(),
                    linePaint);
        }

        for (int i = 0; i <= pointList.size() - 1; i++) {
//            canvas.drawPoint(pointList.get(i).x * (scale + 1) + orginalX, orginalY - pointList.get(i).y, pointPaint);
            // orginalX + getPaddingLeft() + 50 * (scale + 1)
            canvas.drawPoint(pointList.get(i).x * (scale + 1) + orginalX + getPaddingLeft(), orginalY - pointList.get(i).y + getPaddingTop(), pointPaint);
//            Log.i("123", "point " + i + " - " + (pointList.get(i).x * (scale + 1) + orginalX + getPaddingLeft()));
//            Log.i("123", "x " + i + " - " + pointList.get(i).x);
        }

        canvas.drawLines(baseLinePts, baselinePaint);
        if (scale <= 10) {
            defultMaxX = defultMaxX - (scale - 1) * 60;
        }

        canvas.drawText("0", orginalX + getPaddingLeft() - textPadding, orginalY + getPaddingTop(), textPaint);
        canvas.drawText(String.valueOf(defultMaxX), maxWidth + getPaddingLeft() + textPadding, maxHeight + getPaddingTop(), textPaint);

    }

    public int convertDpTodefultMaxX(int dpValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }
}
