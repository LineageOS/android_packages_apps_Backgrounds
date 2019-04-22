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

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.bundle.WallpaperType;

public final class UserWallpaperFactory {

    private UserWallpaperFactory() {
    }

    @NonNull
    public static WallpaperBundle build(@NonNull Resources res) {
        String name = res.getString(R.string.main_wallpaper_pick);
        Drawable drawable = res.getDrawable(R.drawable.ic_wallpaper_add_local, res.newTheme());

        return new WallpaperBundle(name, drawable,
                WallpaperBundle.DESCRIPTOR_EMPTY, WallpaperType.USER);
    }

    @NonNull
    public static WallpaperBundle build(@NonNull String path) {
        return new WallpaperBundle(path, null,
                WallpaperBundle.DESCRIPTOR_EMPTY, WallpaperType.USER);
    }
}
