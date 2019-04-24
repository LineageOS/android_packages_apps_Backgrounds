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
package org.lineageos.backgrounds.task;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.util.TypeConverter;

import java.io.IOException;

final class ApplyWallpaperImpl {
    private static final String TAG = "ApplyWallpaperImpl";

    @NonNull
    private final Callback mCallback;

    ApplyWallpaperImpl(@NonNull final Callback callback) {
        mCallback = callback;
    }

    boolean apply(@NonNull final Drawable drawable) {
        final Bitmap bm = TypeConverter.drawableToBitmap(drawable);
        final WallpaperManager manager = mCallback.getWallpaperManager();

        try {
            manager.setBitmap(bm);
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    interface Callback {

        @NonNull
        WallpaperManager getWallpaperManager();
    }
}
