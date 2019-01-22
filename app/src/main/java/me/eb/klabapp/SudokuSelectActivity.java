package me.eb.klabapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class SudokuSelectActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_select);

        ListView listView = (ListView) findViewById(R.id.listView);

        String[] stringstub = {"one", "two", "three"};
        ArrayAdapter<String> adapter = new SudokuAdapter(this, stringstub);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                start();
            }
        });

    }

    public void start() {
        gotoSudokuGame();
    }

    public void gotoSudokuGame() {
        Intent startSudokuIntent = new Intent(this, SudokuActivity.class);
        startActivity(startSudokuIntent);

    }


}
