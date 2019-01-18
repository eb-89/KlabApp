package me.eb.klabapp;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class SudokuMakeActivity extends AppCompatActivity {

    SudokuGrid sg;
    String savename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_make);

        RelativeLayout mg = (RelativeLayout) findViewById(R.id.innerRL);
        sg = new SudokuGrid(this,null);
        //sg.setEditable(true);


        //ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
        //       ViewGroup.LayoutParams.WRAP_CONTENT
        //       ,ViewGroup.LayoutParams.WRAP_CONTENT);
        //sg.setLayoutParams(lparams);
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(300,300);
        //lp.setMargins(0,100,0,0);
        //sg.setLayoutParams(lp);

        mg.setGravity(Gravity.CENTER_HORIZONTAL);
        sg.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mg.addView(sg);

        //int res = getIntent().getIntExtra("res", R.raw.sudoku1);
        //sg.getDataFromText(res);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglePermanent);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //sg.setPutPermanentTiles(true);
                } else {
                    //sg.setPutPermanentTiles(false);
                }
            }
        });


    }

    public void resetBoard(View v) {
        sg.resetSudoku();
    }

    public void saveSudoku(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name your Sudoku");

        final EditText input = new EditText(this);
        //input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savename = input.getText().toString();
                sg.writeToBinaryFile(savename);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    // OLD
    public void loadSudoku(View v) {
        sg.readFromBinaryFile("hello");
    }


}
