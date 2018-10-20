package io.clei.deviceinfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.google.gson.Gson
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import org.json.JSONObject
import java.util.*

private val APP_VERSION_CODE = "getAppVersionCode"
private val APP_VERSION_NAME = "getAppVersionName"
private val DEVICE_VERSION_CODE = "getDeviceVersionCode"
private val IMEI = "getImei"
private val GET_UUID = "getUUID"
private val GET_DEVICE_SCREEN = "getdevicescreen"
private val GET_BUILD = "getBuild"
private val DEVICE_TOKEN = "getDeviceToken"


private val DEVICE_INFO_SP = "device_info_sp"
private val UUID_SP_KEY = "device_spkey_uuid"
private val DEVICE_TOKEN_KEY = "device_token_key"

/**
 * DeviceinfoPlugin
 */
class DeviceinfoPlugin(private val registrar: Registrar) : MethodCallHandler {


    private val versionCode: Int
        get() {
            val context = registrar.context()
            val packageManager = context.packageManager
            try {
                val info = packageManager.getPackageInfo(context.packageName, 0)
                return info.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return -1
        }

    private val versionName: String
        get() {
            val context = registrar.context()
            val packageManager = context.packageManager
            try {
                val info = packageManager.getPackageInfo(context.packageName, 0)
                return info.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return ""
        }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            // app版本号
            call.method == APP_VERSION_CODE -> result.success(versionCode)
            // app版本名称
            call.method == APP_VERSION_NAME -> result.success(versionName)
            // 习题版本
            call.method == DEVICE_VERSION_CODE -> result.success(deviceVersion())
            // imei号
            call.method == IMEI -> result.success(getImei(registrar.context()))
            // uuid
            call.method == GET_UUID -> result.success(deviceId(registrar.context()))
            // 屏幕宽高
            call.method == GET_DEVICE_SCREEN -> result.success(getDeviceScreen())
            // 获取build信息
            call.method == GET_BUILD -> result.success(getBuild())
            // deviceToken
            call.method == DEVICE_TOKEN -> result.success(getDeviceToken(registrar.context()))
            else -> result.notImplemented()
        }
    }

    /**
     * 获取屏幕宽高信息
     */
    private fun getDeviceScreen():String {
        val context = registrar.context()
        val display = context.resources.displayMetrics
        val screenInfo = JSONObject()
                .put("width", display.widthPixels)
                .put("height", display.heightPixels)
        return screenInfo.toString()
    }


    /**
     * 获取android 系统 sdkint
     */
    private fun deviceVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取deviceid 实际为uuid值
     */
    private fun deviceId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(DEVICE_INFO_SP, Context.MODE_PRIVATE)
        var uuid = sharedPreferences.getString(UUID_SP_KEY, "")
        if (TextUtils.isEmpty(uuid)) {
            // 本地不存在，生成一个
            uuid = UUID.randomUUID().toString()
            val editor = context.getSharedPreferences(DEVICE_INFO_SP, Context.MODE_PRIVATE).edit()
            editor.putString(UUID_SP_KEY, uuid)
            editor.apply()
        }
        return uuid
    }

    private fun getBuild(): String {
        val build = io.clei.deviceinfo.Build(
                Build.DEVICE,
                Build.DISPLAY,
                Build.HARDWARE,
                Build.HOST,
                Build.ID,
                Build.TYPE,
                Build.USER,
                Build.UNKNOWN,
                Version(
                        Build.VERSION.RELEASE,
                        Build.VERSION.SDK,
                        Build.VERSION.SDK_INT,
                        Build.VERSION.CODENAME
                )
        )
        return Gson().toJson(build)
    }


    private fun getDeviceToken(context: Context): String {
        var deviceToken = context.getSharedPreferences(DEVICE_INFO_SP, Context.MODE_PRIVATE)
                .getString(DEVICE_TOKEN_KEY, null)
        if (TextUtils.isEmpty(deviceToken)) {
            val androidId = getAndroidId(context)
            val imei = getImei(context)
            val imsi = getImsi(context)

            var firstId = ""
            var secondId = ""
            if (!TextUtils.isEmpty(androidId)) {
                firstId = androidId
            } else if (!TextUtils.isEmpty(imsi)) {
                firstId = imsi
            }

            var fullId = ""

            if (!TextUtils.isEmpty(imei)) {
                secondId = imei
            } else if (!TextUtils.isEmpty(androidId)) {
                fullId = getWifiMacAddress(context)
                if (!TextUtils.isEmpty(fullId)) {
                    secondId = fullId
                }
            }

            var uuid = ""

            if (!TextUtils.isEmpty(firstId) || !TextUtils.isEmpty(secondId)) {
                fullId = firstId + secondId


                try {

                    uuid = UUID.nameUUIDFromBytes((fullId as java.lang.String).getBytes("utf8")).toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (TextUtils.isEmpty(uuid)) {
                uuid = UUID.randomUUID().toString()
            }

            context.getSharedPreferences(DEVICE_INFO_SP, Context.MODE_PRIVATE)
                    .edit()
                    .putString(DEVICE_TOKEN_KEY, uuid)
                    .apply()

            return uuid
        } else {
            return deviceToken
        }
    }

    private fun getAndroidId(context: Context):String {
        try {
            val androidId = Settings.Secure.getString(context.contentResolver, "android_id")
            if ("9774d56d682e549c" != androidId) {
                return androidId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getImei(context: Context) :String {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return ""
            }
            var deviceId = tm.deviceId
            if ("000000000000000" != deviceId) {
                return deviceId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getImsi(context: Context) : String {
        try {
            val tm =context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != 0)
                "" else tm.subscriberId ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getWifiMacAddress(context: Context):String {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_WIFI_STATE) != 0)
                "" else wifiManager.connectionInfo.macAddress ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {

        /**
         * Plugin registration.
         */
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "deviceinfo")
            channel.setMethodCallHandler(DeviceinfoPlugin(registrar))
        }
    }
}

data class Build(
        val device:String,
        val display: String,
        val hardware: String,
        val host: String,
        val id: String,
        val type:String,
        val user: String,
        val unknow:String,
        val version:Version
)

data class Version(
        val release: String,
        val sdk: String,
        val sdkInt: Int,
        val codeName: String
)