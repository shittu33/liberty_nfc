import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:liberty_nfc/liberty_nfc.dart';

void main() {
  const MethodChannel channel = MethodChannel('liberty_nfc');

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
    expect(await LibertyNfc.platformVersion, '42');
  });
}
