# deviceinfo

一个 `Flutter` 的获取设备信息的 `plugin`

**note** 这个 plugin 暂时只支持 Android 系统

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

* 获取Android device 的 imei 值 (需要获取 `READ_PHONE_STATE` 权限)

```dart
String imei = await _channel.invokeMethod('getImei');
```

* 获取 Android device 的 uuid 值

```dart
String deviceId = await Deviceinfo.uuid;
```

* 获取 Android device 的屏幕信息

```dart
ScreenDisplay screenDisplay = await Deviceinfo.screenInfo;
```

ScreenDisplay 包含：
1.width 屏幕的宽度
2.height 屏幕的高度

* 获取 Android device 的 Build 信息

```dart
Build buildInfo = await Deviceinfo.buildInfo;
```

Build类的数据结构包括：

1. String device;
2. String display;
3. String hardware;
4. String host;
5. String id;
6. String type;
7. String user;
8. String unknow;
9. Version version;

其中 Version 包括：

1. String release;
2. String sdk;
3. int sdkInt;
4. String codeName;

以上字段的含义和 Android 的 Build 里的相同名称字段含义一致


* 获取 Android device 的设备唯一标识 （device token）

```dart
String deviceToken = await Deviceinfo.deviceToken
```

