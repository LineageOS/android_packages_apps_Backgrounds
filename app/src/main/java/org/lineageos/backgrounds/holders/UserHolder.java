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
package org.lineageos.backgrounds.holders;

import android.view.View;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.ui.SelectionInterface;

public final class UserHolder extends WallpaperHolder {

    public UserHolder(@NonNull final View itemView,
                      @NonNull final SelectionInterface callback) {
        super(itemView, callback);
    }

    @Override
    public void bind(@NonNull final WallpaperBundle bundle,
                     final boolean isLast) {
        super.bind(bundle, isLast);

        itemView.setOnClickListener(v -> callback.onWallpaperSelected(previewView, null));
    }
}
