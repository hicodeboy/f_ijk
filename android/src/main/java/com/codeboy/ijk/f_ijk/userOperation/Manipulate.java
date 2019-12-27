package com.codeboy.ijk.f_ijk.userOperation;

public interface Manipulate {

    /*
    播放控制
     */
    void startPlay();
    void stopPlay();
    void destroyPlay();

    void setUrl(String url);
}
