package me.eb.klabapp.roombase;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.room.Room;

public class InsertTask extends AsyncTask<Void, Void, Void> {

    private Sdb sdb;
    private String instring;


    public InsertTask(Sdb s, String str) {
        this.instring = str;
        this.sdb = s;
    }

    @Override
    protected Void doInBackground(Void... v) {

        PuzzleDao pdao = sdb.puzzleDao();
        //pdao.insert(new Puzzle(instring) );
        Log.d("PuzzleLog:  ", "Added: " + instring);

        return null;
    }

}
