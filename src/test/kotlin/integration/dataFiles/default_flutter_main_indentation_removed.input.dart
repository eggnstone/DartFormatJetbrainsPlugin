import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  String title = 'Kais Golf Guide';
  String title1 = 'Kais';
  String title2 = 'Golf';
  String title3 = 'Guide';
  String claim = 'verrückt nach Golf';
  String claim1 = 'verrückt';
  String claim2 = 'nach';
  String claim3 = 'Golf';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: SingleChildScrollView(
            child: Center(
                child: Column(children: [
      SizedBox(height: 16),
      Text('320 x 50 (mobile leaderboard).'),
      Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner320x50(center: true)),
      SizedBox(height: 16),
      Text('468 x 60 (banner)'),
      Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner468x60(center: true)),
      //Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner468x60(center: false)),
      SizedBox(height: 16),
      Text('728 x 90 (leaderboard banner)'),
      Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner728x90(center: true)),
      //Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner728x90(center: false)),
      SizedBox(height: 16),
      Text('250 x 250 (square)'),
      Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner250x250(center: true)),
      SizedBox(height: 16),
      Text('120 x 600 (skyscraper)'),
      Container(decoration: BoxDecoration(border: Border.all()), child: _createBanner120x600(center: true)),
      SizedBox(height: 16)
    ]))));
  }

  Widget _createBanner468x60({required bool center}) {
    TextStyle titleStyle = TextStyle(fontFamily: 'Lato', fontSize: 24);
    double subtitleFactor = 0.75;
    return Container(
        width: 468,
        height: 60,
        padding: const EdgeInsets.all(4),
        child: Row(children: [
          FittedBox(fit: BoxFit.contain, child: SvgPicture.asset('assets/images/logo_full_head.svg')),
          Expanded(child: Center(child: Row(mainAxisSize: MainAxisSize.min, crossAxisAlignment: center ? CrossAxisAlignment.center : CrossAxisAlignment.end, children: [Text(title, style: titleStyle), SizedBox(width: 16), Text('-', style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(width: 16), Text(claim, style: titleStyle, textScaleFactor: subtitleFactor)])))
        ]));
  }

  Widget _createBanner728x90({required bool center}) {
    TextStyle titleStyle = TextStyle(fontFamily: 'Lato', fontSize: 40);

    double subtitleFactor = 0.75;

    return Container(
        width: 728,
        height: 90,
        padding: const EdgeInsets.all(8),
        child: Row(children: [
          FittedBox(fit: BoxFit.contain, child: SvgPicture.asset('assets/images/logo_full_head.svg')),
          Expanded(child: Center(child: Row(mainAxisSize: MainAxisSize.min, crossAxisAlignment: center ? CrossAxisAlignment.center : CrossAxisAlignment.end, children: [Text(title, style: titleStyle), SizedBox(width: 16), Text('-', style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(width: 16), Text(claim, style: titleStyle, textScaleFactor: subtitleFactor)])))
        ]));
  }

  Widget _createBanner250x250({required bool center}) {
    TextStyle titleStyle = TextStyle(fontFamily: 'Lato', fontSize: 20);

    double subtitleFactor = 0.75;

    return Container(
        width: 250,
        height: 250,
        padding: const EdgeInsets.all(8),
        child: Row(children: [
          Expanded(flex: 5, child: FittedBox(fit: BoxFit.contain, child: SvgPicture.asset('assets/images/logo_full_head.svg'))),
          Expanded(flex: 4, child: Center(child: Column(mainAxisSize: MainAxisSize.min, crossAxisAlignment: center ? CrossAxisAlignment.center : CrossAxisAlignment.end, children: [Text(title1, style: titleStyle), SizedBox(height: 8), Text(title2, style: titleStyle), SizedBox(height: 8), Text(title3, style: titleStyle), SizedBox(height: 8), Text('-', style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 8), Text(claim1, style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 8), Text(claim2, style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 8), Text(claim3, style: titleStyle, textScaleFactor: subtitleFactor)])))
        ]));
  }

  Widget _createBanner120x600({required bool center}) {
    TextStyle titleStyle = TextStyle(fontFamily: 'Lato', fontSize: 24);

    double subtitleFactor = 0.75;

    return Container(
        width: 120,
        height: 600,
        padding: const EdgeInsets.all(8),
        child: Column(children: [
          FittedBox(fit: BoxFit.contain, child: SvgPicture.asset('assets/images/logo_full_head.svg')),
          Expanded(child: Center(child: Column(mainAxisSize: MainAxisSize.min, crossAxisAlignment: center ? CrossAxisAlignment.center : CrossAxisAlignment.end, children: [Text(title1, style: titleStyle), SizedBox(height: 16), Text(title2, style: titleStyle), SizedBox(height: 16), Text(title3, style: titleStyle), SizedBox(height: 16), Text('-', style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 16), Text(claim1, style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 16), Text(claim2, style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(height: 16), Text(claim3, style: titleStyle, textScaleFactor: subtitleFactor)])))
        ]));
  }

  Widget _createBanner320x50({required bool center}) {
    TextStyle titleStyle = TextStyle(fontFamily: 'Lato', fontSize: 16);

    double subtitleFactor = 0.75;
    double spacer = 8;

    return Container(
        width: 320,
        height: 50,
        padding: const EdgeInsets.all(8),
        child: Row(children: [
          FittedBox(fit: BoxFit.contain, child: SvgPicture.asset('assets/images/logo_full_head.svg')),
          Expanded(child: Center(child: Row(mainAxisSize: MainAxisSize.min, crossAxisAlignment: center ? CrossAxisAlignment.center : CrossAxisAlignment.end, children: [Text(title, style: titleStyle), SizedBox(width: spacer), Text('-', style: titleStyle, textScaleFactor: subtitleFactor), SizedBox(width: spacer), Text(claim, style: titleStyle, textScaleFactor: subtitleFactor)])))
        ]));
  }
}
