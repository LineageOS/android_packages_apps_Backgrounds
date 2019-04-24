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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.factory.BuiltInWallpaperFactory;
import org.lineageos.backgrounds.factory.GradientWallpaperFactory;
import org.lineageos.backgrounds.factory.MonoWallpaperFactory;
import org.lineageos.backgrounds.factory.UserWallpaperFactory;

import java.util.ArrayList;
import java.util.List;

final class FetchDataImpl {
    private final List<WallpaperBundle> mData = new ArrayList<>();
    private final Callback mCallbacks;

    FetchDataImpl(@NonNull final Callback callbacks) {
        mCallbacks = callbacks;
    }

    @NonNull
    List<WallpaperBundle> fetchData() {
        addUser();
        addBuiltIn();
        addColors();
        addGradients();

        return mData;
    }

    private void addUser() {
        mData.add(UserWallpaperFactory.build(mCallbacks.getResources()));
    }

    private void addBuiltIn() {
        Resources res = mCallbacks.getResources();

        // System wallpaper first
        WallpaperManager manager = mCallbacks.getWallpaperManager();
        mData.add(BuiltInWallpaperFactory.buildDefault(res, manager));

        // Other built-in
        String[] names = res.getStringArray(R.array.wallpaper_built_in_names);
        TypedArray drawables = res.obtainTypedArray(R.array.wallpaper_built_in_drawables);
        for (int i = 0; i < drawables.length(); i++) {
            final TypedValue value = new TypedValue();
            drawables.getValue(i, value);
            if (value.resourceId != 0) {
                mData.add(BuiltInWallpaperFactory.build(names[i], res, value.resourceId));
            }
        }

        drawables.recycle();
    }

    private void addColors() {
        Resources res = mCallbacks.getResources();
        String[] names = res.getStringArray(R.array.wallpaper_mono_names);
        TypedArray colors = res.obtainTypedArray(R.array.wallpaper_mono_colors);
        for (int i = 0; i < colors.length(); i++) {
            final int color = colors.getColor(i, Color.BLACK);
            mData.add(MonoWallpaperFactory.build(names[i], color));
        }

        colors.recycle();
    }

    private void addGradients() {
        Resources res = mCallbacks.getResources();
        String[] names = res.getStringArray(R.array.wallpaper_gradient_names);
        TypedArray gradients = res.obtainTypedArray(R.array.wallpaper_gradient_drawables);
        for (int i = 0; i < gradients.length(); i++) {
            final TypedValue value = new TypedValue();
            gradients.getValue(i, value);
            if (value.resourceId != 0) {
                mData.add(GradientWallpaperFactory.build(names[i], res, value.resourceId));
            }
        }

        gradients.recycle();
    }

    public interface Callback {
        @NonNull
        Resources getResources();

        @NonNull
        WallpaperManager getWallpaperManager();
    }
}
