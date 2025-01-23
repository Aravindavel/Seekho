package com.aravind.seekhoapp.view

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.configs.AppController
import com.aravind.seekhoapp.helper.CommonMethods
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.interfaces.ApiListeners
import com.aravind.seekhoapp.interfaces.ApiService
import com.aravind.seekhoapp.network.NetworkResult
import com.aravind.seekhoapp.viewmodel.CommonViewModel
import javax.inject.Inject
import kotlin.collections.HashMap

abstract class BaseActivity : AppCompatActivity(), ApiListeners {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    var commonViewModel: CommonViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppController.appComponent!!.inject(this)
        setTheme(currentTheme)
        setTheme(currentFont)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimary)
        }

        commonViewModel?.liveDataResponse?.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success<*> -> {
                    onSuccess(it)
                }
                is NetworkResult.Error<*> -> {
                    onFailure(it)
                }
                is NetworkResult.Loading<*> -> {
                    onLoading(it)
                }
            }
        })

    }

    fun emptyHashMap() = HashMap<String, String>().apply {

    }



    class SafeClickListener(
        private var defaultInterval: Int = 1000,
        private val onSafeCLick: (View) -> Unit
    ) : View.OnClickListener {
        private var lastTimeClicked: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                return
            }
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeCLick(v)
        }
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }


    companion object {
        var currentTheme = R.style.Theme_Seekho
        var currentFont = R.style.font_1

        fun isDarkTheme(resources: Resources): Boolean {
            return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }
}