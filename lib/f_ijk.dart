import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

typedef void LivePlayerWidgetCreatedCallback(LivePlayerController controller);

class LivePlayerController {
  LivePlayerController._(int id)
      : _channel = MethodChannel('plugins/live_player_$id');

  final MethodChannel _channel;

  Future<void> play() async {
    return _channel.invokeMethod('play');
  }

  Future<void> shutdown() async {
    return _channel.invokeMethod('shutdown');
  }
}

class LivePlayerView extends StatelessWidget {
  final LivePlayerWidgetCreatedCallback callback;
  final String url;
  final bool shouldAutoPlayer;
  int _id;


  int get id => _id;

  LivePlayerView({Key key, this.callback, this.url, this.shouldAutoPlayer})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'plugins/live_player',
        onPlatformViewCreated: _onPlatformViewCreated,
        creationParams: <String, dynamic>{'url': url, 'shouldAutoPlayer': shouldAutoPlayer},
        creationParamsCodec: StandardMessageCodec(),
      );
    }
    return Text('Live插件尚不支持$defaultTargetPlatform');
  }

  void _onPlatformViewCreated(int id) {
    if (callback == null) return;
    _id = id;
    callback(LivePlayerController._(id));
  }
}


class FIjk {
  static const MethodChannel _channel = const MethodChannel('f_ijk');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
