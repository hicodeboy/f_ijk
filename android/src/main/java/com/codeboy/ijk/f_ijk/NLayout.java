package com.codeboy.ijk.f_ijk;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.platform.PlatformView;

/**
 * @Author: Codeboy 2685312867@qq.com
 * @Date: 2019-12-27 10:07
 */

public class NLayout implements PlatformView {

    private LinearLayout mLinearLayout;
    private BinaryMessenger messenger;

    public NLayout(Context context, BinaryMessenger messenger,
                   int id, Map<String, Object> params) {
        this.messenger = messenger;
        LinearLayout mLinearLayout = new LinearLayout(context);
        mLinearLayout.setBackgroundColor(Color.rgb(100,200,100));
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(900, 900);
        mLinearLayout.setLayoutParams(lp);
        this.mLinearLayout = mLinearLayout;
    }

    @Override
    public View getView() {
        return this.mLinearLayout;
    }

    @Override
    public void dispose() {

    }
}
