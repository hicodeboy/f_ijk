import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:f_ijk/f_ijk.dart';

void main() {
  const MethodChannel channel = MethodChannel('f_ijk');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FIjk.platformVersion, '42');
  });
}
