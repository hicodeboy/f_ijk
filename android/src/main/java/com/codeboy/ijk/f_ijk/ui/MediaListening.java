package com.codeboy.ijk.f_ijk.ui;

import tv.danmaku.ijk.media.player.IMediaPlayer;

interface MediaListening {

    void onPrepared(IMediaPlayer mediaPlayer);

    void onVideoSizeChanged(IMediaPlayer mediaPlayer, int width, int height, int sarNum, int sarDen);

    void onCompletion(IMediaPlayer mediaPlayer);

    boolean onError();

    boolean onInfo(IMediaPlayer mp, int arg1, int arg2);

    void onBufferingUpdate(IMediaPlayer mp, int percent);

}
