import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:f_ijk/f_ijk.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  LivePlayerController _livePlayerController;

  void _onLivePlayerController(LivePlayerController livePlayerController) {
    _livePlayerController = livePlayerController;
  }

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FIjk.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin FIJK example app'),
        ),
        body: Stack(
          alignment: Alignment.bottomCenter,
          children: <Widget>[
            Container(
              child: Stack(
                children: <Widget>[
                  LivePlayerView(
                    url: 'rtmp://58.200.131.2:1935/livetv/hunantv',
                    shouldAutoPlayer: true,
                    callback: _onLivePlayerController,
                  ),
                  Container(
                    alignment: Alignment.center,
                    child: new Text("我是flutter控件，没有被遮挡~"),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(
                  left: 45.0, right: 45.0, top: 0.0, bottom: 50.0),
              child: new Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  FloatingActionButton(
                    onPressed: () {
                      _livePlayerController.play();
                    },
                    child: new Text("Start"),
                  ),
                  FloatingActionButton(
                    onPressed: () {
                      _livePlayerController.shutdown();
                    },
                    child: new Text("Stop"),
                  )
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
