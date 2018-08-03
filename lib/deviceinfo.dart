import 'dart:async';

import 'package:flutter/services.dart';

class Deviceinfo {
  static const MethodChannel _channel =
      const MethodChannel('deviceinfo');

  static Future<String> get appVersionName async {
      final String appVersionName = await _channel.invokeMethod('getAppVersionName');
      return appVersionName;
  }

  static Future<int> get deviceVersionCode async {
    final int deviceVersionCode = await _channel.invokeMethod('getDeviceVersionCode');
    return deviceVersionCode;
  }
}
