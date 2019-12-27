package com.codeboy.ijk.f_ijk.userOperation;

import com.codeboy.ijk.f_ijk.ui.IJKVideoView;
import com.codeboy.ijk.f_ijk.widget.IRenderView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKVideoManager implements Manipulate {

    private final IJKVideoView fVideoView;
    private boolean mPlayerSupport;
    private VideoInfoDispatcher mStatusDispatcher;
    private String mUrl;

    private final int STATE_DESTROY = 1;
    private final int STATE_PLAYING = 2;
    private final int STATE_STOP = 3;

    private int mState = STATE_DESTROY;


    public IJKVideoManager(IJKVideoView videoView, VideoInfoDispatcher dispatcher) {
        fVideoView = videoView;
        mStatusDispatcher = dispatcher;

        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            mPlayerSupport = true;
        } catch (Exception e) {
            mPlayerSupport = false;
        }

        initVideoView();
    }


    private void initVideoView() {
        fVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        fVideoView.setRenderRotation(90);
    }

    public void registStateChangeListner(VideoInfoListner listener) {
        mStatusDispatcher.registStateChangeListner(listener);
    }

    public void unRegistStateChangeListner(VideoInfoListner listener) {
        mStatusDispatcher.unRegistStateChangeListner(listener);
    }

    @Override
    public void startPlay() {
        if (mState == STATE_PLAYING || !mPlayerSupport) {
            return;
        }
        if (mState == STATE_STOP) {
            fVideoView.openVideo();
        }
        fVideoView.start();
        mStatusDispatcher.dispatchStateChange(InfoType.startPlay);
        mState = STATE_PLAYING;
    }

    @Override
    public void stopPlay() {
        fVideoView.stop();
        mState = STATE_STOP;
    }


    @Override
    public void destroyPlay() {
        fVideoView.destroyPlayer();
        mState = STATE_DESTROY;
    }

    @Override
    public void setUrl(String url) {
        if (url == null || url.length() < 7)
            return;
        mUrl = url;
        if (mPlayerSupport) {
            fVideoView.setVideoPath(url);
        }
    }
}
