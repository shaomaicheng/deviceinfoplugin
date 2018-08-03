# deviceinfo

一个 `Flutter` 的获取设备信息的 `plugin`

## Getting Started

* 获取 Android app version name
```dart
String appVersionName = await _channel.invokeMethod('getAppVersionName');
```

* 获取 Android app version code
```dart
int appVersionCode = await _channel.invokeMethod('getAppVersionCode');
```

* 获取Android device version code (Android API level)
```dart
int deviceVersionCode = await _channel.invokeMethod('getDeviceVersionCode');
```

