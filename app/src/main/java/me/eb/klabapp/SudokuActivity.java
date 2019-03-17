package me.eb.klabapp;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.nio.channels.SelectableChannel;
import java.util.List;
import java.util.Random;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import me.eb.klabapp.roombase.AccessDBTask;
import me.eb.klabapp.roombase.Puzzle;
import me.eb.klabapp.roombase.Sdb;
import me.eb.klabapp.roombase.TaskResponse;


public class SudokuActivity extends AppCompatActivity implements TaskResponse {

    SudokuGrid sg;
    SudokuDigitSelector sds;
    Intent intent;
    SudokuController sController;
    Puzzle selectedPuzzle;

    Sdb sdb;
    AccessDBTask gt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //Constraint layout and set of constraints
        ConstraintLayout screen = (ConstraintLayout) findViewById(R.id.screenView);
        ConstraintSet cs = new ConstraintSet();



        //Image view and buttons
        ImageView img = (ImageView) findViewById(R.id.gameImageView);
        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttonLayout);

        //Grid and selector and puzzle
        intent = getIntent();
        selectedPuzzle = intent.getParcelableExtra("sudokuParcel");

        sds = new SudokuDigitSelector(this);
        sg = new SudokuGrid(this);

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

        sController = new SudokuController(sg, new SudokuPuzzle(selectedPuzzle), sds);

    }

    @Override
    public void processFinish(final List<Puzzle> dbpuzzles) {

        Random rand = new Random();
        int len = dbpuzzles.size();
        int idx = rand.nextInt(len);
        selectedPuzzle = dbpuzzles.get(idx);
        sController.setSudoku(new SudokuPuzzle(selectedPuzzle));
        sdb.close();

    }

    public void resetBoard(View v) {
        sController.setSudoku(new SudokuPuzzle(selectedPuzzle));
    }


    public void getRandomBoard(View v) {

        //Build the db
        sdb = Room.databaseBuilder(getApplicationContext(),
                Sdb.class, "sudokudb").build();

        gt = new AccessDBTask(sdb, this);
        gt.execute();

    }


    public void toggleGridFirst(View v) {
        sController.toggleGridFirst();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}


