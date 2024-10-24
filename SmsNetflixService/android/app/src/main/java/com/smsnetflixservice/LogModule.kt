package com.smsnetflixservice

import android.content.Context
import android.content.SharedPreferences
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class LogModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "LogModule"
    }

    @ReactMethod
    fun getLogs(promise: Promise) {
        try {
            val prefs: SharedPreferences = reactApplicationContext.getSharedPreferences("sms_logs", Context.MODE_PRIVATE)
            val logs = prefs.getString("logs", "")
            promise.resolve(logs)
        } catch (e: Exception) {
            promise.reject("Error", e)
        }
    }

    @ReactMethod
    fun clearLogs(promise: Promise) {
        try {
            val prefs: SharedPreferences = reactApplicationContext.getSharedPreferences("sms_logs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
            promise.resolve(null)
        } catch (e: Exception) {
            promise.reject("Error", e)
        }
    }
}
