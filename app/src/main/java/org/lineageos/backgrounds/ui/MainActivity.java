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
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.lineageos.backgrounds.R;
import org.lineageos.backgrounds.adapters.WallsAdapter;
import org.lineageos.backgrounds.bundle.WallpaperBundle;
import org.lineageos.backgrounds.bundle.WallpaperType;
import org.lineageos.backgrounds.factory.UserWallpaperFactory;
import org.lineageos.backgrounds.task.FetchDataTask;
import org.lineageos.backgrounds.util.UiUtils;

import java.util.List;

public final class MainActivity extends AppCompatActivity implements SelectionInterface {
    public static final int RESULT_APPLIED = 917;
    private static final int PICK_IMAGE_FROM_EXT = 618;
    private static final int APPLY_WALLPAPER = 619;

    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingTextView;
    private RecyclerView mContentRecyclerView;

    private WallsAdapter mAdapter;

    @Nullable
    private View mHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_main);

        mLoadingProgressBar = findViewById(R.id.main_loading_bar);
        mLoadingTextView = findViewById(R.id.main_loading_text);
        mContentRecyclerView = findViewById(R.id.main_recyclerview);

        setupRecyclerView();
        loadContent();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Cleanup
        if (mHolder != null) {
            mHolder.setTransitionName("");
            mHolder = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_EXT && data != null) {
            onPickedFromExt(data.getDataString());
            return;
        }
        if (requestCode == APPLY_WALLPAPER && resultCode == RESULT_APPLIED) {
            // We're done
            finish();
        }
    }

    @Override
    public void onWallpaperSelected(@NonNull View view, @Nullable WallpaperBundle bundle) {
        mHolder = view;
        if (bundle == null) {
            pickWallpaperFromExternalStorage();
        } else {
            openPreview(bundle);
        }
    }

    private void setupRecyclerView() {
        //UiUtils.addSystemUiPadding(getResources(), mContentRecyclerView);

        mAdapter = new WallsAdapter(this);

        int numOfColumns = getResources().getInteger(R.integer.main_list_columns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                numOfColumns, LinearLayout.VERTICAL);
        mContentRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mContentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContentRecyclerView.setAdapter(mAdapter);
    }

    private void loadContent() {
        new FetchDataTask(new FetchDataTask.Callback() {
            @Override
            public void onCompleted(@NonNull List<WallpaperBundle> data) {
                onContentLoaded(data);
            }

            @NonNull
            @Override
            public Resources getResources() {
                return MainActivity.this.getResources();
            }

            @NonNull
            @Override
            public WallpaperManager getWallpaperManager() {
                return getSystemService(WallpaperManager.class);
            }
        }).execute();
    }

    private void onContentLoaded(@NonNull final List<WallpaperBundle> data) {
        mAdapter.setData(data);

        postContentLoaded();
    }

    private void postContentLoaded() {
        mLoadingTextView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.GONE);
        mContentRecyclerView.setVisibility(View.VISIBLE);
    }

    private void pickWallpaperFromExternalStorage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/*");

        startActivityForResult(intent, PICK_IMAGE_FROM_EXT);
    }

    private void openPreview(@NonNull final WallpaperBundle bundle) {
        Intent intent = new Intent(this, ApplyActivity.class)
                .putExtra(ApplyActivity.EXTRA_WALLPAPER, bundle);
        if (mHolder == null) {
            return;
        }

        if (bundle.getType() == WallpaperType.USER) {
            // No animations for you
            startActivity(intent);
            return;
        }

        // Shared element transition
        mHolder.setTransitionName(ApplyActivity.EXTRA_TRANSITION_NAME);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, mHolder, ApplyActivity.EXTRA_TRANSITION_NAME);

        startActivityForResult(intent, APPLY_WALLPAPER, options.toBundle());
    }

    private void onPickedFromExt(@Nullable final String uriString) {
        if (uriString == null) {
            return;
        }
        // Pass a fake bundle with name as URI path
        WallpaperBundle fakeBundle = UserWallpaperFactory.build(uriString);
        //noinspection All: we know mHolder is not null at this point
        onWallpaperSelected(mHolder, fakeBundle);
    }
}
