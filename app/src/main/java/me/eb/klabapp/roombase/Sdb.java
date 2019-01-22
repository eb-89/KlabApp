package me.eb.klabapp.roombase;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;

@Database(entities = {Puzzle.class}, version = 1,exportSchema = false)
public abstract class Sdb extends RoomDatabase {
    public abstract PuzzleDao puzzleDao();
}
