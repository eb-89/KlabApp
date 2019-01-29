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



    private Paint paintBrush = new Paint();
    private Bitmap bitmap;
    private Rect boundingRect;
    private boolean showBox;
    private boolean isClickable;
    private int value;


    public SudokuTile(int x, int y, int w, int h, Bitmap bmp, boolean isClickable) {
        this.boundingRect = new Rect(x,y,w,h);
        this.bitmap = bmp;
        this.isClickable = isClickable;

    }

    public int getColor() {
        return paintBrush.getColor();
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

    public boolean isClickable() { return isClickable;}


}
