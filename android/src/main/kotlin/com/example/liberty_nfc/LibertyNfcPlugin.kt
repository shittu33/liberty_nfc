package com.example.liberty_nfc

import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import dev.flutter.nfc.Pigeon
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** LibertyNfcPlugin */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class LibertyNfcPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity

    private var nfcApi: NfcApi? = null
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        nfcApi = NfcApi()
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "liberty_nfc")
        Pigeon.NfcApi.setup(flutterPluginBinding.binaryMessenger, nfcApi)
        channel.setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        nfcApi?.init(binding.activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        nfcApi?.onResume()
    }

    override fun onDetachedFromActivity() {
        Log.d(ContentValues.TAG, "onDetachedFromActivity: Detached")
//        nfcApi?.onPause()
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        Log.d(ContentValues.TAG, "onDetachedFromEngine: Detached")
//
//        nfcApi?.onPause()
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.d(ContentValues.TAG, "onDetachedFromActivityForConfigChanges: Detached")
        nfcApi?.onPause()
    }

}
