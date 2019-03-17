package me.eb.klabapp.roombase;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.room.Room;
import me.eb.klabapp.SudokuActivity;
import me.eb.klabapp.SudokuPuzzle;

public class AccessDBTask extends AsyncTask<Void, Void, List<Puzzle>> {

    private Sdb sdb;
    private PuzzleDao pdao;
    private List<Puzzle> dbpuzzles;
    private TaskResponse tr;


    private static Random rand = new Random();


    // TODO: db and activity, so we can run the string code in there
    public AccessDBTask(Sdb s, TaskResponse tr) {
        this.sdb = s;
        this.tr = tr;

    }

    @Override
    protected List<Puzzle> doInBackground(Void... v) {
        this.pdao = sdb.puzzleDao();
        this.dbpuzzles = pdao.getAll();
        //dbpuzzles.add(new Puzzle("a", "b", "c"));
        //pdao.insert(new Puzzle("testsud1", "easy", "800000700034009210100830064000000025380562091920000000670021003018300940003000002"));
        //pdao.insert(new Puzzle("testsud2", "easy", "908310040070040893032000065000007600005103900007800000780000510126080030090031208"));
        //pdao.insert(new Puzzle("testsud3", "easy", "370612508200030000851009000020480000900000005000075080000300426000050003607824059"));
        return dbpuzzles;
    }

    @Override
    protected void onPostExecute(List<Puzzle> puzzles) {
        tr.processFinish(puzzles);
    }


}
