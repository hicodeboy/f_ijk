package com.codeboy.ijk.f_ijk.ui;

public class VideoStatusManager {

    //初始状态设置为idle
    private VideoStatus mCurrentState = VideoStatus.idle;
    private VideoStatus mTargetState = VideoStatus.idle;


    public VideoStatus getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(VideoStatus currentState) {
        mCurrentState = currentState;
    }

    public VideoStatus getTargetState() {
        return mTargetState;
    }

    public void setTargetState(VideoStatus targetState) {
        mTargetState = targetState;
    }

    public void setStatusIdle() {
        mCurrentState = VideoStatus.idle;
        mTargetState = VideoStatus.idle;
    }

    public void setStatusError() {
        mCurrentState = VideoStatus.error;
        mTargetState = VideoStatus.error;
    }

    public void setStatusComplete() {
        mCurrentState = VideoStatus.complete;
        mTargetState = VideoStatus.complete;
    }

    public boolean isInPlaybackState(IJKMediaPlayerManager manager) {
        if (manager.getMediaPlayer() == null){
            return false;
        }
        return (mCurrentState != VideoStatus.error &&
                mCurrentState != VideoStatus.idle &&
                mCurrentState != VideoStatus.preparing);
    }
}
