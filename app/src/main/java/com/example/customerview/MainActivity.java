package com.example.customerview;

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

}
