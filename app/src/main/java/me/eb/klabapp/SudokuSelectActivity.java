package me.eb.klabapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;


public class SudokuSelectActivity extends AppCompatActivity {

    //int chosenResource;
    private String[] sudokuNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_select);


        //sudokuNames = getAllRawResources();
        sudokuNames = getFileNames();

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new SudokuAdapter(this, sudokuNames);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                int count = (int) arg3;
                Toast.makeText(arg0.getContext(), "Chosen file: " + sudokuNames[count], Toast.LENGTH_LONG).show();

                //int chosenResource = getResources().getIdentifier(sudokuNames[count], "raw", "me.eb.klabapp");
                start(sudokuNames[count]);

            }
        });


    }


    public void start(String name) {

        gotoSudokuGame(name);
    }

    public void gotoSudokuGame(String name) {
        Intent startSudokuIntent = new Intent(this, SudokuActivity.class);
        startSudokuIntent.putExtra("filename", name);
        startActivity(startSudokuIntent);

    }


    private String[] getFileNames() {

        File dir = getFilesDir();
        File[] files = dir.listFiles();

        String[] out = new String[files.length];

        for (int i=0; i< files.length; i++) {
            out[i] = files[i].getName();
        }

        return out;
    }

    //OLD
    private String[] getAllRawResources() {
        Field fields[] = R.raw.class.getDeclaredFields() ;
        String[] names = new String[fields.length] ;

        try {
            for( int i=0; i< fields.length; i++ ) {
                Field f = fields[i] ;
                names[i] = f.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names ;
    }


}
