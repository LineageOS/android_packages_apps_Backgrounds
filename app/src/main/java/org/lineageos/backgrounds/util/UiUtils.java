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
package org.lineageos.backgrounds.util;

import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public final class UiUtils {

    private UiUtils() {
    }

    public static void setStatusBarColor(@NonNull final Window window, @ColorInt final int color) {
        window.setStatusBarColor(color);
        final boolean isLight = ColorUtils.isColorLight(color);
        int flags = window.getDecorView().getSystemUiVisibility();
        if (isLight) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        window.getDecorView().setSystemUiVisibility(flags);
    }
}
