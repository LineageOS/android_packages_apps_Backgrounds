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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.IOException;

final class LoadDrawableFromUriImpl {
    @NonNull
    private final Callback mCallbacks;

    LoadDrawableFromUriImpl(@NonNull final Callback callback) {
        mCallbacks = callback;
    }

    @Nullable
    Drawable fetchDrawableFromUri(@NonNull final String uriString) {
        Uri uri = Uri.parse(uriString);
        try {
            ParcelFileDescriptor parcelDescriptor = mCallbacks.getContentResolver()
                    .openFileDescriptor(uri, "r");
            if (parcelDescriptor == null) {
                return null;
            }

            FileDescriptor fileDescriptor = parcelDescriptor.getFileDescriptor();
            Bitmap bm = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            bm.setDensity(Bitmap.DENSITY_NONE);
            return new BitmapDrawable(mCallbacks.getResources(), bm);
        } catch (IOException e) {
            return null;
        }

    }

    public interface Callback {
        @NonNull
        ContentResolver getContentResolver();

        @NonNull
        Resources getResources();
    }
}
