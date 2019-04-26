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

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.task.ApplyWallpaperTask;
import org.lineageos.backgrounds.task.LoadDrawableFromUriTask;
import org.lineageos.backgrounds.util.ColorUtils;
import org.lineageos.backgrounds.util.TypeConverter;
import org.lineageos.backgrounds.util.UiUtils;

public final class ApplyActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSITION_NAME = "transition_shared_preview";
    static final String EXTRA_WALLPAPER = "apply_extra_wallpaper_parcel";

    private static final int BOTH_FLAG = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
    private static final int HOME_FLAG = WallpaperManager.FLAG_SYSTEM;
    private static final int LOCK_FLAG = WallpaperManager.FLAG_LOCK;

    private ImageView mPreviewView;

    private BottomSheetBehavior mApplySheetBehavior;
    private boolean mIsApplyingWallpaper = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_apply);

        mPreviewView = findViewById(R.id.apply_preview);
        ImageView closeView = findViewById(R.id.apply_close);

        LinearLayout applySheetView = findViewById(R.id.apply_button);
        TextView bothView = findViewById(R.id.apply_both);
        TextView homeView = findViewById(R.id.apply_home);
        TextView lockView = findViewById(R.id.apply_lock);

        mApplySheetBehavior = BottomSheetBehavior.from(applySheetView);

        closeView.setOnClickListener(v -> quitIfDoingNothing());
        bothView.setOnClickListener(v -> applyWallpaper(BOTH_FLAG));
        homeView.setOnClickListener(v -> applyWallpaper(HOME_FLAG));
        lockView.setOnClickListener(v -> applyWallpaper(LOCK_FLAG));

        setup();
    }

    private void setup() {
        setupBottomSheet();

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
            case GRADIENT:
                setupGradient(wallpaperBundle);
                break;
            case MONO:
                setupMono(wallpaperBundle);
                break;
            case USER:
                setupUser(wallpaperBundle.getName());
                break;
        }
    }

    private void setupBottomSheet() {
        mApplySheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mApplySheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                /*  1 - Expanded
                 *  :
                 *  0 - Peek
                 *  :
                 * -1 - Hidden
                 */
                if (v == 1f || v == 0f || v == -1f) {
                    // Let the sliding sheet "lock in" the new position
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                    // Quit if the user slides the sheet out
                    if (v == -1f) {
                        quitIfDoingNothing();
                    }
                }
            }
        });
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

    private void setupGradient(@NonNull final WallpaperBundle bundle) {
        /*
         * Welcome to HackLand pt2: GradientDrawable doesn't play nicely with
         * shared element transitions, so we have to make some magic:
         * 1. Get the OG drawable
         * 2. Convert to a Bitmap
         * 3. Convert the Bitmap to a BitmapDrawable
         * 4. Apply the BitmapDrawable
         */
        final Drawable origDrawable = ContextCompat.getDrawable(this, bundle.getDescriptor());
        if (origDrawable == null) {
            return;
        }

        final Bitmap bm = TypeConverter.drawableToBitmap(origDrawable);
        final BitmapDrawable bmd = new BitmapDrawable(getResources(), bm);
        displayPreview(bmd);
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
        mIsApplyingWallpaper = true;

        hideApplyLayout();

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
        showApplyLayout();
    }

    private void showApplyLayout() {
        new Handler().postDelayed(() ->
                mApplySheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED), 350);
    }

    private void hideApplyLayout() {
        mApplySheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
        UiUtils.setStatusBarColor(getWindow(), color);
    }

    private void quitIfDoingNothing() {
        if (mIsApplyingWallpaper) {
            return;
        }

        finish();
    }
}
