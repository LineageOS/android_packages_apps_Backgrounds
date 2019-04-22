/*
 * Copyright (C) 2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lineageos.backgrounds.util;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static Palette extractPalette(@NonNull final Drawable drawable) {
        final Bitmap bm = TypeConverter.drawableToBitmap(drawable);
        return Palette.from(bm).generate();
    }

    @ColorInt
    public static int extractColor(@NonNull final Palette palette) {
        final int muted = palette.getMutedColor(Color.WHITE);
        if (muted != Color.WHITE) {
            return muted;
        }
        final int vibrant = palette.getVibrantColor(Color.WHITE);
        if (vibrant != Color.WHITE) {
            return vibrant;
        }
        return darken(palette.getDominantColor(Color.WHITE));
    }

    @ColorInt
    private static int darken(@ColorInt final int color) {
        return androidx.core.graphics.ColorUtils.blendARGB(color, Color.BLACK, 0.2f);
    }

    public static boolean isColorLight(@ColorInt final int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        //noinspection all
        float hsl[] = new float[3];

        androidx.core.graphics.ColorUtils.RGBToHSL(red, green, blue, hsl);
        return hsl[2] > 0.5f;
    }
}
