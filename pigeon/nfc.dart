import 'package:pigeon/pigeon.dart';

class NfcResult {
  String? cardNumber;
  Date? expiryDate;
  String? aid;
  String? cardType;
}

class Date{
  String? month;
  String? year;
}

@HostApi()
abstract class NfcApi {
  @async
  NfcResult scanNfcTag();

  @async
  void stopScan();
}
