package me.eb.klabapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;


public class SudokuActivity extends AppCompatActivity {

    SudokuGrid sg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        RelativeLayout mg = (RelativeLayout) findViewById(R.id.innerRL);
        sg = new SudokuGrid(this);


        //ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
        //        ViewGroup.LayoutParams.WRAP_CONTENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT);
        //sg.setLayoutParams(lparams);
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(300,300);
        //lp.setMargins(0,100,0,0);
        //sg.setLayoutParams(lp);

        mg.setGravity(Gravity.CENTER_HORIZONTAL);
        mg.addView(sg);

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
