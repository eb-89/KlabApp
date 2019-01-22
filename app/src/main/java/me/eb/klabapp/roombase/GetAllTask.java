package me.eb.klabapp.roombase;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Random;

import androidx.room.Room;
import me.eb.klabapp.SudokuPuzzle;

public class GetAllTask extends AsyncTask<Void, Void, List<Puzzle>> {

    private Sdb sdb;
    private List<Puzzle> dbpuzzles;

    private static Random rand = new Random();

    public GetAllTask(Sdb s) {
        this.sdb = s;
    }

    @Override
    protected List<Puzzle> doInBackground(Void... v) {

        PuzzleDao pdao = sdb.puzzleDao();
        dbpuzzles = pdao.getAll();
        return dbpuzzles;

    }

    public Puzzle getRandom() {
        int len = dbpuzzles.size();
        int idx = rand.nextInt(len);
        return dbpuzzles.get(idx);
    }

}
