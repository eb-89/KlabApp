package me.eb.klabapp;

/**
 * Created by Erik on 2019-01-02.
 */

public class SudokuPuzzle {

    private int[][] puzzle =  {
        {5,3,7,2,8,0,0,0,0},
        {0,0,0,0,3,9,7,4,2},
        {2,0,4,0,0,1,5,3,0},
        {0,1,5,0,7,0,8,0,9},
        {3,8,0,9,0,0,0,7,1},
        {0,0,9,1,4,8,3,0,0},
        {0,0,0,4,1,0,9,8,3},
        {9,4,3,8,0,2,0,0,0},
        {1,0,8,0,0,6,0,5,4}};

    public SudokuPuzzle() {
    }

    public int getDigit(int x, int y) {
        return puzzle[x][y];
    }


}
