package me.eb.klabapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class SudokuTile  {

    private SudokuDigit digit;

    private Paint paintBrush = new Paint();
    private Bitmap bitmap;
    private Rect boundingRect;
    private boolean highlighted = false;

    public SudokuTile(SudokuDigit d, Rect r, Bitmap bmp) {
        this.digit = d;
        this.boundingRect = r;
        this.bitmap = bmp;
        paintBrush.setColor(Color.DKGRAY);
    }


    public void setDigit(SudokuDigit d) {
        digit = d;
    }

    public SudokuDigit getDigit() {
        return digit;
    }

    public void setColor(int color) {
        paintBrush.setColor(color);
    }

    public Rect getRect() { return boundingRect;  }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }

    public void draw(Canvas canvas) {

        int borderfatness = 8;
        paintBrush.setStrokeWidth(borderfatness);
        paintBrush.setStyle(Paint.Style.STROKE);


        canvas.drawBitmap(bitmap, null, boundingRect, paintBrush);


        canvas.drawRect(new Rect(
                boundingRect.left + borderfatness/2,
                boundingRect.top + borderfatness/2 ,
                boundingRect.right -borderfatness/2,
                boundingRect.bottom -borderfatness/2), paintBrush);
    }

    public void setHighlight(boolean b) {

        highlighted = b;
        if (highlighted) {
            setColor(Color.RED);
        } else {
            setColor(Color.DKGRAY);
        }
    }

}
