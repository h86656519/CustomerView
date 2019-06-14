package com.example.customerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    EditText x_ed, y_ed;
    int x, y = 0;
    private LineChartView chartView;
    float scale;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x_ed = findViewById(R.id.inputX);
        y_ed = findViewById(R.id.inputY);
        Button button = findViewById(R.id.button);
        chartView = findViewById(R.id.chartView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = Integer.valueOf(x_ed.getText().toString());
                y = Integer.valueOf(y_ed.getText().toString());
                generateNewPoints();
            }
        });

        SeekBar seekScaleX = findViewById(R.id.seekScaleX);
        seekScaleX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int scalex = progress / 10;
//                chartView.setScaleX(px2dip(scale));
                chartView.setScaleX(scalex);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekScaleY = findViewById(R.id.seekScaleY);
        seekScaleY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int scaley = progress / 10;
                chartView.setScaleY(scaley);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        chartView.setOnTouchListener(this); //要綁定
        detector=  new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final float FLING_MIN_DISTANCE = 100;
                final float FLING_MIN_VELOCITY = 150;
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    //X轴上的移动速度去绝对值进行比较
                    //判断x轴坐标如果第一次按下时的坐标减去第二次离开屏幕时的坐标大于我们设置的位移，就说明此时是向左滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getApplicationContext(), "向左滑动", 0).show();
                }
                if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    //判断x轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向右滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getApplicationContext(), "向右滑动", 0).show();
                }
                if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                        && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    //判断Y轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向下滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getApplicationContext(), "向下滑动", 0).show();
                }
                if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                        && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    //判断Y轴坐标如果第一次按下屏幕时的坐标减去第二次离开屏幕时的坐标大于我们设置的位移，就说明此时是向上滑动的,坐标的原点位于该控件的左上角
                    Toast.makeText(getApplicationContext(), "向上滑动", 0).show();
                }
                return true;//【注】返回ture，意思为该触摸事件被该回调函数消耗掉了，不会再返回给在我们拦截的方法中

            }
        });
    }


    private void generateNewPoints() {
        List<Point> pointList = new ArrayList<>();
        pointList.add(new Point(60, 10));
        pointList.add(new Point(120, 140));
        pointList.add(new Point(200, 180));
        pointList.add(new Point(400, 300));
        if (x != 0 && y != 0) {
            pointList.add(new Point(x, y));
        }
        chartView.setPoints(pointList);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
