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
package org.lineageos.backgrounds.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import org.lineageos.backgrounds.bundle.WallpaperBundle;

import java.util.List;

final class WallsDiffCallback extends DiffUtil.Callback {
    @NonNull
    private final List<WallpaperBundle> mOld;

    @NonNull
    private final List<WallpaperBundle> mNew;

    WallsDiffCallback(@NonNull final List<WallpaperBundle> oldList,
                      @NonNull final List<WallpaperBundle> newList) {
        mOld = oldList;
        mNew = newList;
    }

    @Override
    public int getOldListSize() {
        return mOld.size();
    }

    @Override
    public int getNewListSize() {
        return mNew.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition,
                                   final int newItemPosition) {
        return mOld.get(oldItemPosition).getName().equals(mNew.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition,
                                      final int newItemPosition) {
        return mOld.get(oldItemPosition).equals(mNew.get(newItemPosition));
    }
}
