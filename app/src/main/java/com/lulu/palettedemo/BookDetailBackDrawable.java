package com.lulu.palettedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * @author zhanglulu on 2019/8/30.
 * for 书籍详情页背景 Drawable
 */
public class BookDetailBackDrawable extends Drawable {
    private static final String TAG = "BookDetailBackDrawable";
    private int alpha = 0xFF;
    private int color;
    private Paint mPaint;

    public BookDetailBackDrawable() {
        mPaint = new Paint();
    }

    public void setColor(int color) {
        this.color = color;
    }



    protected int getTranslucentColor(float percent, int rgb) {
        // 10101011110001111
        int blue = Color.blue(rgb);
        int green = Color.green(rgb);
        int red = Color.red(rgb);
        int alpha = Color.alpha(rgb);
//      int blue = rgb & 0xff;
//      int green = rgb>>8 & 0xff;
//      int red = rgb>>16 & 0xff;
//      int alpha = rgb>>>24;

        alpha = Math.round(alpha*percent);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
//        LinearGradient linearGradient = new LinearGradient(0, 0, getIntrinsicWidth(), getIntrinsicHeight(), new int[]{getTranslucentColor(0, color), getTranslucentColor(1, color)}, null, LinearGradient.TileMode.CLAMP);
//        mPaint.setShader(linearGradient);
        mPaint.setColor(Color.YELLOW);
        Log.d(TAG, "draw: getIntrinsicWidth(): " + getBounds().right);
        Log.d(TAG, "draw: getIntrinsicHeight(): " + getBounds().bottom);
        canvas.drawRect(0, 0, getBounds().right, getBounds().bottom,mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from=0,to=255) int alpha) {
        this.alpha = alpha;
    }

    @Override
    @IntRange(from=0,to=255)
    public int getAlpha() {
        return alpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        }
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        }
        return PixelFormat.TRANSLUCENT;
    }
}
