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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

public final class ApplyWallpaperTask extends AsyncTask<Drawable, Void, Boolean> implements
        ApplyWallpaperImpl.Callback {
    @NonNull
    private final Callback mCallbacks;

    public ApplyWallpaperTask(@NonNull final Callback callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected Boolean doInBackground(@NonNull Drawable... drawables) {
        final Drawable drawable = drawables[0];
        return new ApplyWallpaperImpl(this).apply(drawable);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        mCallbacks.onCompleted(result);
    }

    @NonNull
    @Override
    public WallpaperManager getWallpaperManager() {
        return mCallbacks.getWallpaperManager();
    }

    @Override
    public int getFlags() {
        return mCallbacks.getFlags();
    }

    public interface Callback {
        void onCompleted(final boolean result);

        @NonNull
        WallpaperManager getWallpaperManager();

        int getFlags();
    }
}
