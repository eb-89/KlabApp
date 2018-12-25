package me.eb.klabapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoSudokuSelect(View v) {
        Intent myIntent = new Intent(this, SudokuSelectActivity.class);
        startActivity(myIntent);
    }
    public void gotoSudokuMake(View v) {
        Intent myIntent = new Intent(this, SudokuMakeActivity.class);
        startActivity(myIntent);
    }

    public void deleteAll(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");

            final TextView sure = new TextView(this);
            sure.setText("   This will delete all Sudokus, are you sure?");
            sure.setGravity(Gravity.CENTER);
            builder.setView(sure);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAllSudokus();
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

    private void deleteAllSudokus() {


        File dir = getFilesDir();
        File[] files = dir.listFiles();


        for (File f : files) {
            //if (f.getName().equals(""))
                f.delete();

        }

    }


}
