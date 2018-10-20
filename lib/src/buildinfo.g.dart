// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'buildinfo.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Build _$BuildFromJson(Map<String, dynamic> json) {
  return Build(
      json['device'] as String,
      json['display'] as String,
      json['hardware'] as String,
      json['host'] as String,
      json['id'] as String,
      json['type'] as String,
      json['user'] as String,
      json['unknow'] as String,
      json['version'] == null
          ? null
          : Version.fromJson(json['version'] as Map<String, dynamic>));
}

Map<String, dynamic> _$BuildToJson(Build instance) => <String, dynamic>{
      'device': instance.device,
      'display': instance.display,
      'hardware': instance.hardware,
      'host': instance.host,
      'id': instance.id,
      'type': instance.type,
      'user': instance.user,
      'unknow': instance.unknow,
      'version': instance.version
    };

Version _$VersionFromJson(Map<String, dynamic> json) {
  return Version(json['release'] as String, json['sdk'] as String,
      json['sdkInt'] as int, json['codeName'] as String);
}

Map<String, dynamic> _$VersionToJson(Version instance) => <String, dynamic>{
      'release': instance.release,
      'sdk': instance.sdk,
      'sdkInt': instance.sdkInt,
      'codeName': instance.codeName
    };
