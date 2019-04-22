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
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.bundle.WallpaperType;

public final class TypeConverter {

    private TypeConverter() {
    }

    @NonNull
    public static WallpaperType intToWallpaperType(final int value) {
        switch (value) {
            case 1:
                return WallpaperType.BUILT_IN;
            case 2:
                return WallpaperType.DEFAULT;
            case 3:
                return WallpaperType.MONO;
            default:
                return WallpaperType.USER;
        }
    }

    public static int wallpaperTypeToInt(@NonNull final WallpaperType type) {
        switch (type) {
            case BUILT_IN:
                return 1;
            case DEFAULT:
                return 2;
            case MONO:
                return 3;
            default:
                return 0;
        }
    }

    @NonNull
    public static Bitmap drawableToBitmap(@NonNull final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            final BitmapDrawable bmd = (BitmapDrawable) drawable;
            final Bitmap bm = bmd.getBitmap();
            if (bm != null) {
                return bm;
            }
        }

        final Bitmap bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bm;
    }
}
