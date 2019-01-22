package me.eb.klabapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Erik on 2017-09-20.
 */

public class SudokuTile  {


    private Paint paintBrush = new Paint();
    private Bitmap bitmap;
    private Rect boundingRect;
    private boolean showBox;
    private boolean isTouchable;


    public SudokuTile(int x, int y, int w, int h, Bitmap bmp, boolean f) {
        this.boundingRect = new Rect(x,y,w,h);
        this.bitmap = bmp;
        this.isTouchable = f;
    }

    public int getColor() {
        return paintBrush.getColor();
    }
    public void setColor(int color) {
        paintBrush.setColor(color);
    }

    public Rect getRect() { return boundingRect; }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void draw(Canvas canvas) {
        paintBrush.setStrokeWidth(10);
        paintBrush.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bitmap, null, boundingRect, paintBrush);

        if (showBox) {
            canvas.drawRect(boundingRect, paintBrush);
        }
    }

    public void hideBoundingBox() {
        showBox = false;
    }

    public void showBoundingBox() {
        showBox = true;
    }

    public boolean isTouchable() { return isTouchable;}


}
