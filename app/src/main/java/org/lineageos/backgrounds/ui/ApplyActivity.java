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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.task.ApplyWallpaperTask;
import org.lineageos.backgrounds.task.LoadDrawableFromUriTask;
import org.lineageos.backgrounds.util.ColorUtils;
import org.lineageos.backgrounds.util.UiUtils;

public final class ApplyActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSITION_NAME = "transition_shared_preview";
    static final String EXTRA_WALLPAPER = "apply_extra_wallpaper_parcel";

    private static final int BOTH_FLAG = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private static final int HOME_FLAG = WallpaperManager.FLAG_SYSTEM;
    private static final int LOCK_FLAG = WallpaperManager.FLAG_LOCK;

    private ImageView mPreviewView;
    private LinearLayout mApplyView;
    private TextView mBothView;
    private TextView mHomeView;
    private TextView mLockView;
    private ImageView mCloseView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_apply);

        mPreviewView = findViewById(R.id.apply_preview);
        mApplyView = findViewById(R.id.apply_button);
        mBothView = findViewById(R.id.apply_both);
        mHomeView = findViewById(R.id.apply_home);
        mLockView = findViewById(R.id.apply_lock);
        mCloseView = findViewById(R.id.apply_close);

        mBothView.setOnClickListener(v -> applyWallpaper(BOTH_FLAG));
        mHomeView.setOnClickListener(v -> applyWallpaper(HOME_FLAG));
        mLockView.setOnClickListener(v -> applyWallpaper(LOCK_FLAG));
        mCloseView.setOnClickListener(v -> finish());

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

    private void applyWallpaper(final int flags) {
        hideApplyButton();
        mCloseView.setClickable(false);

        final Drawable drawable = mPreviewView.getDrawable();

        new ApplyWallpaperTask(new ApplyWallpaperTask.Callback() {
            @Override
            public void onCompleted(boolean result) {
                onWallpaperApplied(result);
            }

            @NonNull
            @Override
            public WallpaperManager getWallpaperManager() {
                return getSystemService(WallpaperManager.class);
            }

            @Override
            public int getFlags() {
                return flags;
            }
        }).execute(drawable);
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
        mApplyView.setScaleX(0f);
        mApplyView.setScaleY(0f);
        mApplyView.setVisibility(View.VISIBLE);
        mApplyView.animate()
                .setStartDelay(350)
                .scaleX(1f)
                .scaleY(1f)
                .start();
    }

    private void hideApplyButton() {
        mApplyView.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(250)
                .start();
    }

    private void onWallpaperApplied(final boolean success) {
        if (success) {
            setResult(MainActivity.RESULT_APPLIED);
        }

        Toast.makeText(this, success ? R.string.apply_success : R.string.apply_failure,
                Toast.LENGTH_LONG).show();
        finish();

    }

    private void colorUi() {
        final Drawable previewDrawable = mPreviewView.getDrawable();
        final int color = ColorUtils.extractColor(ColorUtils.extractPalette(previewDrawable));

        // SystemUI
        UiUtils.setSystemUiColors(getWindow(), color);

        // Apply
        final ColorStateList backgroundList = ColorStateList.valueOf(color);
        mApplyView.setBackgroundTintList(backgroundList);

        final int actionsColor = getColor(ColorUtils.isColorLight(color) ?
                android.R.color.black : android.R.color.white);
        final ColorStateList actionsList = ColorStateList.valueOf(actionsColor);

        mBothView.setTextColor(actionsColor);
        mBothView.setCompoundDrawableTintList(actionsList);
        mHomeView.setTextColor(actionsColor);
        mHomeView.setCompoundDrawableTintList(actionsList);
        mLockView.setTextColor(actionsColor);
        mLockView.setCompoundDrawableTintList(actionsList);
    }
}
