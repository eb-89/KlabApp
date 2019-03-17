package me.eb.klabapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2019-01-03.
 */

public class SudokuDigitSelector extends SurfaceView {

    private int tileWidth;
    private int tileHeight;
    private SurfaceHolder surfaceHolder;
    private List<Bitmap> images;

    List<SudokuTile> tiles;

    public SudokuDigitSelector(Context context) {
        super(context);
        init();
    }

    public void setHighlight(SudokuDigit s, boolean b) {

        resetHighlight();

        SudokuTile t = tiles.get(s.val);
        t.setHighlight(b);
    }

    public void resetHighlight() {
        for (SudokuTile t : tiles) {
            t.setHighlight(false);
        }
    }

    public SudokuDigit getSudokuDigit(int x, int y) {
        SudokuDigit out = null;
        for (SudokuTile s : tiles) {
            if (s.getRect().contains(x,y)) {
                out = s.getDigit();
            }
        }
        return out;
    }

    public void updateSelector() {
        invalidate();
    }


    private void init() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setWillNotDraw(false);

                images = loadImages();
                tiles = createTiles(); //calls getHeight() and with() of this view
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

    }


    private List<Bitmap> loadImages() {
        List<Bitmap> list = new ArrayList<>();
        tileWidth = getWidth()/10;
        tileHeight = getHeight();

        list.add(Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.ARGB_8888));
        for (int i = 1; i <=9 ; i++) {
            int resId = getResources().getIdentifier("number_" + i, "drawable", "me.eb.klabapp");
            Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
            b = Bitmap.createScaledBitmap(b, tileWidth, tileHeight, false);
            list.add(b);
        }
        return list;
    }

    public List<SudokuTile> createTiles() {
        List<SudokuTile> list = new ArrayList<>();

        for (int i = 0; i<10; i++) {

            int xCoord = i*this.getWidth()/10;
            int yCoord = 0;

            Rect rect = new Rect(xCoord, yCoord, xCoord+tileWidth, yCoord + tileHeight);
            SudokuTile s = new SudokuTile(new SudokuDigit(i), rect, images.get(i));
            list.add(s);

        }
        return list;
    }

    public void drawSelector(Canvas canvas) {
        for (SudokuTile s : tiles) {
            s.draw(canvas);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawSelector(canvas);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
