package com.zplay.playable.vastdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;

import java.io.IOException;
import java.io.InputStream;

public class ResFactory {

    public static Drawable getDrawableByAssets(String name, Context ctx) {
        try {
            InputStream is = ctx.getAssets().open(name + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            byte[] nine = bitmap.getNinePatchChunk();
            boolean isNinePatchChunk = NinePatch.isNinePatchChunk(nine);
            if (isNinePatchChunk) {
                NinePatchDrawable drawable = new NinePatchDrawable(ctx.getResources(), bitmap, nine, new Rect(), null);
                return drawable;
            } else {
                BitmapDrawable drawable = new BitmapDrawable(ctx.getResources(), bitmap);
                return drawable;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
