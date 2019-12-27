package com.codeboy.ijk.f_ijk.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.codeboy.ijk.f_ijk.widget.IRenderView;
import com.codeboy.ijk.f_ijk.widget.SurfaceRenderView;
import com.codeboy.ijk.f_ijk.widget.TextureRenderView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

class RenderManager implements IRenderView.IRenderCallback {

    private IRenderView mRenderView;
    private final int[] s_allAspectRatio = {
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            // IRenderView.AR_MATCH_PARENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT};

    private int mCurrentAspectRatioIndex = 0;
    private int mCurrentAspectRatio = s_allAspectRatio[0];
    private SurfaceCallback mSurfaceCallback;
    private IRenderView.ISurfaceHolder mSurfaceHolder = null;
    private int mSurfaceWidth;
    private int mSurfaceHeight;


    public RenderManager(Context context, IJKVideoView callback) {
        mSurfaceCallback = callback;
        mRenderView = createRender(RenderType.render_texture, context);
        initRenderView();
    }

    private IRenderView createRender(RenderType renderType, Context context) {
        IRenderView iRenderView = null;
        switch (renderType) {
            case render_none:
                break;
            case render_texture: {
                TextureRenderView renderView = new TextureRenderView(context);
                iRenderView = renderView;
                break;
            }
            case render_surface: {
                SurfaceRenderView renderView = new SurfaceRenderView(context);
                iRenderView = renderView;
                break;
            }
        }
        return iRenderView;
    }


    private void initRenderView() {
        if (mRenderView == null) {
            return;
        }
        mRenderView.setAspectRatio(mCurrentAspectRatio);
        mRenderView.addRenderCallback(this);
    }

    public IRenderView getRenderView() {
        return mRenderView;
    }


    public void bindSurfaceHolder(IMediaPlayer mp) {
        if (mp == null)
            return;

        if (mSurfaceHolder == null) {
            mp.setDisplay(null);
            return;
        }

        mSurfaceHolder.bindToMediaPlayer(mp);
    }

    public void addRenderToView(IJKVideoView videoView) {
        videoView.removeAllViews();
        View renderUIView = mRenderView.getView();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        renderUIView.setLayoutParams(lp);
        videoView.addView(renderUIView);
    }

    @Override
    public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
        if (holder.getRenderView() != mRenderView) {
            return;
        }
        mSurfaceHolder = holder;
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mSurfaceCallback.onSurfaceCreated(holder, width, height);
    }

    @Override
    public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int width, int height) {
        if (holder.getRenderView() != mRenderView) {
            return;
        }
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mSurfaceCallback.onSurfaceChanged(holder, format, width, height);
    }

    @Override
    public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
        if (holder.getRenderView() != mRenderView) {
            return;
        }
        mSurfaceHolder = null;
        mSurfaceCallback.onSurfaceDestroyed(holder);
    }

    public int getSurfaceWidth() {
        return mSurfaceWidth;
    }

    public int getSurfaceHeight() {
        return mSurfaceHeight;
    }

    public void setAspectRatio(int arAspectFitParent){
        if (mRenderView != null) {
            mRenderView.setAspectRatio(arAspectFitParent);
        }
    }

    public void setRenderRotation(int degree){
        if (mRenderView != null) {
            mRenderView.setVideoRotation(degree);
        }
    }

    public void setVideoSize(int width,int height){
        if (mRenderView != null) {
            mRenderView.setVideoSize(width,height);
        }
    }

    public void setVideoSampleAspectRatio(int width,int height){
        if (mRenderView != null) {
            mRenderView.setVideoSampleAspectRatio(width,height);
        }
    }
}
