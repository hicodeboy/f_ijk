package com.codeboy.ijk.f_ijk.userOperation;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoInfoDispatcher {
    private List<VideoInfoListner> mStateListners;

    public VideoInfoDispatcher() {
        mStateListners = new ArrayList<>();
    }


    public void registStateChangeListner(VideoInfoListner listener) {
        mStateListners.add(listener);
    }

    public void unRegistStateChangeListner(VideoInfoListner listener) {
        mStateListners.remove(listener);
    }

    public void dispatchStateChange(int what){
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                dispatchStateChange(InfoType.loading);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                dispatchStateChange(InfoType.revealPic);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                dispatchStateChange(InfoType.revealPic);
                break;
        }

    }

    public void dispatchStateChange(InfoType type) {
        for (VideoInfoListner listener : mStateListners) {
            switch (type) {
                case error:
                    listener.error();
                    break;
                case startPlay:
                    listener.startPlay();
                    break;
                case loading:
                    listener.loading();
                    break;
                case revealPic:
                    listener.revealPic();
                    break;
                case stopPlay:
                    listener.stopPlay();
                    break;
            }
        }
    }

}
