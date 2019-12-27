package com.codeboy.ijk.f_ijk.ui;

import androidx.annotation.NonNull;

import com.codeboy.ijk.f_ijk.widget.IRenderView;

interface SurfaceCallback {
    void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height);

    void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int width, int height);

    void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder);
}
