package me.eb.klabapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;

public class SudokuController {

    private SudokuGrid view;
    private SudokuDigitSelector selector;
    private SudokuPuzzle model;


    GestureDetector viewGd;
    GestureDetector selectorGd;

    public SudokuController(SudokuGrid inview, SudokuPuzzle inmodel, SudokuDigitSelector inselector) {
        this.view = inview;
        this.model = inmodel;
        this.selector = inselector;

        viewGd = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();

                SudokuDigit s = view.getSudokuDigit(x,y);
//                model.setDigit(s.x,s.y,s.val);
                model.setDigit(0,0,1);
                model.setDigit(1,0,2);
                model.setDigit(2,0,3);

                Log.i("ControllerGridGest: ", "Digits: " + model.getDigit(0,0) + " " + model.getDigit(1,0) + " " + model.getDigit(2,0));

                view.updateGrid();
                if (s != null) {
                    Log.i("ControllerGridGest: ", "Digit at " + s.x + " " + s.y + " with val: " + s.val);
                }
                return true;
            }
        });

        selectorGd = new GestureDetector(selector.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();

                SudokuDigit s = selector.getSudokuDigit(x,y);
                selector.setSelected(s.val);
                selector.updateSelector();
                Log.i("ControllerSelectGest: ", "Tried to invalidate");
                Log.i("ControllerSelectGest: ", "Digit with val: " +s.val);
                return true;
            }
        });

        view.setOnTouchListener(new SudokuTouchListener(viewGd));
        selector.setOnTouchListener(new SudokuTouchListener(selectorGd));
    }


    public void setDigit(int x, int y, int digit) {
        model.setDigit(x, y, digit);
    }


}
