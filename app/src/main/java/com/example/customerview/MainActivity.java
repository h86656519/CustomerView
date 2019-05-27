package com.example.customerview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText x_ed, y_ed;
    int x, y = 0;
    private LineChartView chartView;
    float scale;

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

        SeekBar seekScale = findViewById(R.id.seekScale);
        seekScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //progress 0-100
                // 0-10 -> 100
                // 10 - 20 -> 200
                // ......
                // 90-100 -> 1000
                int scale = progress / 10;
//                chartView.setScale(px2dip(scale));
                chartView.setScale(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        Button button = new Button(this);
//        button.setText();
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

    public int px2dip(float pxValue) {
        scale = this.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
