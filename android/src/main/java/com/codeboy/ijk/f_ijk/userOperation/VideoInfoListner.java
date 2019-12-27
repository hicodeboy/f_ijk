package com.codeboy.ijk.f_ijk.userOperation;

public interface VideoInfoListner {
    //播放出错
    void error();

    //进入播放界面
    void startPlay();

    //缓冲加载
    void loading();

    //画面出现
    void revealPic();

    //结束播放
    void stopPlay();
}
