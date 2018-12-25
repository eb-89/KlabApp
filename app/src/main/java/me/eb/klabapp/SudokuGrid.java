package me.eb.klabapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
import java.util.ArrayList;

//TODO:Split this into two classes.

public class SudokuGrid extends SurfaceView {

    Paint paint = new Paint();
    SudokuTile[][] tiles = new SudokuTile[9][9];
    private SurfaceHolder holder;

    Bitmap[] tileGraphics = new Bitmap[9];
    Bitmap[][] sudokuImages = new Bitmap[9][9];
    int [][][] positionMtx = new int[9][9][2];
    int [][] sudokuData = new int[9][9];
    int [][] fixedTilesMask = new int[9][9];
    private boolean editable = false;
    private boolean putPermanentTiles = false;
    private int tileWidth;
    private int tileHeight;

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



    //int tileWidth;
    int smallPad;
    int largePad;
    GestureDetector gesDec;

    public SudokuGrid(Context context) {
        super(context);
        init();

    }

    public void setPutPermanentTiles(boolean b) {
        putPermanentTiles = b;
    }
    public void setEditable(boolean b) {
        editable = b;
    }



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
        getTiles();
        //setPositionMtx();
        showConflictingTiles();
        invalidate();
    }

    //OLD
    private void readFromFile( int resID) {
        try {
            //int resId = getResources().getIdentifier("sudoku", "raw", "me.eb.klabapp");
            InputStream is = getContext().getResources().openRawResource(resID);
            Reader r = new InputStreamReader(is, "US-ASCII");
            BufferedReader br = new BufferedReader(r);

            for (int i= 0; i<sudokuData.length; i++) {
                for (int j= 0; j<sudokuData.length; j++) {
                    char a = (char) br.read();
                    int number = Character.getNumericValue(a);

                        if (0 <= number && number <10)
                            sudokuData[i][j] = number;
                        else
                            sudokuData[i][j] = 0;
                }
            }

            br.skip(2);

            for (int i= 0; i<sudokuData.length; i++) {
                for (int j= 0; j<sudokuData.length; j++) {
                    char a = (char) br.read();
                    int number = Character.getNumericValue(a);

                        fixedTilesMask[i][j] = number;

                }
            }

            is.close();
            r.close();
            br.close();
            //return new String(buffer);
            //invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSudoku() {
        for (int i = 0; i<sudokuData.length; i++) {
            for (int j = 0; j<sudokuData.length; j++) {
                if (!editable) {
                    if (fixedTilesMask[i][j] == 0) {
                        sudokuData[i][j] = 0;
                    } else {
                        tiles[i][j].setColor(Color.BLACK);
                    }
                } else {
                    sudokuData[i][j] = 0;
                    fixedTilesMask[i][j] = 0;
                }
            }
        }
        showConflictingTiles();
        invalidate();
    }

    private void initParams() {
        SudokuTile blankTile =  new SudokuTile(this, Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888));
        tileWidth = blankTile.getWidth();
        tileHeight = blankTile.getHeight();
    }


    private SudokuTile getNewTileFromData(int i, int j) {
        if (sudokuData[i][j] != 0) {
            int resId = getResources().getIdentifier("number_" + (sudokuData[i][j]), "drawable", "me.eb.klabapp");

            SudokuTile sd = new SudokuTile(this, BitmapFactory.decodeResource(getResources(), resId));

            if (fixedTilesMask[i][j] == 0)
                sd.setPermanence(false);
            return sd;

        }
        else return null;

    }

    public void getTiles() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j<tiles.length; j++) {
                tiles[i][j] = getNewTileFromData(i,j);
            }
        }
    }

    private void setPositionMtx() {

        //int tileWidth = tiles[0][0].getWidth();
        //int tileHeight = tiles[0][0].getHeight();

        for (int i=0; i<positionMtx.length; i++) {
            for (int j = 0; j< positionMtx.length; j++) {
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


                positionMtx[i][j][0] = xPos + (j%3)*(tileWidth);
                positionMtx[i][j][1] = yPos + (i%3)*(tileHeight);
            }
        }
    }

    private void showConflictingTiles() {

        ArrayList<SudokuTile> list = new ArrayList<>();

        for (int i=0; i<sudokuData.length; i++) {
            for (int j = 0; j < sudokuData.length; j++) {

                if (sudokuData[i][j] != 0) {
                    list = getConflictingTiles(i, j);
                }

                if (list.size() > 1) {

                    for (SudokuTile s : list) {
                        if (s.isPermanent()) {
                            s.setColor(Color.RED);
                        } else {
                            s.setColor(Color.MAGENTA);
                        }

                    }
                }
            }
        }

    }


    private ArrayList<SudokuTile> getConflictingTiles(int i, int j) {

        int number = sudokuData[i][j];
        ArrayList<SudokuTile> l = new ArrayList<SudokuTile>();
        //l.add(tiles[i][j]);

        for (int k = 0; k < sudokuData.length; k++) {
            if (sudokuData[i][k] == number && k != j) {
                //tiles[i][k].setColor(Color.BLACK);
                l.add(tiles[i][k]);
            }
        }

        for (int k = 0; k < sudokuData.length; k++) {
            if (sudokuData[k][j] == number && k != i) {
                //tiles[k][j].setColor(Color.BLACK);
                l.add(tiles[k][j]);
            }
        }

        for (int h = 0 ; h < 3 ; h++  ) {
            for (int p = 0; p < 3; p++  ) {

                int a = (i/3)*3 + h;
                int b = (j/3)*3 + p;

                if (tiles[a][b] != null && sudokuData[a][b] == number ) {
                    l.add(tiles[a][b]);
                }
            }
        }


        return l;


    }


    private void init() {

        //sudokuData = testData2;
        //fixedTilesMask = testMask2;
        //readFromFile(R.raw.sudoku3);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                setWillNotDraw(false);

                initParams();
                getTiles();
                setPositionMtx();
                showConflictingTiles();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });

        gesDec = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            //TODO: there must be a faster way to do this...
            public boolean onSingleTapUp(MotionEvent e) {

                int x = (int)e.getX();
                int y = (int)e.getY();

                for (int i=0; i<positionMtx.length; i++) {
                    for (int j = 0; j< positionMtx.length; j++) {


                        if (positionMtx[i][j][0] < x && x < positionMtx[i][j][0]+tileWidth && positionMtx[i][j][1] < y && y < positionMtx[i][j][1]+tileHeight) {

                            if (!editable) {
                                if (fixedTilesMask[i][j] == 0) {
                                    sudokuData[i][j] = (sudokuData[i][j] + 1) % 10;
                                    if (sudokuData[i][j] == 0) {
                                        sudokuData[i][j]++;
                                    }
                                    tiles[i][j] = getNewTileFromData(i, j);

                                }
                            } else {

                                sudokuData[i][j] = (sudokuData[i][j] + 1) % 10;
                                if (sudokuData[i][j] == 0) {
                                    sudokuData[i][j]++;
                                }
                                tiles[i][j] = getNewTileFromData(i, j);

                                if (putPermanentTiles && tiles[i][j] != null) {
                                    fixedTilesMask[i][j] = 1;
                                    tiles[i][j].setPermanence(true);
                                }

                            }

                        }


                        if (tiles[i][j] != null) {
                            if (fixedTilesMask[i][j] == 0) {
                                tiles[i][j].setColor(Color.GREEN);
                            } else {
                                tiles[i][j].setColor(Color.BLACK);
                            }

                        }

                    }
                }

                showConflictingTiles();
                invalidate();

                return true;
            }



            public void onLongPress(MotionEvent e) {

                int x = (int)e.getX();
                int y = (int)e.getY();

                //int tileWidth = tiles[0][0].getWidth();
                //int tileHeight = tiles[0][0].getHeight();

                //search:
                for (int i=0; i<positionMtx.length; i++) {
                    for (int j = 0; j< positionMtx.length; j++) {

                        if (positionMtx[i][j][0] < x && x < positionMtx[i][j][0]+tileWidth && positionMtx[i][j][1] < y && y < positionMtx[i][j][1]+tileHeight) {

                            if (fixedTilesMask[i][j] == 0 || editable) {
                                fixedTilesMask[i][j] = 0;
                                sudokuData[i][j] = 0;
                                tiles[i][j] = null;
                                //
                            }
                        }

                        if (tiles[i][j] != null) {
                            if (fixedTilesMask[i][j] == 0) {
                                tiles[i][j].setColor(Color.GREEN);
                            } else {
                                tiles[i][j].setColor(Color.BLACK);
                            }

                        }


                    }
                }
                /*
                if (coordX > -1 && coordY > -1) {
                    sudokuData[coordX][coordY] = (sudokuData[coordX][coordY] +1) % 9;
                    sudokuImages[coordX][coordY] = tileGraphics[sudokuData[coordX][coordY]];
                }
                */
                //setTileWidth();
                showConflictingTiles();
                invalidate();

            }

        });

        /*
        for (int i = 0; i<tileGraphics.length; i++) {
            int resId = getResources().getIdentifier("number_" + (i+1), "drawable", "me.eb.klabapp");
            tileGraphics[i] = BitmapFactory.decodeResource(getResources(), resId);
        }

        for (int i=0; i<sudokuImages.length; i++) {
            for (int j = 0; j< sudokuImages.length; j++) {
                if (sudokuData[i][j] != 9)
                sudokuImages[i][j] = tileGraphics[sudokuData[i][j]];
            }
        }
        */
    }

    //OLD
    private void setTileWidth() {

        for (int i=0; i<sudokuImages.length; i++) {
            for (int j = 0; j< sudokuImages.length; j++) {
                //if (sudokuImages[i][j] != null)
                //sudokuImages[i][j] = Bitmap.createScaledBitmap(sudokuImages[i][j], tileWidth, tileWidth, false);
            }
        }
    }

    /*
    private void setPositionMtx() {
        for (int i=0; i<positionMtx.length; i++) {
            for (int j = 0; j< positionMtx.length; j++) {
                positionMtx[i][j][0] = i*(tileWidth+smallPad);
                positionMtx[i][j][1] = j*(tileWidth+smallPad);

                if (i>2)
                    positionMtx[i][j][0] += largePad;
                if (i>5)
                    positionMtx[i][j][0] += largePad;
                if (j>2)
                    positionMtx[i][j][1] += smallPad;
                if (j>5)
                    positionMtx[i][j][1] += smallPad;
            }
        }

    }
    */

    /*
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //canvas.drawColor(Color.BLACK);
        bmp = Bitmap.createScaledBitmap(bmp, this.getWidth(), this.getHeight(), true);
        canvas.drawBitmap(bmp, 0, 0, paint);
        canvas.drawBitmap(bmp, CanvasWidth, 0, paint);
    }
    */

    //OLD
    private int[] getClickedImage(int x, int y) {
        int[] ret = new int[2];
        ret[0] = -1;
        ret[1] = -1;

        for (int i=0; i<positionMtx.length; i++) {
            for (int j = 0; j< positionMtx.length; j++) {
                /*
                if (positionMtx[i][j][0] < x && x < positionMtx[i][j][0] + tileWidth &&
                        positionMtx[i][j][1] < y && y < positionMtx[i][j][1] + tileWidth) {
                    ret[0] = i;
                    ret[1] = j;
                }
                */
            }
        }
        return ret;
    }

    //OLD
    public void getDataFromText(int resID) {
        readFromFile(resID);
    }

    //OLD
    public void getNewBoardFromText(int resID) {
        readFromFile(resID);
        getTiles();
        resetSudoku();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gesDec.onTouchEvent(event);

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int squareDim = widthSize - 100;

        //tileWidth = squareDim / 14;
        //smallPad = squareDim / 28;
        //largePad = squareDim / 14;
        //setTileWidth();
        //setPositionMtx();

        setMeasuredDimension(squareDim, squareDim);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //int x = getWidth();
        //int y = getHeight();

        //paint.setColor(Color.GREEN);

        //canvas.drawRect(0, 0, x, y, paint);
        //Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.);
        //canvas.drawBitmap(bMap, 0, 100, paint);
        //bMap = (Bitmap.createScaledBitmap(bMap, 100, 100, false));
        //canvas.drawRect(0,0,100,100,paint);


        for (int i = 0; i<sudokuData.length; i++) {
            for (int j = 0; j < sudokuData.length; j++) {
                canvas.save();
                canvas.translate(positionMtx[i][j][0], positionMtx[i][j][1]);

                if (sudokuData[i][j] != 0) {
                        tiles[i][j].onDraw(canvas);
                }

                canvas.restore();

            }
        }

        for (int i = 0; i<sudokuData.length; i++) {

            canvas.save();
            canvas.translate(positionMtx[0][i][0], positionMtx[0][i][1]);

            paint.setStrokeWidth(1);
            paint.setColor(Color.YELLOW);


            if (i%3 == 0)
                paint.setStrokeWidth(5);

            canvas.drawLine(0,0,0,this.getHeight(),paint);

            if (i%3 == 2) {
                canvas.translate(tileWidth,0);
                paint.setStrokeWidth(5);
                canvas.drawLine(0,0,0,this.getHeight(),paint);
            }

            canvas.restore();
        }

        for (int i = 0; i<sudokuData.length; i++) {

            //Paint paint = new Paint();

            canvas.save();
            canvas.translate(positionMtx[i][0][0], positionMtx[i][0][1]);

            paint.setStrokeWidth(1);
            paint.setColor(Color.YELLOW);



            if (i%3 == 0)
                paint.setStrokeWidth(5);

            canvas.drawLine(0,0,this.getWidth(),0,paint);

            if (i%3 == 2) {
                canvas.translate(0, tileHeight);
                paint.setStrokeWidth(5);
                canvas.drawLine(0,0,this.getWidth(),0,paint);
            }

            canvas.restore();
        }

        //canvas.drawRect(0,0,100,100,paint);
    }
}
