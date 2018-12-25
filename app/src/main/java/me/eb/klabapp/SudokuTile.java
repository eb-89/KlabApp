package me.eb.klabapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Erik on 2017-09-20.
 */

public class SudokuTile  {

    private Paint paint = new Paint();
    private Bitmap bMap;
    private boolean isPermanent = true;

    public boolean isPermanent() {
        return isPermanent;
    }

    public SudokuTile(View view, Bitmap bmp) {

        bMap = Bitmap.createScaledBitmap(bmp, (view.getWidth()/9)- 20 , (view.getHeight()/9) - 20 ,false);
        init();
    }

    public void setPermanence(boolean b) {
        isPermanent = b;
        if (isPermanent)
            paint.setColor(Color.BLACK);
        else
            paint.setColor(Color.GREEN);

    }

    private void init() {
        if (isPermanent)
            paint.setColor(Color.BLACK);
        else
            paint.setColor(Color.GREEN);
        //setOnClickListener(this);

    }
    public int getHeight() {
        return bMap.getHeight() + 20;
    }

    public int getWidth() { return bMap.getWidth() + 20; }

    public void setColor(int i) {
        paint.setColor(i);
    }

    public int getColor() {
        return paint.getColor();
    }


    public void onDraw(Canvas canvas) {

        //super.onDraw(canvas);

        //paint = new Paint();

        canvas.drawRect(0,0,bMap.getWidth()+20,bMap.getHeight()+20, paint);
        canvas.drawBitmap(bMap, 10, 10, paint);

        //canvas.drawCircle(50,50,50,paint);
        //canvas.drawText("test1", 30,30, paint);




    }


}
