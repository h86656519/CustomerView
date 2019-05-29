package com.example.customerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class BarGraphView extends View {
    private final int maxWidth = 600;
    private final int maxHeight = 550;
    private ArrayList<Point> pointList = new ArrayList<Point>();

    public BarGraphView(Context context) {
        super(context);
    }

    public BarGraphView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getBarGraphViewSize(maxWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        int height = getBarGraphViewSize(maxHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
    }
    private int getBarGraphViewSize(int defaultSize, int measureSpec) {
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

    }
}
