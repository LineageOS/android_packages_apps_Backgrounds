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
package org.lineageos.backgrounds.ui;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.task.LoadDrawableFromUriTask;
import org.lineageos.backgrounds.util.ColorUtils;
import org.lineageos.backgrounds.util.TypeConverter;
import org.lineageos.backgrounds.util.UiUtils;

import java.io.IOException;

public final class ApplyActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSITION_NAME = "transition_shared_preview";
    static final String EXTRA_WALLPAPER = "apply_extra_wallpaper_parcel";

    private ImageView mPreviewView;
    private TextView mButtonView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_apply);

        mPreviewView = findViewById(R.id.apply_preview);
        mButtonView = findViewById(R.id.apply_button);

        mButtonView.setOnClickListener(v -> applyWallpaper());

        setup();
    }

    private void setup() {
        final WallpaperBundle wallpaperBundle = getIntent().getParcelableExtra(EXTRA_WALLPAPER);
        if (wallpaperBundle == null) {
            return;
        }

        switch (wallpaperBundle.getType()) {
            case BUILT_IN:
                setupBuiltIn(wallpaperBundle);
                break;
            case DEFAULT:
                setupDefault();
                break;
            case MONO:
                setupMono(wallpaperBundle);
                break;
            case USER:
                setupUser(wallpaperBundle.getName());
                break;
        }
    }

    private void setupBuiltIn(@NonNull final WallpaperBundle bundle) {
        Drawable drawable = ContextCompat.getDrawable(this, bundle.getDescriptor());
        displayPreview(drawable);
    }

    private void setupDefault() {
        WallpaperManager manager = getSystemService(WallpaperManager.class);
        Drawable drawable = manager.getBuiltInDrawable();
        displayPreview(drawable);
    }

    private void setupMono(@NonNull final WallpaperBundle bundle) {
        /*
         * Welcome to HackLand: ColorDrawable doesn't play nicely with
         * shared element transitions, so we have to draw a Bitmap and convert
         * it to a Drawable through BitmapDrawable
         */
        final Bitmap bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        bm.eraseColor(bundle.getDescriptor());
        Drawable drawable = new BitmapDrawable(getResources(), bm);
        displayPreview(drawable);
    }

    private void setupUser(@Nullable final String wallpaperUri) {
        if (wallpaperUri == null) {
            finish();
            return;
        }

        new LoadDrawableFromUriTask(new LoadDrawableFromUriTask.Callback() {
            @Override
            public void onCompleted(@Nullable Drawable drawable) {
                displayPreview(drawable);
            }

            @NonNull
            @Override
            public ContentResolver getContentResolver() {
                return ApplyActivity.this.getContentResolver();
            }

            @NonNull
            @Override
            public Resources getResources() {
                return ApplyActivity.this.getResources();
            }
        }).execute(wallpaperUri);
    }

    private void applyWallpaper() {
        final Drawable drawable = mPreviewView.getDrawable();
        final WallpaperManager manager = getSystemService(WallpaperManager.class);

        final Bitmap bm = TypeConverter.drawableToBitmap(drawable);

        hideApplyButtonAndClose();

        try {
            manager.setBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayPreview(@Nullable final Drawable drawable) {
        if (drawable == null) {
            return;
        }

        mPreviewView.setImageDrawable(drawable);
        colorUi();
        showApplyButton();
    }

    private void showApplyButton() {
        mButtonView.setScaleX(0f);
        mButtonView.setScaleY(0f);
        mButtonView.setVisibility(View.VISIBLE);
        mButtonView.animate()
                .setStartDelay(350)
                .scaleX(1f)
                .scaleY(1f)
                .start();
    }

    private void hideApplyButtonAndClose() {
        if (mButtonView.getVisibility() == View.GONE) {
            return;
        }

        mButtonView.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(75)
                .withEndAction(this::finish)
                .start();
    }

    private void colorUi() {
        final Drawable previewDrawable = mPreviewView.getDrawable();
        final int color = ColorUtils.extractColor(ColorUtils.extractPalette(previewDrawable));

        // SystemUI
        UiUtils.setSystemUiColors(getWindow(), color);

        // Apply
        final ColorStateList colorStateList = ColorStateList.valueOf(color);
        mButtonView.setBackgroundTintList(colorStateList);
        mButtonView.setTextColor(getColor(ColorUtils.isColorLight(color) ?
                android.R.color.black : android.R.color.white));
    }
}
