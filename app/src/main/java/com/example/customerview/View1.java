package com.example.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class View1 extends View {
    Paint linePaint = new Paint();
    Paint baselinePaint = new Paint();
    Paint pointPaint = new Paint();

    ArrayList<Point> pointList = new ArrayList<Point>();

    public View1(Context context) {
        super(context);
        init();
    }

    public View1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public View1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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

    public View1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setXXX(List<Point> pointList) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Point point = new Point(1,1);
        //第一個點的x，第一個點的y，線的長度，結束點的y
//        float[] pts = {50, 100, 150, 50,
//                150, 50, 250, 150,
//                350, 120, 250, 150
//        };
        float[] baselinepts = {50, 500, 900, 500,
                50, 500, 50, 100,
        };
        linePaint.setColor(Color.RED);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(20);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        for (int i = 0; i <= pointList.size() - 1; i++) {
            canvas.drawPoint(pointList.get(i).x + 50, 500-pointList.get(i).y, pointPaint);
            canvas.drawLine(pointList.get(i).x + 50, 500-pointList.get(i).y,pointList.get(i + 1).x + 50,500-pointList.get(i + 1).y, pointPaint);
        }

//        canvas.drawPoint(50, 100, pointPaint);
//        canvas.drawPoint(150, 50, pointPaint);
//        canvas.drawPoint(250, 150, pointPaint);
//        canvas.drawPoint(350, 120, pointPaint);

        canvas.drawPoint(50, 500, pointPaint);
//        canvas.drawLines(pts, linePaint);

        baselinePaint.setTextSize(28);
        baselinePaint.setStrokeWidth(10);
        canvas.drawLines(baselinepts, baselinePaint);
        canvas.drawText("0", 20, 520, baselinePaint);

//        canvas.drawCircle(300, 300, 200, linePaint);
    }
}
