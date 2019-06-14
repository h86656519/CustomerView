package com.example.customerview;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("132", "MyGestureListener onDown");
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

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
         //   Toast.makeText(getContext(), "MyGestureListener 向左滑动", Toast.LENGTH_SHORT).show();
        }
        if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            //判断x轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向右滑动的,坐标的原点位于该控件的左上角
        //    Toast.makeText(getContext(), "MyGestureListener 向右滑动", Toast.LENGTH_SHORT).show();
        }
        if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            //判断Y轴坐标如果第二次离开屏幕时的坐标减去第一次按下时的坐标大于我们设置的位移，就说明此时是向下滑动的,坐标的原点位于该控件的左上角
      //      Toast.makeText(getContext(), "MyGestureListener 向下滑动", Toast.LENGTH_SHORT).show();
        }
        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            //判断Y轴坐标如果第一次按下屏幕时的坐标减去第二次离开屏幕时的坐标大于我们设置的位移，就说明此时是向上滑动的,坐标的原点位于该控件的左上角
     //       Toast.makeText(getContext(), "MyGestureListener 向上滑动", Toast.LENGTH_SHORT).show();
        }
        return true;//【注】返回ture，意思为该触摸事件被该回调函数消耗掉了，不会再返回给在我们拦截的方法中

    }

}

