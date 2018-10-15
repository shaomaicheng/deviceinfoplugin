import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:deviceinfo/deviceinfo.dart';
import 'package:deviceinfo/screeninfo.dart';

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
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      appVersionName = await Deviceinfo.appVersionName;
      deviceVersionCode = await Deviceinfo.deviceVersionCode;
      appVersionCode = await Deviceinfo.appVersionCode;
      imei = await Deviceinfo.imei;
      deviceId = await Deviceinfo.deviceId;
      screenDisplay = await Deviceinfo.screenInfo;
    } on PlatformException {
      appVersionName = 'Failed to get app version name.';
      deviceVersionCode = -2;
      appVersionCode = -2;
      imei = 'Failed to get app imei';
      deviceId = 'Failed to get app deviceId';
      screenDisplay = null;
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
                child: Text('device screen display, width: ${_screenDisplay.width};  height: ${_screenDisplay.height}'),
              )
            ],
          )
        ),
      ),
    );
  }
}
