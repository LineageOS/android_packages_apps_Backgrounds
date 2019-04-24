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
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.lineageos.backgrounds.bundle.WallpaperBundle;

import java.util.List;

public final class FetchDataTask extends AsyncTask<Void, Integer, List<WallpaperBundle>> implements
        FetchDataImpl.Callback {
    private final FetchDataTask.Callback mCallbacks;

    public FetchDataTask(@NonNull final FetchDataTask.Callback taskCallback) {
        mCallbacks = taskCallback;
    }

    @Override
    protected List<WallpaperBundle> doInBackground(Void[] params) {
        return new FetchDataImpl(this).fetchData();
    }

    @Override
    protected void onPostExecute(List<WallpaperBundle> result) {
        mCallbacks.onCompleted(result);
    }

    @NonNull
    @Override
    public Resources getResources() {
        return mCallbacks.getResources();
    }

    @NonNull
    @Override
    public WallpaperManager getWallpaperManager() {
        return mCallbacks.getWallpaperManager();
    }

    public interface Callback {
        void onCompleted(@NonNull final List<WallpaperBundle> data);

        @NonNull
        Resources getResources();

        @NonNull
        WallpaperManager getWallpaperManager();
    }
}
