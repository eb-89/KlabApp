package me.eb.klabapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import me.eb.klabapp.roombase.AccessDBTask;
import me.eb.klabapp.roombase.Puzzle;
import me.eb.klabapp.roombase.Sdb;
import me.eb.klabapp.roombase.TaskResponse;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SudokuSelectActivity extends AppCompatActivity implements TaskResponse {

    Sdb sdb;
    AccessDBTask gt;
    ListView listView;
    Intent startSudokuIntent;
    Puzzle sudokuParcel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_select);

        startSudokuIntent = new Intent(this, SudokuActivity.class);

        listView = (ListView) findViewById(R.id.listView);
        //getApplicationContext().deleteDatabase("sudokudb");

        sdb = Room.databaseBuilder(getApplicationContext(),
                Sdb.class, "sudokudb").build();
        gt = new AccessDBTask(sdb, this);
        gt.execute();



    }


    public void start() {

        startSudokuIntent.putExtra("sudokuParcel", sudokuParcel );
        startActivity(startSudokuIntent);
    }

    @Override
    public void processFinish(final List<Puzzle> puzzles) {


        ArrayAdapter<Puzzle> adapter = new SudokuAdapter(this, puzzles);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                sudokuParcel = puzzles.get(position);
                start();
            }
        });
        Log.i("RoomDB: ", "ProcessFinished");
        sdb.close();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


}
