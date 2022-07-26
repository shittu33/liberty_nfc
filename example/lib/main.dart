import 'dart:developer';

import 'package:flutter/material.dart';

import 'package:liberty_nfc/liberty_nfc.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var result = '';
  var nfcApi = NfcApi();

  @override
  void initState() {
    super.initState();
    log("initState");
    nfcApi.scanNfcTag().then((value) {
      setState(() {
        result += "Card number: ${value.cardNumber}\n";
        result += "Card type: ${value.cardType}\n";
        result +=
            "Card date:  ${value.expiryDate?.month}/${value.expiryDate?.year}\n";
      });
      print('RESULT READY');
      print("Card number: ${value.cardNumber}");
      print("Card type: ${value.cardType}");
      print("Card date: ${value.expiryDate?.month}/${value.expiryDate?.year}");
    });
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    NfcApi().stopScan();
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(result),
        ),
      ),
    );
  }
}
