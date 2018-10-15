package io.clei.deviceinfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
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

private val DEVICE_INFO_SP_KEY = "device_info_sp_key"
private val UUID_SP_KEY = "device_spkey_uuid"

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
            call.method == IMEI -> result.success(imei())
            // uuid
            call.method == GET_UUID -> result.success(deviceId(registrar.context()))
            // 屏幕宽高
            call.method == GET_DEVICE_SCREEN -> result.success(getDeviceScreen())
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
     * 获取手机 imei值
     * 需要申请权限
     */
    private fun imei(): String {
        val context = registrar.context()
        if (Build.VERSION.SDK_INT >= 23) {
            val checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
            return if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                "this phone don't have READ_PHONE_STATE permission"
            } else {
                imei(context)
            }
        } else {
            return imei(context)
        }
    }

    /**
     * 获取 imei
     *
     */
    private fun imei(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    /**
     * 获取deviceid 实际为uuid值
     */
    private fun deviceId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(DEVICE_INFO_SP_KEY, Context.MODE_PRIVATE)
        var uuid = sharedPreferences.getString(UUID_SP_KEY, "")
        if (TextUtils.isEmpty(uuid)) {
            // 本地不存在，生成一个
            uuid = UUID.randomUUID().toString()
            val editor = context.getSharedPreferences(DEVICE_INFO_SP_KEY, Context.MODE_PRIVATE).edit()
            editor.putString(UUID_SP_KEY, uuid)
            editor.apply()
        }
        return uuid
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
