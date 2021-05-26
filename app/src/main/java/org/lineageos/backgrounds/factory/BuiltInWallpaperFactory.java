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
package org.lineageos.backgrounds.factory;

import android.app.WallpaperManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.bundle.WallpaperType;
import org.lineageos.backgrounds.util.UiUtils;

public final class BuiltInWallpaperFactory {

    private BuiltInWallpaperFactory() {
    }

    public static WallpaperBundle build(@NonNull final String name,
                                        @NonNull final Resources res,
                                        @DrawableRes final int drawableRes) {        
        Drawable drawable = new BitmapDrawable(res, UiUtils.decodeSampledBitmapFromResource(res, drawableRes, 250, 500));

        return new WallpaperBundle(name, drawable, drawableRes, WallpaperType.BUILT_IN);
    }

    public static WallpaperBundle buildDefault(@NonNull Resources res,
                                               @NonNull WallpaperManager manager) {
        final String name = res.getString(R.string.wallpaper_built_in_system);
        final Drawable drawable = manager.getBuiltInDrawable();

        return new WallpaperBundle(name, drawable,
                WallpaperBundle.DESCRIPTOR_EMPTY, WallpaperType.DEFAULT);
    }
}
