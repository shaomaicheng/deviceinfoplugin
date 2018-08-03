import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:deviceinfo/deviceinfo.dart';

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
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      appVersionName = await Deviceinfo.appVersionName;
      deviceVersionCode = await Deviceinfo.deviceVersionCode;
      appVersionCode = await Deviceinfo.appVersionCode;
      imei = await Deviceinfo.imei;
    } on PlatformException {
      appVersionName = 'Failed to get app version name.';
      deviceVersionCode = -2;
      appVersionCode = -2;
      imei = 'Failed to get app imei';
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
              )
            ],
          )
        ),
      ),
    );
  }
}
