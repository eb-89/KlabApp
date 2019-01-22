package me.eb.klabapp.roombase;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Dao
public interface PuzzleDao {
    @Query("SELECT * FROM puzzle")
    List<Puzzle> getAll();

    @Query("DELETE FROM puzzle")
    void nukeAll();


    @Insert
    void insert(Puzzle... p);

    @Delete
    void delete(Puzzle p);
}
