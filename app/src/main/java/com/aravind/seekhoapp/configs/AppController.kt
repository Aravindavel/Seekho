package com.aravind.seekhoapp.configs

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.interfaces.AppComponent
import com.aravind.seekhoapp.interfaces.DaggerAppComponent
import java.util.*


class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
            .networkModule(NetworkModule(resources.getString(R.string.apiBaseUrl),resources.getString(R.string.domain))).build()
        instance = this
    }



    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        var appComponent: AppComponent? = null
            private set
        private val APP_LOCALES = mutableListOf(Locale.ENGLISH, Locale.US)
        private var instance: AppController? = null
        val contexts: Context
            get() = instance!!.applicationContext
    }
}