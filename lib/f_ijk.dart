import 'dart:async';

import 'package:flutter/services.dart';

class FIjk {
  static const MethodChannel _channel =
      const MethodChannel('f_ijk');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
