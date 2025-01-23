package com.aravind.seekhoapp.helper

import android.content.SharedPreferences
import com.aravind.seekhoapp.configs.AppController
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SessionManager {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    init {
        AppController.appComponent!!.inject(this)
    }

    var appName: String?
        get() = sharedPreferences!!.getString("appName", "")
        set(appName) = sharedPreferences!!.edit().putString("appName", appName).apply()


    fun clearAll() {
        sharedPreferences!!.edit().clear().apply()
    }
}