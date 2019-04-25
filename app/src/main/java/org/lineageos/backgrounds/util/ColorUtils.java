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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

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

    public static Palette extractPaletteFromBottom(@NonNull final Drawable drawable) {
        final Bitmap originalBm = TypeConverter.drawableToBitmap(drawable);
        // Crop bottom 20%
        final int cropY = (int) (originalBm.getHeight() * 0.8f);
        final Bitmap bottomPart = Bitmap.createBitmap(originalBm, 0, cropY,
                originalBm.getWidth(), originalBm.getHeight() - cropY);
        return Palette.from(bottomPart).generate();
    }

    @ColorInt
    public static int extractColor(@NonNull final Palette palette) {
        final int muted = palette.getDominantColor(Color.WHITE);
        if (muted != Color.WHITE) {
            return muted;
        }
        final int vibrant = palette.getVibrantColor(Color.WHITE);
        if (vibrant != Color.WHITE) {
            return vibrant;
        }
        return palette.getMutedColor(Color.WHITE);
    }


    @ColorInt
    public static int extractContrastColor(@NonNull final Palette palette) {
        int color = Color.BLACK;

        final Palette.Swatch dominant = palette.getDominantSwatch();
        if (dominant != null) {
            color = dominant.getRgb();
        } else {
            final Palette.Swatch vibrant = palette.getVibrantSwatch();
            if (vibrant != null) {
                color = vibrant.getRgb();
            }
        }

        Log.d("OHAI", Integer.toHexString(color));

        return isColorLight(color) ? Color.BLACK : Color.WHITE;
    }

    public static boolean isColorLight(@ColorInt final int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        //noinspection all
        float hsl[] = new float[3];

        androidx.core.graphics.ColorUtils.RGBToHSL(red, green, blue, hsl);
        return hsl[2] > 0.76f;
    }
}
