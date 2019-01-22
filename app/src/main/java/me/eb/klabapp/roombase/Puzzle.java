

package me.eb.klabapp.roombase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Erik on 2019-01-20.
 */

@Entity
public class Puzzle {


    @PrimaryKey(autoGenerate = true)
    int pkey;

    @ColumnInfo(name = "data")
    public String data;

    public Puzzle(String data) {
        this.data = data;
    }



}
