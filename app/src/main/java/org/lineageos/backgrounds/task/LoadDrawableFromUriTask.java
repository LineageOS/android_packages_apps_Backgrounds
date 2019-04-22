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

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class LoadDrawableFromUriTask extends AsyncTask<String, Void, Drawable> implements
        LoadDrawableFromUriImpl.Callback {
    @NonNull
    private final Callback mCallback;

    public LoadDrawableFromUriTask(@NonNull final Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Drawable doInBackground(@NonNull final String... strings) {
        String arg = strings[0];
        return new LoadDrawableFromUriImpl(this).fetchDrawableFromUri(arg);
    }

    @Override
    protected void onPostExecute(@Nullable final Drawable drawable) {
        super.onPostExecute(drawable);
        mCallback.onCompleted(drawable);
    }

    @NonNull
    @Override
    public ContentResolver getContentResolver() {
        return mCallback.getContentResolver();
    }

    @NonNull
    @Override
    public Resources getResources() {
        return mCallback.getResources();
    }

    public interface Callback {
        void onCompleted(@Nullable final Drawable drawable);

        @NonNull
        ContentResolver getContentResolver();

        @NonNull
        Resources getResources();
    }
}
