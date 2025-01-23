package com.aravind.seekhoapp.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.configs.AppController
import com.aravind.seekhoapp.helper.CommonMethods
import com.aravind.seekhoapp.helper.JsonResponse
import com.aravind.seekhoapp.helper.LogManager
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.view.SplashActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ApiExceptionHandler {
    var apiResponseHandler = ApiResponseHandler()


    @Inject
    lateinit var jsonResp: JsonResponse

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent!!.inject(this)

    }


    suspend fun exceptionHandler(response: Response<Any>?, context: Context): ApiResponseHandler {
        try {

            if (response != null) {
                if (response.isSuccessful && response.body() != null) {

                    println("JSON RESPONSE : " + gson.toJson(response!!.body()))
                    apiResponseHandler = ApiResponseHandler()
                    apiResponseHandler.isSuccess = true
                    apiResponseHandler.errorResonse = ""

                } else {
                    errorHandling(response, context)
                }

            } else {
                errorResponse(context.getString(R.string.internal_server_error))
            }


        } catch (e: HttpException) {
            errorResponse(e.message!!)

        } catch (e: Throwable) {
            errorResponse(e.message!!)
        }
        return apiResponseHandler
    }

    private fun errorHandling(response: Response<Any>, context: Context) {
        if (response.errorBody() != null) {

            if (response.code() == 401){
                commonMethods.hideProgressDialog()
                Toast.makeText(context,context.getString(R.string.please_try_again),Toast.LENGTH_SHORT).show()
            }
            if (response.code() == 500){
                commonMethods.hideProgressDialog()
                Toast.makeText(context,context.getString(R.string.please_try_again), Toast.LENGTH_SHORT).show()
            }
            if (response.code() == 404) {

                sessionManager.clearAll()

                val intent = Intent(context, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                (context as Activity).finish()
                (context as Activity).finishAffinity()
                errorResponse(context.getString(R.string.internal_server_error))

            } else if (response.code() == 503) {

            } else {
                errorResponse(context.getString(R.string.internal_server_error))
            }

        } else {
            errorResponse(context.getString(R.string.internal_server_error))
        }
    }

    fun errorResponse(errorMessage: String) {
        apiResponseHandler = ApiResponseHandler()
        apiResponseHandler.isSuccess = false
        apiResponseHandler.errorResonse = errorMessage
    }

}