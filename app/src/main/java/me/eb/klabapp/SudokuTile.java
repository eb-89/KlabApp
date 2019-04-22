package me.eb.klabapp;

import android.animation.ValueAnimator;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;


public class SudokuTile extends AnimationDrawable {

    ValueAnimator animation;

    private SudokuDigit digit;

    private Paint paintBrush = new Paint();
    private Bitmap bitmap;
    private Rect boundingRect;
    private boolean highlighted = false;
    private int borderfatness = 8;

    public SudokuTile(SudokuDigit d, Rect r, Bitmap bmp) {
        this.digit = d;
        this.boundingRect = r;
        this.bitmap = bmp;
        paintBrush.setColor(Color.DKGRAY);

    }

    public void startAnimation() {
        animation.start();
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

    public int getColor() {
        return paintBrush.getColor();
    }

    public Rect getRect() { return boundingRect;  }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }


    @Override
    public void draw(Canvas canvas) {

        paintBrush.setStrokeWidth(borderfatness);
        paintBrush.setStyle(Paint.Style.STROKE);

        canvas.drawBitmap(bitmap, null, boundingRect, paintBrush);


        canvas.drawRect(new Rect(
                boundingRect.left + borderfatness/2,
                boundingRect.top + borderfatness/2 ,
                boundingRect.right -borderfatness/2,
                boundingRect.bottom -borderfatness/2), paintBrush);

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
    @Override
    public void start() {
        animation.start();
        invalidateSelf();
    }
    @Override
    public void stop() {
        animation.start();
        invalidateSelf();
    }
    @Override
    public boolean isRunning() {
        return true;
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
