package com.codeboy.ijk.f_ijk.ui;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import com.codeboy.ijk.f_ijk.utils.Settings;

import java.io.IOException;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

class IJKMediaPlayerManager implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnSeekCompleteListener {

    private IMediaPlayer mMediaPlayer = null;
    private MediaListening mListener;


    public IJKMediaPlayerManager(MediaListening mediaListening) {
        mListener = mediaListening;
    }


    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setDisplay(SurfaceHolder var) {
        if (mMediaPlayer != null)
            mMediaPlayer.setDisplay(var);
    }

    private void registListners() {
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
    }

    public void destroyPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;

        }
    }

    public void stopPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            setDisplay(null);
        }
    }


    public IMediaPlayer createPlayer(Context context) {
        if (mMediaPlayer==null){
            mMediaPlayer = newPlayer(MediaPlayerType.msPlayer, context);
        }
        registListners();
        return mMediaPlayer;
    }

    public boolean setDataSource(String url) {
        boolean setSuss = true;
        if (mMediaPlayer == null) {
            return false;
        }
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            setSuss = false;
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.prepareAsync();

        setSuss = true;
        return setSuss;
    }


    private IMediaPlayer newPlayer(MediaPlayerType playerType, Context context) {
        Settings settings = new Settings(context);

        IMediaPlayer mediaPlayer = null;

        switch (playerType) {
            case exoPlayer: {
                IjkExoMediaPlayer IjkExoMediaPlayer = new IjkExoMediaPlayer(context);
                mediaPlayer = IjkExoMediaPlayer;
            }
            break;
            case systemPlayer: {
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
            }
            break;
            case msPlayer:
            default: {
                IjkMediaPlayer ijkMediaPlayer = null;

                ijkMediaPlayer = new IjkMediaPlayer();
                ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

                if (settings.getUsingMediaCodec()) {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                    if (settings.getUsingMediaCodecAutoRotate()) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
                    }
                    if (settings.getMediaCodecHandleResolutionChange()) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
                    }
                } else {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
                }

                if (settings.getUsingOpenSLES()) {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
                } else {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
                }

                String pixelFormat = settings.getPixelFormat();
                if (TextUtils.isEmpty(pixelFormat)) {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
                } else {
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
                }
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

                mediaPlayer = ijkMediaPlayer;
            }
            break;
        }

        if (settings.getEnableDetachedSurfaceTextureView()) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }

        return mediaPlayer;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mListener.onPrepared(mp);
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
        mListener.onVideoSizeChanged(mp, width, height, sarNum, sarDen);
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        mListener.onCompletion(mp);
    }

    @Override
    public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
        mListener.onError();
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int arg, int arz) {
        mListener.onInfo(mp, arg, arz);
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        //直播不需要
        mListener.onBufferingUpdate(mp,percent);
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        //直播不需要
    }

    public int getVideoWidth() {
        if (mMediaPlayer == null)
            return 0;
        return mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        if (mMediaPlayer == null)
            return 0;
        return mMediaPlayer.getVideoHeight();
    }

    public int getVideoSarNum() {
        if (mMediaPlayer == null)
            return 0;
        return mMediaPlayer.getVideoSarNum();
    }

    public int getVideoSarDen() {
        if (mMediaPlayer == null)
            return 0;
        return mMediaPlayer.getVideoSarDen();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public int getDuration() {
        return (int) mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return (int) mMediaPlayer.getCurrentPosition();
    }
}
