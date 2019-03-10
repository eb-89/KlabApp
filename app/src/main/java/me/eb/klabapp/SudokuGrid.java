package me.eb.klabapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//TODO:Split this into two classes.

public class SudokuGrid extends SurfaceView {

    //private Paint paint = new Paint();
    private SurfaceHolder surfaceHolder;
    private GestureDetector gestureDetector;

    List<SudokuTile> tiles;
    List<Bitmap> images;
    SudokuPuzzle currentPuzzle;
    SudokuDigitSelector digitSelector;

    private int tileWidth; // Set onSurfaceCreated in the holder.
    private int tileHeight;

    //Controls the spread of blocks
    private int ZOOM = 8; //In pixels;
    private int PADDING = 25; //In pixels;


    //Wanted by tools;
    public SudokuGrid(Context context) {
        super(context);
    }

    public SudokuGrid(Context context, SudokuDigitSelector s) {
        super(context);
        this.currentPuzzle = new SudokuPuzzle();
        this.digitSelector = s;
        init();
    }
    public SudokuGrid(Context context, SudokuDigitSelector s, SudokuPuzzle p) {
        super(context);
        this.currentPuzzle = p;
        this.digitSelector = s;
        init();
    }

    public void updateGrid() {
        Log.i("Grid:","  Invalidating...");
        invalidate();
    }

    public void setSudoku(SudokuPuzzle p) {
        currentPuzzle = p;
        tiles = createTiles(p);
        invalidate();
    }

    public SudokuDigit getSudokuDigit(int x, int y) {
        SudokuDigit out = null;
        for (SudokuTile s : tiles) {
            if (s.getRect().contains(x, y)) {
//                SudokuTile ss = digitSelector.getSelectedTile();
//                //ss.getValue();
//                s.setColor(Color.BLUE);
//                s.setBitmap(ss.getBitmap());
                out = new SudokuDigit(s.getBoardX(), s.getBoardY(),s.getValue());
            }
        }
        return out;
    }

    public SudokuPuzzle getSudoku() {
        return currentPuzzle;
    }

    private void init() {

        //sController = new SudokuController(sudokuGrid, currentPuzzle);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setWillNotDraw(false);

                setTileDimensions();
                images = loadImages();
                tiles = createTiles(currentPuzzle); //calls getHeight() and with() of this view

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapUp(MotionEvent e) {

                int x = (int)e.getX();
                int y = (int)e.getY();
                Log.i("Grid says:  ", "x: " + x + "  y: " + y);

                if (digitSelector.isActive()) {
                    for (SudokuTile s : tiles) {
                        if (s.getRect().contains(x, y) && s.isClickable() ) {
                            SudokuTile ss = digitSelector.getSelectedTile();
                            //ss.getValue();
                            s.setColor(Color.BLUE);
                            s.setBitmap(ss.getBitmap());


                            currentPuzzle.setDigit(s.getBoardX(), s.getBoardY(), ss.getValue());

                        }
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

    private void setTileDimensions() {
        tileWidth = this.getWidth()/ 9 - ZOOM;
        tileHeight = this.getHeight()/ 9 - ZOOM;
    }

    private List<Bitmap> loadImages() {
        List<Bitmap> list = new ArrayList<>();

        list.add(Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.ARGB_8888));
        for (int i = 1; i <=9 ; i++) {
            int resId = KlabApp.getContext().getResources().getIdentifier("number_" + i, "drawable", "me.eb.klabapp");
            Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
            b = Bitmap.createScaledBitmap(b, tileWidth, tileHeight, false);
            list.add(b);
        }
        return list;
    }

    private List<SudokuTile> createTiles(SudokuPuzzle puzzle) {

        List<SudokuTile> list = new ArrayList<>();

        for (int i=0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                int xPos = 0;
                int yPos = 0;

                switch (j/3) {
                    case 0: xPos = 0;break;
                    case 1: xPos = this.getWidth()/2 - tileWidth - tileWidth/2; break;
                    case 2: xPos = this.getWidth() - tileWidth*3; break;
                }

                switch (i/3) {
                    case 0: yPos = 0;break;
                    case 1: yPos = this.getHeight()/2 - tileHeight - tileHeight/2; break;
                    case 2: yPos = this.getHeight() - tileHeight*3; break;
                }

                int xCoord = xPos + (j%3)*(tileWidth);
                int yCoord = yPos + (i%3)*(tileHeight);
                xCoord += PADDING/2;
                yCoord += PADDING/2;


                Rect rect = new Rect(
                        xCoord,
                        yCoord,
                        xCoord+tileWidth - PADDING,
                        yCoord + tileHeight - PADDING);

                //If the digit is 0, the tile is open
                int d = puzzle.getDigit(i,j);
                SudokuTile s;
                if (d == 0) {
                    s = new SudokuTile(rect, d, images.get(d), true);
                    s.setColor(Color.DKGRAY);
                    s.showBoundingBox();
                    s.setBoardXY(i,j);
                } else {
                    s = new SudokuTile(rect, d, images.get(d), false);
                    s.setColor(Color.YELLOW);
                    s.showBoundingBox();
                    s.setBoardXY(i,j);
                }
                list.add(s);
            }
        }
        return list;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
        Log.i("Grid says:  ", "Saw  at " + event.getX() + " and " + event.getY() +  " event but didnt consume");
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawGrid(Canvas canvas) {
                Log.i("Grid:","  Invalidating...");

                for (SudokuTile s : tiles) {
                    int td = currentPuzzle.getDigit(s.getBoardX(),s.getBoardY());
                    //Ths is working
                    s.setBitmap(images.get(td));
                    s.draw(canvas);
                }
                Log.i("Grid: ", "Digits: " + currentPuzzle.getDigit(0,0) + " " +currentPuzzle.getDigit(1,0) + " " + currentPuzzle.getDigit(2,0));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawGrid(canvas);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
