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

    //Controls the spread of blox
    private int ZOOM = 8; //In pixels;

    //Garbage
    Bitmap[] tileGraphics = new Bitmap[9];
    Bitmap[][] sudokuImages = new Bitmap[9][9];
    int [][][] positionMtx = new int[9][9][2];
    int [][] sudokuData = new int[9][9];
    int [][] fixedTilesMask = new int[9][9];
    private boolean editable = false;
    private boolean putPermanentTiles = false;

    int [][] testData1 = {
            {1,2,4,3,4,6,7,8,9},
            {1,2,5,3,4,9,7,8,8},
            {1,2,1,3,4,1,0,8,9},
            {1,0,2,3,4,7,7,6,9},
            {5,2,1,3,4,7,7,8,9},
            {1,2,1,3,4,2,0,8,8},
            {6,2,1,0,4,0,7,0,9},
            {1,2,0,3,4,5,7,8,9},
            {1,2,1,3,4,4,7,8,9}};

    int [][] testMask1 = {
            {1,0,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,0,0,0},
            {1,0,0,1,0,0,0,0,0},
            {1,0,0,0,0,0,0,1,0},
            {1,0,0,0,0,0,0,0,0},
            {1,1,0,0,0,1,0,0,0},
            {1,0,0,0,0,0,0,0,0},
            {1,0,0,0,1,0,0,0,0},
            {1,0,0,0,0,0,0,0,0}};

    int [][] testData2 = {
            {5,3,7,2,8,0,0,0,0},
            {0,0,0,0,3,9,7,4,2},
            {2,0,4,0,0,1,5,3,0},
            {0,1,5,0,7,0,8,0,9},
            {3,8,0,9,0,0,0,7,1},
            {0,0,9,1,4,8,3,0,0},
            {0,0,0,4,1,0,9,8,3},
            {9,4,3,8,0,2,0,0,0},
            {1,0,8,0,0,6,0,5,4}};

    int [][] testMask2 = {
            {1,1,1,1,1,0,0,0,0},
            {0,0,0,0,1,1,1,1,1},
            {1,0,1,0,0,1,1,1,0},
            {0,1,1,0,1,0,1,0,1},
            {1,1,0,1,0,0,0,1,1},
            {0,0,1,1,1,1,1,0,0},
            {0,0,0,1,1,0,1,1,1},
            {1,1,1,1,0,1,0,0,0},
            {1,0,1,0,0,1,0,1,1}};


    public SudokuGrid(Context context, SudokuDigitSelector s) {
        super(context);
        digitSelector = s;
        init();

    }

    public void resetSudoku() {
        tiles = createTiles(currentPuzzle);
        invalidate();
    }

    //probably garbage

    public void writeToBinaryFile(String name) {

        String filename = name;
        byte[] databytes = new byte[81];
        byte[] maskbytes = new byte[81];

        try {
            FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE);

            for (int i = 0; i < sudokuData.length; i++) {
                for (int j = 0; j < sudokuData.length; j++) {
                    databytes[(9*i + j)] = (byte) sudokuData[i][j];
                }
            }

            for (int i = 0; i < sudokuData.length; i++) {
                for (int j = 0; j < sudokuData.length; j++) {
                    maskbytes[(9*i + j)] = (byte) fixedTilesMask[i][j];
                }
            }

            fos.write(databytes);
            fos.write(maskbytes);
            fos.close();

        } catch (Exception e) {}

        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
    }
    public void readFromBinaryFile(String name) {

        String filename = name;
        byte[] bytes = new byte[81];

        File dir = getContext().getFilesDir();
        File[] files = dir.listFiles();

        try {
            FileInputStream fis = getContext().openFileInput(filename);

            for (int i = 0; i < sudokuData.length; i++) {
                for (int j = 0; j < sudokuData.length; j++) {
                    sudokuData[i][j] = fis.read();

                }
            }

            for (int i = 0; i < sudokuData.length; i++) {
                for (int j = 0; j < sudokuData.length; j++) {
                    fixedTilesMask[i][j] = fis.read();

                }
            }

            fis.close();

        } catch (Exception e) {}

        //initParams();
        //getTiles();
        //setPositionMtx();
        //showConflictingTiles();
        invalidate();
    }



    //Garbage
    private ArrayList<SudokuTile> getConflictingTiles(int i, int j) {

        int number = sudokuData[i][j];
        ArrayList<SudokuTile> l = new ArrayList<SudokuTile>();
        //l.add(tiles[i][j]);

        for (int k = 0; k < sudokuData.length; k++) {
            if (sudokuData[i][k] == number && k != j) {
                //tiles[i][k].setColor(Color.BLACK);
                //l.add(tiles[i][k]);
            }
        }

        for (int k = 0; k < sudokuData.length; k++) {
            if (sudokuData[k][j] == number && k != i) {
                //tiles[k][j].setColor(Color.BLACK);
                //l.add(tiles[k][j]);
            }
        }

        for (int h = 0 ; h < 3 ; h++  ) {
            for (int p = 0; p < 3; p++  ) {

                int a = (i/3)*3 + h;
                int b = (j/3)*3 + p;

                //if (tiles[a][b] != null && sudokuData[a][b] == number ) {
                //    l.add(tiles[a][b]);
                //}
            }
        }


        return l;


    }



    //Working

    private void setTileDimensions() {
        tileWidth = this.getWidth()/ 9 - ZOOM;
        tileHeight = this.getHeight()/ 9 - ZOOM;
    }

    private void init() {

        currentPuzzle = new SudokuPuzzle();
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

                if (digitSelector.isActive()) {
                    for (SudokuTile s : tiles) {
                        if (s.getRect().contains(x, y) && s.isTouchable() ) {
                            SudokuTile ss = digitSelector.getSelectedTile();
                            s.setBitmap(ss.getBitmap());
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

    private List<Bitmap> loadImages() {
        List<Bitmap> list = new ArrayList<>();

        list.add(Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.ARGB_8888));
        for (int i = 1; i <=9 ; i++) {
            int resId = getResources().getIdentifier("number_" + i, "drawable", "me.eb.klabapp");
            Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
            b = Bitmap.createScaledBitmap(b, tileWidth, tileHeight, false);
            list.add(b);
        }
        return list;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private List<SudokuTile> createTiles(SudokuPuzzle puzzle) {

        //tileWidth = this.getWidth()/ 9;
        //tileHeight = this.getHeight()/ 9;
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

                int d = puzzle.getDigit(i,j);
                SudokuTile s;
                /*
                    If the digit is 0, the tile is open
                 */
                if (d == 0) {
                    s = new SudokuTile(xCoord, yCoord, xCoord + tileWidth, yCoord + tileHeight, images.get(d), true);

                } else {
                    s = new SudokuTile(xCoord, yCoord, xCoord + tileWidth, yCoord + tileHeight, images.get(d), false);
                    s.setColor(Color.DKGRAY);
                    s.showBoundingBox(true);
                }
                list.add(s);
            }
        }
        return list;
    }

    private void drawGrid(Canvas canvas) {
                for (SudokuTile s : tiles) {
                    s.draw(canvas);
                }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawGrid(canvas);

    }
}
