package me.eb.klabapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SudokuController {

    private SudokuGrid view;
    private SudokuDigitSelector selector;
    private SudokuPuzzle model;
    private SudokuDigit selectorSelectedDigit;
    private SudokuDigit gridSelectedDigit;
    private boolean gridFirst;


    private GestureDetector viewGd;
    private GestureDetector selectorGd;

    public SudokuController(SudokuGrid inview, SudokuPuzzle inmodel, SudokuDigitSelector inselector) {
        this.view = inview;
        this.model = inmodel;
        this.selector = inselector;
        this.gridFirst = true;

        view.setInitSudoku(model);

        viewGd = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();

                gridSelectedDigit = view.getSudokuDigit(x,y);

                if (gridFirst) {
                    if (gridSelectedDigit != null && model.isChangeable(gridSelectedDigit) ) {
                        view.resetHighlight();
                        view.animateHighlight(gridSelectedDigit);
                    }
                } else if (gridSelectedDigit!=null && selectorSelectedDigit != null  && model.isChangeable(gridSelectedDigit)) {
                    model.setDigit(gridSelectedDigit.x, gridSelectedDigit.y, selectorSelectedDigit.val);
                    selector.setHighlight(selectorSelectedDigit,false );
                    selectorSelectedDigit = null;
                }

                view.updateGrid();
                selector.updateSelector();
                return true;
            }
        });

        selectorGd = new GestureDetector(selector.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();

                selectorSelectedDigit = selector.getSudokuDigit(x,y);

                if (gridFirst) {
                    if (selectorSelectedDigit != null && gridSelectedDigit != null && model.isChangeable(gridSelectedDigit)) {

                        view.resetHighlight();
                        view.setHighlight(gridSelectedDigit);

                        List<SudokuDigit> conflicts = model.getConflictsOf(gridSelectedDigit.x, gridSelectedDigit.y, selectorSelectedDigit.val );
//                        Toast.makeText(view.getContext(), "conf size: " + conflicts.size() , Toast.LENGTH_SHORT).show();
                        if (conflicts.size() > 0) {
//                            view.setHighlight(conflicts);
                            view.blink(conflicts);
                        } else {
                            model.setDigit(gridSelectedDigit.x, gridSelectedDigit.y, selectorSelectedDigit.val);
                        }
                    }
                } else if (selectorSelectedDigit!=null) {
                    selector.setHighlight(selectorSelectedDigit,true );
                }

                view.updateGrid();
                selector.updateSelector();

                return true;
            }
        });

        view.setOnTouchListener(new SudokuTouchListener(viewGd));
        selector.setOnTouchListener(new SudokuTouchListener(selectorGd));
    }

    public void setSudoku(SudokuPuzzle p){
        model = p;
        view.setNewSudoku(model);
        view.updateGrid();
        gridSelectedDigit = null;
        selectorSelectedDigit = null;
        selector.updateSelector();
    }

    public void toggleGridFirst() {
        gridFirst = !gridFirst;
        view.resetHighlight();
        selector.resetHighlight();

        selectorSelectedDigit = null;
        gridSelectedDigit = null;

        view.updateGrid();
        selector.updateSelector();
    }



}
