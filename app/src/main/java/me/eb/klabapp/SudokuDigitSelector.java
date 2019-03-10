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
    private GestureDetector gestureDetector;
    private List<Bitmap> images;
    private SudokuTile selectedTile;
    private boolean isActive;

    List<SudokuTile> tiles;

    public SudokuDigitSelector(Context context) {
        super(context);
        init();
    }

    public SudokuDigit getSudokuDigit(int x, int y) {
        for (SudokuTile s : tiles) {
            if (s.getRect().contains(x,y)) {
                return new SudokuDigit(s.getValue());
            }
        }
        return null;
    }

    public void updateSelector() {
        invalidate();
    }

    public SudokuTile getSelectedTile() {
        return selectedTile;
    }

    public boolean isActive() {
        return isActive;
    }

    private void init() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                images = loadImages();
                setWillNotDraw(false);
                tiles = createTiles(); //calls getHeight() and with() of this view
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapUp(MotionEvent e) {

                int x = (int)e.getX();
                int y = (int)e.getY();

                isActive = false;
                for (SudokuTile s : tiles) {
                    s.hideBoundingBox();
                    if (s.getRect().contains(x,y)) {
                            s.showBoundingBox();
                            isActive = true;
                            selectedTile = s;
                        }
                    }

                invalidate();
                return true;
            }

            public void onLongPress(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();
                invalidate();
            }
        });
    }

    public void setSelected(int val) {

        for (SudokuTile t : tiles) {
            t.hideBoundingBox();
        }

        tiles.get(val).showBoundingBox();
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

            int PADDING = 10;

            int xCoord = i*this.getWidth()/10;
            int yCoord = 0;

            Rect rect = new Rect(xCoord, yCoord, xCoord+tileWidth, yCoord + tileHeight);
//            xCoord += PADDING/2;
//            yCoord += PADDING/2;
            SudokuTile s = new SudokuTile(rect, i, images.get(i), false);
            s.setColor(Color.RED);
//            s.showBoundingBox();
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
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawSelector(canvas);

    }

}
