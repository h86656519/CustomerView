package com.example.customerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewPoints();
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

        LineChartView chartView = findViewById(R.id.chartView);
        chartView.setPoints(pointList);
    }
}
