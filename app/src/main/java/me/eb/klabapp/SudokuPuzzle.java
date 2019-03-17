package me.eb.klabapp;

import java.util.ArrayList;
import java.util.List;

import me.eb.klabapp.roombase.Puzzle;


public class SudokuPuzzle {


    private List<SudokuDigit> digits;
    private List<Boolean> changeableMask;

    public SudokuPuzzle(Puzzle p) {
        char[] chars = p.data.toCharArray();
        digits = new ArrayList<>();
        changeableMask = new ArrayList<>();

        for (int i = 0; i<81; i++ ) {
            int val =  Character.getNumericValue(chars[i]);
            digits.add(new SudokuDigit(i/9,i%9, val));

            if (val == 0) {
                changeableMask.add(true);
            } else {
                changeableMask.add(false);
            }
        }
    }

    public SudokuDigit getDigit(int x, int y) {
        return digits.get(x*9 + y) ;
    }

    public void setDigit(int x, int y, int val) {
        digits.set(x*9 + y, new SudokuDigit(x,y, val));
    }
    public void setDigit(SudokuDigit s) {
        digits.set(s.x*9 + s.y, s);
    }

    public boolean isChangeable(SudokuDigit s) {
        return changeableMask.get(s.x*9 + s.y);
    }


}
