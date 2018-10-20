import 'dart:async';

import 'package:deinfo/src/buildinfo.dart';
import 'package:deinfo/src/deviceinfo.dart';
import 'package:deinfo/src/screeninfo.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _appVersionName = 'Unknown';
  int _deviceVersionCode = -1;
  int _appVersionCode = -1;
  String _imei = 'Unknown';
  String _deviceId = '';
  ScreenDisplay _screenDisplay;
  Build _buildInfo;
  String _deviceToken;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }


  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String appVersionName;
    int deviceVersionCode;
    int appVersionCode;
    String imei;
    String deviceId;
    ScreenDisplay screenDisplay;
    Build buildInfo;
    String deviceToken;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      appVersionName = await Deviceinfo.appVersionName;
      deviceVersionCode = await Deviceinfo.deviceVersionCode;
      appVersionCode = await Deviceinfo.appVersionCode;
      imei = await Deviceinfo.imei;
      deviceId = await Deviceinfo.uuid;
      screenDisplay = await Deviceinfo.screenInfo;
      buildInfo = await Deviceinfo.buildInfo;
      deviceToken = await Deviceinfo.deviceToken;
    } on PlatformException {
      appVersionName = 'Failed to get app version name.';
      deviceVersionCode = -2;
      appVersionCode = -2;
      imei = 'Failed to get app imei';
      deviceId = 'Failed to get app deviceId';
      screenDisplay = null;
      buildInfo = null;
      deviceToken = 'Failed to get device token';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    // if (!mounted) return;

    setState(() {
      _appVersionName = appVersionName;
      _deviceVersionCode = deviceVersionCode;
      _appVersionCode = appVersionCode;
      _imei = imei;
      _deviceId = deviceId;
      _screenDisplay = screenDisplay;
      _buildInfo = buildInfo;
      _deviceToken = deviceToken;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: Column(
            children: <Widget>[
              Expanded(
                child: new Text('Running on appVersionName: $_appVersionName\n'),
              ),
              Expanded(
                child: Text('deviceVersionCode: $_deviceVersionCode'),
              ),
              Expanded(
                child: Text('appVersionCode: $_appVersionCode'),
              ),
              Expanded(
                child: Text('app imei: $_imei'),
              ),
              Expanded(
                child: Text('app deviceId: $_deviceId'),
              ),
              Expanded(
                child: Text(
                    'device screen display, width: ${_screenDisplay == null ? 0 : _screenDisplay.width};  height: ${_screenDisplay == null ? 0 : _screenDisplay.height}'
                )
              ),
              Expanded(
//                child: Text('device build info: ${_buildInfo == null ? "build info is null" : json.encode(_buildInfo)}'),
                child: Text('device build info:'),
              ),
              Expanded(
                child: Text('device token: ${_deviceToken}'),
              )
            ],
          )
        ),
      ),
    );
  }
}
