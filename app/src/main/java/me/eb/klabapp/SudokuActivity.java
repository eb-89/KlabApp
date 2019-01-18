package me.eb.klabapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;


public class SudokuActivity extends AppCompatActivity {

    SudokuGrid sg;
    SudokuDigitSelector sds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //Constraint layout and set of constraints
        ConstraintLayout screen = (ConstraintLayout) findViewById(R.id.screenView);
        ConstraintSet cs = new ConstraintSet();

        //Image view and buttons
        ImageView img = (ImageView)  findViewById(R.id.gameImageView);
        LinearLayout buttons = (LinearLayout)  findViewById(R.id.buttonLayout);

        //Grid and selector

        sds = new SudokuDigitSelector(this);
        sg = new SudokuGrid(this, sds);

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
        cs.connect(sg.getId(), ConstraintSet.TOP, img.getId(), ConstraintSet.BOTTOM,10);

        cs.connect(sds.getId(), ConstraintSet.TOP, sg.getId(), ConstraintSet.BOTTOM,10);
        cs.connect(sds.getId(), ConstraintSet.BOTTOM, buttons.getId(), ConstraintSet.TOP,10);

        cs.applyTo(screen);

        String filename = getIntent().getStringExtra("filename");

        sg.readFromBinaryFile(filename);

    }

    public void resetBoard(View v) {
       sg.resetSudoku();
    }

    public void getRandomBoard( View v) {

        File dir = getFilesDir();
        File[] files = dir.listFiles();

        int i = (int) Math.round(Math.random() * (files.length-1));
        sg.readFromBinaryFile(files[i].getName());
    }


    //OLD
    View.OnClickListener changeText = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
            tv.setText("NowClicked" + tv.getId());

        }
    };

    //OLD
    public void populateGrid(GridLayout m){

        for (int j = 0; j < 81; j++) {
            TextView t = new TextView(this);
            ImageView bb = new ImageView(this);
            //Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.bluebox);
            //bb.setImageBitmap(Bitmap.createScaledBitmap(bMap,40,40,false));
            bb.setMaxHeight(10);
            bb.setMaxWidth(10);

            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT);

            GridLayout.LayoutParams gparams = new GridLayout.LayoutParams(lparams);

            gparams.setMargins(20,20,20,20);
            gparams.setGravity(Gravity.CENTER);

            t.setLayoutParams(gparams);
            bb.setLayoutParams(gparams);

            //t.setLayoutParams(m.getLayoutParams());

            /*
            t.setLayoutParams(new ViewGroup.LayoutParams(
                    m.getMeasuredWidth() / 9,
                    m.getMeasuredHeight() / 9));
               */

            //t.setWidth(30);
            //t.setHeight(30);

            t.setId(j);
            t.setOnClickListener(changeText);
            t.setText("" + j);
            //m.addView(t);
            //m.addView(bb);
        }
    }


}
