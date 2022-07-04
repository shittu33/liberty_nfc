package com.example.liberty_nfc.nfc_engine

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

object ActivityUtil {
    fun showToast(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun enableNfcForegroundDispatch(activity: Activity, mNfcAdapter: NfcAdapter) {
        try {
            val intent = Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent =
                PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            mNfcAdapter.enableForegroundDispatch(activity, nfcPendingIntent, null, null)
        } catch (ex: IllegalStateException) {
            Log.e("getTag", "Error enabling NFC foreground dispatch", ex)
        }
    }

    fun disableNfcForegroundDispatch(activity: Activity, mNfcAdapter: NfcAdapter) {
        try {
            mNfcAdapter.disableForegroundDispatch(activity)
        } catch (ex: IllegalStateException) {
            Log.e("mNfcAdapter", "Error disabling NFC foreground dispatch", ex)
        }
    }

}

