package me.eb.klabapp;

public class SudokuDigit {
    public int x;
    public int y;
    public int val;

    public SudokuDigit(int x, int y, int val) {
        this.x = x;
        this.y = y;
        this.val = val;
    }

    public SudokuDigit(int val) {
        this.val = val;
    }
}
