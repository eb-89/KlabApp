package me.eb.klabapp;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import me.eb.klabapp.roombase.Puzzle;
import me.eb.klabapp.roombase.Sdb;


public class SudokuActivity extends AppCompatActivity {

    SudokuPuzzle activePuzzle;
    SudokuGrid sg;
    SudokuDigitSelector sds;
    Sdb sdb;
    Puzzle sudokuParcel;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //Constraint layout and set of constraints
        ConstraintLayout screen = (ConstraintLayout) findViewById(R.id.screenView);
        ConstraintSet cs = new ConstraintSet();

        /* Now done in XML.

        Resources res = KlabApp.getContext().getResources();
        int resId = res.getIdentifier("background", "drawable", "me.eb.klabapp");
        Drawable d = res.getDrawable(resId, KlabApp.getContext().getTheme());
        screen.setBackground(d);

        */
        //Image view and buttons
        ImageView img = (ImageView) findViewById(R.id.gameImageView);
        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttonLayout);

        //Grid and selector and puzzle
        intent = getIntent();
        Puzzle p = intent.getParcelableExtra("sudokuParcel");
        activePuzzle = new SudokuPuzzle(p);
        sds = new SudokuDigitSelector(this);
        sg = new SudokuGrid(this, sds, activePuzzle);

        //Ids for grid and selector
        sg.setId(View.generateViewId());
        sds.setId(View.generateViewId());


        //Add the grid and selector
        screen.addView(sg);
        screen.addView(sds);

        //Clone constraint set from screen
        cs.clone(screen);

        //Add constraints
        cs.constrainHeight(sg.getId(), ConstraintSet.MATCH_CONSTRAINT);
        cs.setDimensionRatio(sg.getId(), "1:1");

        cs.constrainHeight(sds.getId(), ConstraintSet.MATCH_CONSTRAINT_SPREAD);
        cs.constrainMaxHeight(sds.getId(), 100);

        //Connect the constraints
        cs.connect(sg.getId(), ConstraintSet.TOP, img.getId(), ConstraintSet.BOTTOM, 10);

        cs.connect(sds.getId(), ConstraintSet.TOP, sg.getId(), ConstraintSet.BOTTOM, 10);
        cs.connect(sds.getId(), ConstraintSet.BOTTOM, buttons.getId(), ConstraintSet.TOP, 10);

        cs.applyTo(screen);



    }


    public void resetBoard(View v) {
        sg.setSudoku(sg.getSudoku());
    }

    public void getRandomBoard(View v) {
        //sg.setSudoku(new SudokuPuzzle(gt.getRandom()));
    }

}


