package com.codeboy.ijk.f_ijk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.codeboy.ijk.f_ijk.userOperation.IJKVideoManager;
import com.codeboy.ijk.f_ijk.userOperation.InfoType;
import com.codeboy.ijk.f_ijk.userOperation.VideoInfoDispatcher;
import com.codeboy.ijk.f_ijk.widget.IRenderView;

import tv.danmaku.ijk.media.player.IMediaPlayer;


public class IJKVideoView extends FrameLayout implements MediaController.MediaPlayerControl,
        SurfaceCallback,
        MediaListening {

    private String mUriPath;

    private IJKVideoManager mVideoManager;
    private VideoInfoDispatcher mUserStatusDispatcher;


    private RenderManager mRenderManager;
    private IJKMediaPlayerManager mMediaPlayerManager;
    private VideoStatusManager mVideoStatusManager;


    public IJKVideoView(Context context) {
        super(context);
        initVideoView();
    }

    public IJKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public IJKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }


    private void initVideoView() {
        mMediaPlayerManager = new IJKMediaPlayerManager(this);
        mRenderManager = new RenderManager(getContext(), this);
        mVideoStatusManager = new VideoStatusManager();

        mUserStatusDispatcher = new VideoInfoDispatcher();
    }

    public void setRenderRotation(int degree) {
        //mRenderManager.setRenderRotation(degree);
    }

    public void setAspectRatio(int arAspectFitParent) {
        mRenderManager.setAspectRatio(arAspectFitParent);
    }


    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        mUriPath = path;
        openVideo();
        requestLayout();
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void openVideo() {
        if (mUriPath == null) {
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        mRenderManager.addRenderToView(this);

        IJKAudioManager.requestAudioFocus(getContext());

        mMediaPlayerManager.createPlayer(getContext());
        boolean isSuss = mMediaPlayerManager.setDataSource(mUriPath);
        if (!isSuss) {
            mVideoStatusManager.setStatusError();
            onError();
            return;
        }
        mRenderManager.bindSurfaceHolder(mMediaPlayerManager.getMediaPlayer());

        mVideoStatusManager.setCurrentState(VideoStatus.preparing);

    }


    /*
     * release the media player in any state
     */
    private void release(boolean cleartargetstate) {
        mMediaPlayerManager.destroyPlayer();
        IJKAudioManager.abandonAudioFocus(getContext());
        mVideoStatusManager.setCurrentState(VideoStatus.idle);
        if (cleartargetstate) {
            mVideoStatusManager.setTargetState(VideoStatus.idle);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void start() {
        if (mVideoStatusManager.isInPlaybackState(mMediaPlayerManager)) {
            mMediaPlayerManager.start();
            mVideoStatusManager.setCurrentState(VideoStatus.playing);
        }
        mVideoStatusManager.setTargetState(VideoStatus.playing);
    }

    @Override
    public void pause() {
        if (mVideoStatusManager.isInPlaybackState(mMediaPlayerManager)) {
            if (mMediaPlayerManager.isPlaying()) {
                mMediaPlayerManager.pause();
                mVideoStatusManager.setCurrentState(VideoStatus.paused);
            }
        }
        mVideoStatusManager.setTargetState(VideoStatus.paused);
    }

    public void stop() {
        mMediaPlayerManager.stopPlayer();
        IJKAudioManager.abandonAudioFocus(getContext());
        mVideoStatusManager.setStatusIdle();
        this.removeAllViews();
        mUserStatusDispatcher.dispatchStateChange(InfoType.stopPlay);
    }

    public void destroyPlayer() {
        release(true);
    }

    @Override
    public int getDuration() {
        if (mVideoStatusManager.isInPlaybackState(mMediaPlayerManager)) {
            return mMediaPlayerManager.getDuration();
        }
        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (mVideoStatusManager.isInPlaybackState(mMediaPlayerManager)) {
            return mMediaPlayerManager.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        //直播不需要拖动
    }

    @Override
    public boolean isPlaying() {
        return mVideoStatusManager.isInPlaybackState(mMediaPlayerManager) && mMediaPlayerManager.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        //直播不需要进度
        return 0;
    }


    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    public IJKVideoManager getManager() {
        if (mVideoManager == null) {
            mVideoManager = new IJKVideoManager(this,mUserStatusDispatcher);
        }
        return mVideoManager;
    }

    @Override
    public void onSurfaceCreated(IRenderView.ISurfaceHolder holder, int width, int height) {

        if (mMediaPlayerManager.getMediaPlayer() != null)
            mRenderManager.bindSurfaceHolder(mMediaPlayerManager.getMediaPlayer());
        else
            openVideo();
    }

    @Override
    public void onSurfaceChanged(IRenderView.ISurfaceHolder holder, int format, int width, int height) {
        if (mVideoStatusManager.getTargetState() == VideoStatus.playing) {
            start();
        }
    }

    @Override
    public void onSurfaceDestroyed(IRenderView.ISurfaceHolder holder) {
        mMediaPlayerManager.setDisplay(null);
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mVideoStatusManager.setCurrentState(VideoStatus.prepared);

        int videoWidth = mMediaPlayerManager.getVideoWidth();
        int videoHeight = mMediaPlayerManager.getVideoHeight();
        int videoSarNum = mMediaPlayerManager.getVideoSarNum();
        int videoSarDen = mMediaPlayerManager.getVideoSarDen();


        if (videoWidth != 0 && videoHeight != 0) {
            mRenderManager.getRenderView().setVideoSize(videoWidth, videoHeight);
            mRenderManager.getRenderView().setVideoSampleAspectRatio(videoSarNum, videoSarDen);
        }
        //可能会有获取videoWidth、videoHeight出错情况，但是也要开启播放
        if (mVideoStatusManager.getTargetState() == VideoStatus.playing) {
            start();
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
        if (mMediaPlayerManager.getVideoWidth() != 0 && mMediaPlayerManager.getVideoHeight() != 0) {
            mRenderManager.setVideoSize(mMediaPlayerManager.getVideoWidth(), mMediaPlayerManager.getVideoHeight());
            mRenderManager.setVideoSampleAspectRatio(mMediaPlayerManager.getVideoSarNum(), mMediaPlayerManager.getVideoSarDen());
            requestLayout();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mediaPlayer) {
        mVideoStatusManager.setStatusComplete();
    }

    @Override
    public boolean onError() {
        mVideoStatusManager.setStatusError();
        mUserStatusDispatcher.dispatchStateChange(InfoType.error);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        mUserStatusDispatcher.dispatchStateChange(what);
        return true;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        //直播不需要进度
    }
}
