package com.aravind.seekhoapp.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.configs.AppController
import org.json.JSONObject
import java.io.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CommonMethods {

    internal var mProgressDialog: Dialog? = null

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent!!.inject(this)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI_AWARE")
                    return true
                }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_VPN")
                    return true
                }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_LOWPAN")
                    return true
                }
            }
        }
        return false
    }

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }
    fun getJsonValue(jsonString: String, key: String, `object`: Any): Any {
        var object1 = `object`
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.has(key)) object1 = jsonObject.get(key)
        } catch (e: Exception) {
            e.printStackTrace()
            return Any()
        }

        return object1
    }



    fun showToast(activity: Context,message: String){
        if (message.isNotEmpty())
            Toast.makeText(activity,message,Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(activity: Activity,message: String){
        if (message.isNotEmpty()) for (i in 0..7) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog?.dismiss()
        }
    }

    fun showProgressDialog(context: Context) {
        try {
            mProgressDialog = getDialog(context, R.layout.app_loader_view)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setCanceledOnTouchOutside(false)
            mProgressDialog!!.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK })
            if (!mProgressDialog!!.isShowing) {
                try {
                    mProgressDialog?.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getDialog(mContext: Context, mLayout: Int): Dialog {
        val mDialog = Dialog(mContext)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        mDialog.setContentView(mLayout)
        mDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        mDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        return mDialog

    }


    fun dynamicTextColor(context: Context, attributeSet: Int): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(attributeSet, value, true)
        return value.data
    }


}