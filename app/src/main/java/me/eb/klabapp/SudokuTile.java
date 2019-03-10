package me.eb.klabapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Erik on 2017-09-20.
 */

public class SudokuTile  {

    private int boardX = 0;
    private int boardY = 0;

    private Paint paintBrush = new Paint();
    private Bitmap bitmap;
    private Rect boundingRect;
    private boolean showBox;
    private boolean isClickable;
    private int value;
    private int borderfatness;


    public SudokuTile(Rect r, int value, Bitmap bmp, boolean isClickable) {
        this.boundingRect = r;
        this.bitmap = bmp;
        this.isClickable = isClickable;
        this.value = value;
    }

    public void setValue(int val) {
        value = val;
    }

    public void setBoardXY(int x, int y) {
        boardX = x;
        boardY = y;
    }

    public int getValue() {
        return value;
    }
    public void setColor(int color) {
        paintBrush.setColor(color);
    }

    public Rect getRect() { return boundingRect;  }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void draw(Canvas canvas) {

        borderfatness = 8;
        paintBrush.setStrokeWidth(borderfatness);
        paintBrush.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bitmap, null, boundingRect, paintBrush);

        if (showBox) {
            canvas.drawRect(new Rect(
                    boundingRect.left + borderfatness/2,
                    boundingRect.top + borderfatness/2 ,
                    boundingRect.right -borderfatness/2,
                    boundingRect.bottom -borderfatness/2), paintBrush);
        }
    }

    public void hideBoundingBox() {
        showBox = false;
    }

    public void showBoundingBox() {
        showBox = true;
    }

    public boolean isClickable() { return isClickable;}


    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }
}
