package com.codeboy.ijk.f_ijk;

import android.content.Context;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * @Author: Codeboy 2685312867@qq.com
 * @Date: 2019-12-27 10:17
 */

public class NLayoutFactory extends PlatformViewFactory {
    private FlutterPlugin.FlutterPluginBinding flutterPluginBinding;

    public NLayoutFactory(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        super(StandardMessageCodec.INSTANCE);
        this.flutterPluginBinding = flutterPluginBinding;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        return new NLayout(context, this.flutterPluginBinding, viewId, params);
    }

    public static void registerWith(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {

        flutterPluginBinding.getPlatformViewRegistry().
                registerViewFactory("plugins/live_player",
                        new NLayoutFactory(flutterPluginBinding));
    }
}
