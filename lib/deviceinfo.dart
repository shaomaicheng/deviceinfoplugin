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

  static Future<int> get appVersionCode async {
    final int appVersionCode = await _channel.invokeMethod('getAppVersionCode');
    return appVersionCode;
  }

  static Future<String> get imei async {
    final String imei = await _channel.invokeMethod('getImei');
    return imei;
  }
}
