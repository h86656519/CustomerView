package com.example.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    Paint linePaint = new Paint();
    Paint baselinePaint = new Paint();
    Paint pointPaint = new Paint();
    Paint textPaint = new Paint();
    private final int orginalX = 50;
    private final int orginalY = 550;
    private final int maxWidth = 550;
    private final int maxHeight = 550;
    private int scale = 0;
    ArrayList<Point> pointList = new ArrayList<Point>();


    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMySize(maxWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        int height = getMySize(maxHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

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
        //50,50
        //70,90
        //90,80
        //110,200
        pointList.add(new Point(50, 50));
        pointList.add(new Point(100, 190));
        pointList.add(new Point(200, 180));
        pointList.add(new Point(300, 300));
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
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
        int px = 550;
        //baseLinePts
        float[] baseLinePts = {
                orginalX + getPaddingLeft(), getPaddingTop(), orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), //由上往畫
//              horzontalStartX, horzontalStartY, horzontalEndX, horzontalEndY,
                orginalX + getPaddingLeft(), maxHeight + getPaddingTop(), maxWidth + getPaddingLeft(), maxHeight + getPaddingTop(), //
        };
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(7);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(20);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i <= pointList.size() - 1; i++) {
//            canvas.drawPoint(pointList.get(i).x + orginalX, orginalY - pointList.get(i).y, pointPaint);
            canvas.drawPoint(pointList.get(i).x * (scale + 1) + orginalX + getPaddingLeft(), orginalY - pointList.get(i).y + getPaddingTop(), pointPaint);
        }

        for (int i = 0; i <= pointList.size() - 2; i++) {
            canvas.drawLine(pointList.get(i).x * (scale + 1) + orginalX + getPaddingLeft(),
                    orginalY - pointList.get(i).y + getPaddingTop(),
                    pointList.get(i + 1).x * (scale + 1) + orginalX + getPaddingLeft(),
                    orginalY - pointList.get(i + 1).y + getPaddingTop(),
                    linePaint);
        }

      
        //pixel
        textPaint.setTextSize(28);
        textPaint.setTextAlign(Paint.Align.CENTER);
        baselinePaint.setStrokeWidth(10);
        canvas.drawLines(baseLinePts, baselinePaint);
        canvas.drawText("0", orginalX + getPaddingLeft() - 20, orginalY + getPaddingTop(), textPaint);
        if (scale <= 10) {
            px = px - scale * 50;
        }
        canvas.drawText(String.valueOf(px), orginalX + getPaddingLeft() - 10,  getPaddingTop(), textPaint);

    }
}
