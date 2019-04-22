package me.eb.klabapp;

import java.util.ArrayList;
import java.util.List;

import me.eb.klabapp.roombase.Puzzle;


public class SudokuPuzzle {


    private SudokuDigit[][] digits ;
    private Boolean[][] changeableMask;


    public SudokuPuzzle(Puzzle p) {
        char[] chars = p.data.toCharArray();

        digits = new SudokuDigit[9][9];
        changeableMask = new Boolean[9][9];

        for (int i= 0; i< 9; i++) {
            for (int j=0; j<9; j++) {
                int val =  Character.getNumericValue(chars[i*9 +j]);
                digits[i][j] = new SudokuDigit(i,j, val);
                changeableMask[i][j] = val == 0;

            }
        }

    }

    public SudokuDigit getDigit(int x, int y) {
        return digits[x][y];
    }

    public void setDigit(int x, int y, int val) {
        digits[x][y] = new SudokuDigit(x,y, val);
    }
    public void setDigit(SudokuDigit s) {
        digits[s.x][s.y] = s;
    }

    public boolean isChangeable(SudokuDigit s) {
        return changeableMask[s.x][s.y];
    }

    public List<SudokuDigit> getConflictsOf(int x, int y, int val) {



        List<SudokuDigit> out = new ArrayList<>();

        if (val == 0) {
            return out;
        }
        if (val == digits[x][y].val) {
            return out;
        }

        for (int i=0; i<9; i++) {
            if ( val == digits[x][i].val ) {
                if (!out.contains(digits[x][i])) {
                    out.add(digits[x][i]);
                }
            }
        }
        for (int i=0; i<9; i++) {
            if ( val == digits[i][y].val ) {
                if (!out.contains(digits[i][y])) {
                    out.add(digits[i][y]);
                }

            }
        }


        for (int i = 0; i<3; i++) {
            for (int j=0; j<3;j++) {
                int xx = (x/3)*3 + i;
                int yy = (y/3)*3 + j;

                if (digits[xx][yy].val == val ) {
                    if (!out.contains(digits[xx][yy])) {
                        out.add(digits[xx][yy]);
                    }
                }
            }
        }

        return out;
    }

    /* Write the following:::   for each row for each i : get i on 9x+i
    *
    *                           for each column (for each i) in (get 9i+ y)
    *
    *                           for each i = x / 3
    *                            for each j = y/3
    *                             get (3*i
    * */
}
