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

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.ui.SelectionInterface;
import org.lineageos.backgrounds.util.ColorUtils;

public class WallpaperHolder extends RecyclerView.ViewHolder {
    @NonNull
    final SelectionInterface callback;

    public WallpaperHolder(@NonNull View itemView,
                           @NonNull SelectionInterface callback) {
        super(itemView);
        this.callback = callback;
    }

    public void bind(@NonNull final WallpaperBundle bundle) {
        ImageView previewView = itemView.findViewById(R.id.item_wallpaper_preview);
        TextView nameView = itemView.findViewById(R.id.item_wallpaper_name);

        // We can't set this in xml layout: https://issuetracker.google.com/issues/37036728
        itemView.setClipToOutline(true);

        Drawable drawable = bundle.getContentDrawable();
        if (drawable != null) {
            previewView.setImageDrawable(drawable);

            // Tint title for contrast
            final int color = ColorUtils.extractContrastColor(ColorUtils.extractPaletteFromBottom(drawable));
            nameView.setTextColor(color);
        }

        String name = bundle.getName();
        nameView.setText(name);

        itemView.setOnClickListener((v) -> callback.onWallpaperSelected(previewView, bundle));
    }
}
