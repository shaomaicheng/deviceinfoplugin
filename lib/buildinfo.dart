import 'package:json_annotation/json_annotation.dart';

part 'buildinfo.g.dart';

@JsonSerializable()
class Build {
  String device;
  String display;
  String hardware;
  String host;
  String id;
  String type;
  String user;
  String unknow;
  Version version;

  Build(this.device, this.display, this.hardware, this.host, this.id, this.type,
      this.user, this.unknow, this.version);

  factory Build.fromJson(Map<String, dynamic> json) => _$BuildFromJson(json);

  Map<String, dynamic> toJson() => _$BuildToJson(this);

}

@JsonSerializable()
class Version {
  String release;
  String sdk;
  int sdkInt;
  String codeName;

  Version(this.release, this.sdk, this.sdkInt, this.codeName);

  factory Version.fromJson(Map<String, dynamic> json) => _$VersionFromJson(json);

  Map<String, dynamic> toJson() => _$VersionToJson(this);

}