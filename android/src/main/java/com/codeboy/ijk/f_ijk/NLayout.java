package com.codeboy.ijk.f_ijk;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.codeboy.ijk.f_ijk.ui.IJKVideoView;
import com.codeboy.ijk.f_ijk.userOperation.IJKVideoManager;
import com.codeboy.ijk.f_ijk.userOperation.VideoInfoListner;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

/**
 * @Author: Codeboy 2685312867@qq.com
 * @Date: 2019-12-27 10:07
 */

public class NLayout implements PlatformView, VideoInfoListner, MethodChannel.MethodCallHandler {

    private IJKVideoView mVideoView;
    private IJKVideoManager mVideoManager;
    private ProgressBar mProgressBar;

    private String mUrl = "rtmp://58.200.131.2:1935/livetv/hunantv123";

    private int viewId;
    private LinearLayout mLinearLayout;
    private FlutterPlugin.FlutterPluginBinding flutterPluginBinding;

    public NLayout(Context context, FlutterPlugin.FlutterPluginBinding flutterPluginBinding,
                   int viewId, Map<String, Object> params) {
        this.flutterPluginBinding = flutterPluginBinding;
        mVideoView = new IJKVideoView(context);

        this.viewId = viewId;
        Boolean shouldAutoPlayer = (Boolean) params.get("shouldAutoPlayer");

        mUrl = (String) params.get("url");

        RelativeLayout.LayoutParams lp_videoView =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        lp_videoView.addRule(RelativeLayout.CENTER_IN_PARENT);
        mVideoView.setLayoutParams(lp_videoView);

        mVideoManager = mVideoView.getManager();
        mVideoManager.registStateChangeListner(this);
        mVideoManager.setUrl(mUrl);

        if (shouldAutoPlayer)
            mVideoManager.startPlay();

        String channelName = "plugins/live_player_" + viewId;
        final MethodChannel channel =
                new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(),
                        channelName);
        channel.setMethodCallHandler(this);

    }

    public void initView(Context context) {

    }

    @Override
    public View getView() {
        return this.mVideoView;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void error() {

    }

    @Override
    public void startPlay() {

    }

    @Override
    public void loading() {

    }

    @Override
    public void revealPic() {

    }

    @Override
    public void stopPlay() {

    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("play")) {
            mVideoManager.startPlay();
        } else if (call.method.equals("shutdown")) {
            mVideoManager.stopPlay();
        } else if (call.method.equals("id")) {
            result.success(viewId);
        }

    }
}
