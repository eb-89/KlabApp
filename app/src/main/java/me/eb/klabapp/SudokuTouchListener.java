package me.eb.klabapp;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SudokuTouchListener implements View.OnTouchListener {

    private GestureDetector gd;

    public SudokuTouchListener(GestureDetector gd) {
        this.gd = gd;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        gd.onTouchEvent(e);
        return true;
    }

}
