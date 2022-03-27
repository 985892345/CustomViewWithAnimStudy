package com.ndhzs.share_element2;

import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;
import java.io.Serializable;

public class Sample implements Serializable {

    final int color;
    private final String name;

    public Sample(@ColorInt int color, String name) {
        this.color = color;
        this.name = name;
    }

    public static void setColorTint(ImageView view, int color) {
        DrawableCompat.setTint(view.getDrawable(), color);
        //view.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}