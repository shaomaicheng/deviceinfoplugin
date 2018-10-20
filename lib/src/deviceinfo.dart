import 'dart:async';

import 'package:deinfo/src/buildinfo.dart';
import 'package:deinfo/src/screeninfo.dart';
import 'package:flutter/services.dart';
import 'dart:convert';

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
  
  static Future<String> get uuid async {
    final String deviceId = await _channel.invokeMethod('getUUID');
    return deviceId;
  }

  static Future<ScreenDisplay> get screenInfo async {
    final String screenDisplay = await _channel.invokeMethod("getdevicescreen");
    return ScreenDisplay.fromJson(json.decode(screenDisplay));
  }

  static Future<Build> get buildInfo async {
    final String buildInfo = await _channel.invokeMethod('getBuild');
    return Build.fromJson(json.decode(buildInfo));
  }

  static Future<String> get deviceToken async {
    final String deviceToken = await _channel.invokeMethod('getDeviceToken');
    return deviceToken;
  }

}
