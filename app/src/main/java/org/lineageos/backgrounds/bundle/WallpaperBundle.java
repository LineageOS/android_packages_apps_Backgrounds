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
package org.lineageos.backgrounds.bundle;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lineageos.backgrounds.util.TypeConverter;

public final class WallpaperBundle implements Parcelable {
    public static final int DESCRIPTOR_EMPTY = -1;
    public static final Creator<WallpaperBundle> CREATOR = new Creator<WallpaperBundle>() {
        @Override
        public WallpaperBundle createFromParcel(@NonNull final Parcel in) {
            return new WallpaperBundle(in);
        }

        @Override
        public WallpaperBundle[] newArray(int size) {
            return new WallpaperBundle[size];
        }
    };

    @NonNull
    private final String mName;

    @Nullable
    private final Drawable mContentDrawable;

    /*
     * Can be both color or resource drawable
     */
    private final int mDescriptor;

    @NonNull
    private final WallpaperType mType;

    public WallpaperBundle(@NonNull final String name,
                           @Nullable final Drawable contentDrawable,
                           int descriptor,
                           @NonNull final WallpaperType type) {
        mName = name;
        mContentDrawable = contentDrawable;
        mDescriptor = descriptor;
        mType = type;
    }

    private WallpaperBundle(@NonNull final Parcel parcel) {
        final String parcelName = parcel.readString();
        mName = parcelName == null ? "" : parcelName;
        mContentDrawable = null;
        mDescriptor = parcel.readInt();
        mType = TypeConverter.intToWallpaperType(parcel.readInt());
    }

    @NonNull
    public final String getName() {
        return mName;
    }

    @Nullable
    public final Drawable getContentDrawable() {
        return mContentDrawable;
    }

    @NonNull
    public final WallpaperType getType() {
        return mType;
    }

    public final int getDescriptor() {
        return mDescriptor;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof WallpaperBundle)) {
            return false;
        }

        WallpaperBundle otherBundle = (WallpaperBundle) other;
        return otherBundle.mType == mType && otherBundle.mContentDrawable == mContentDrawable;
    }

    @Override
    public int hashCode() {
        return mType.hashCode() + (mContentDrawable == null ? 0 : mContentDrawable.hashCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest, final int flags) {
        dest.writeString(mName);
        dest.writeInt(mDescriptor);
        dest.writeInt(TypeConverter.wallpaperTypeToInt(mType));
    }
}
