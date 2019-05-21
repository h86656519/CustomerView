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
    private final int orginalX =50;
    private final int orginalY = 500;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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

    public void setPoints(List<Point> pointList) {
        this.pointList.clear();
        this.pointList.addAll(pointList);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //baseLinePts
        float[] baseLinePts = {50, 500, 900, 500,
                50, 500, 50, 100,
        };
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(7);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(20);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i <= pointList.size() - 1; i++) {
            canvas.drawPoint(pointList.get(i).x + orginalX, orginalY - pointList.get(i).y, pointPaint);
        }

        for (int i = 0; i <= pointList.size() - 2; i++) {
            canvas.drawLine(pointList.get(i).x + orginalX,
                    orginalY - pointList.get(i).y,
                    pointList.get(i + 1).x + orginalX,
                    orginalY - pointList.get(i + 1).y,
                    linePaint);
        }

        canvas.drawPoint(orginalX, orginalY, pointPaint);
        baselinePaint.setTextSize(28);
        baselinePaint.setStrokeWidth(10);
        canvas.drawLines(baseLinePts, baselinePaint);
        canvas.drawText("0", 20, 520, baselinePaint);

    }
}
