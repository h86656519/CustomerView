package com.example.customerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText x_ed, y_ed;
    int x, y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x_ed = findViewById(R.id.inputX);
        y_ed = findViewById(R.id.inputY);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = Integer.valueOf(x_ed.getText().toString());
                y = Integer.valueOf(y_ed.getText().toString());
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
        if (x != 0 && y != 0) {
            pointList.add(new Point(x, y));
        }
        LineChartView chartView = findViewById(R.id.chartView);
        chartView.setPoints(pointList);
    }
}
