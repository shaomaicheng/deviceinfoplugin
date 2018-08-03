package io.clei.deviceinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

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
    private static final String IMEL = "getImel";

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
        } else if (call.method.equals(IMEL)) {
            result.success(imei());
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
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
