import 'package:json_annotation/json_annotation.dart';
part 'screeninfo.g.dart';

@JsonSerializable()
class ScreenDisplay {
  int width;
  int height;

  ScreenDisplay(this.width, this.height);

  factory ScreenDisplay.fromJson(Map<String, dynamic> json)  => _$ScreenDisplayFromJson(json);

  Map<String, dynamic> toJson() => _$ScreenDisplayToJson(this);

}