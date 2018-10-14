package io.clei.deviceinfo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * DeviceinfoPlugin
 */
public class DeviceinfoPlugin implements MethodCallHandler {


    private static final String APP_VERSION_CODE = "getAppVersionCode";
    private static final String APP_VERSION_NAME = "getAppVersionName";
    private static final String DEVICE_VERSION_CODE = "getDeviceVersionCode";
    private static final String IMEI = "getImei";
    private static final String GET_UUID = "getUUID";

    private static final String DEVICE_INFO_SP_KEY = "device_info_sp_key";
    private static final String UUID_SP_KEY = "device_spkey_uuid";

    private Registrar registrar;

    public DeviceinfoPlugin(Registrar registrar) {
        this.registrar = registrar;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "deviceinfo");
        channel.setMethodCallHandler(new DeviceinfoPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals(APP_VERSION_CODE)) {
            result.success(getVersionCode());
        } else if (call.method.equals(APP_VERSION_NAME)) {
            result.success(getVersionName());
        } else if (call.method.equals(DEVICE_VERSION_CODE)) {
            result.success(deviceVersion());
        } else if (call.method.equals(IMEI)) {
            result.success(imei());
        } else if (call.method.equals(GET_UUID)) {
            result.success(deviceId(registrar.context()));
        } else {
            result.notImplemented();
        }
    }


    private int getVersionCode() {
        Context context = registrar.context();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getVersionName() {
        Context context = registrar.context();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int deviceVersion() {
        return Build.VERSION.SDK_INT;
    }

    private String imei() {
        Context context = registrar.context();
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                return "this phone don't have READ_PHONE_STATE permission";
            } else {
                return imei(context);
            }
        } else {
            return imei(context);
        }
    }

    private String imei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

    private String deviceId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEVICE_INFO_SP_KEY, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(UUID_SP_KEY, "");
        if (TextUtils.isEmpty(uuid)) {
            // 本地不存在，生成一个
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = context.getSharedPreferences(DEVICE_INFO_SP_KEY, Context.MODE_PRIVATE).edit();
            editor.putString(UUID_SP_KEY, uuid);
            editor.apply();
        }
        return uuid;
    }
}
