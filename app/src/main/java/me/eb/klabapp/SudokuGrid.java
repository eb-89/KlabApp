package me.eb.klabapp;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;


public class SudokuGrid extends SurfaceView {

    List<SudokuTile> tiles;
    List<Bitmap> images;
    SudokuPuzzle currentPuzzle;

    private int tileWidth; // Set onSurfaceCreated in the holder.
    private int tileHeight;


    public SudokuGrid(Context context) {
        super(context);
        init();
    }


    public void updateGrid() {
        for (int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuDigit d = currentPuzzle.getDigit(i, j);
                tiles.get(i * 9 + j).setDigit(d);
                tiles.get(i * 9 + j).setBitmap(images.get(d.val));
            }
        }
        invalidate();
    }

    public void setInitSudoku(SudokuPuzzle p) {
        currentPuzzle = p;
    }

    public void setNewSudoku(SudokuPuzzle p) {
        currentPuzzle = p;
        tiles = createTiles(currentPuzzle);
        resetHighlight();
    }

    public SudokuDigit getSudokuDigit(int x, int y) {
        SudokuDigit out = null;
        for (SudokuTile s : tiles) {
            if (s.getRect().contains(x, y)) {

                out = s.getDigit();
            }
        }
        return out;
    }

    public void animateHighlight(SudokuDigit s) {

        final int x = s.x;
        final int y = s.y;
        ValueAnimator animation =  ValueAnimator.ofInt(0,100);
        animation.setDuration(200);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                tiles.get(9*x + y).setColor(Color.rgb(255,255-animatedValue,255) );
                invalidate();
            }
        });
        animation.start();

    }

    //blinks
    public void blink(List<SudokuDigit> list) {

        final List<SudokuDigit> ct = list;

        ValueAnimator animforward =  ValueAnimator.ofInt(0,255);
        animforward.setRepeatCount(1);
        animforward.setDuration(200);

        animforward.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                for (SudokuDigit s : ct) {
                    SudokuTile tile = tiles.get(s.x*9 + s.y);
                    int ci = tile.getColor();
                    int cr = Color.red(ci);
                    int cg = Color.green(ci);
                    int cb = Color.blue(ci);

                    tile.setColor(Color.argb(animatedValue, cr, cg,cb));
                }
                invalidate();
            }
        });

        AnimatorSet as = new AnimatorSet();
        as.play(animforward);
        as.start();
    }

    public void setHighlight(SudokuDigit s) {
        SudokuTile t = tiles.get(s.x*9 + s.y);
        t.setColor(Color.rgb(255,155,255));
    }

    //Currently not used at all
    public void setHighlight(List<SudokuDigit> list) {

        for (SudokuDigit s : list) {
            SudokuTile t = tiles.get(s.x*9 + s.y);
            t.setColor(Color.RED);

        }
    }

    public void resetHighlight() {
        for (SudokuTile t : tiles) {
            if (currentPuzzle.isChangeable(t.getDigit())) {
                t.setColor(Color.WHITE);
//                t.setHighlight(false);
            } else {
                t.setColor(Color.BLUE);
            }
        }
    }

    public SudokuPuzzle getSudoku() {
        return currentPuzzle;
    }

    private void init() {

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setWillNotDraw(false);

                setTileDimensions();
                images = loadImages();
                tiles = createTiles(currentPuzzle); //calls getHeight() and with() of this view
                resetHighlight();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });

    }

    private void setTileDimensions() {

        //Controls the spread of blocks
        int ZOOM = 8; //In pixels;

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

        int PADDING = 25; //In pixels;
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
                SudokuDigit d = puzzle.getDigit(i,j);
                SudokuTile s = new SudokuTile(d, rect, images.get(d.val));
                list.add(s);
            }
        }
        return list;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
