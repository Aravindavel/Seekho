package com.aravind.seekhoapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.configs.AppController
import com.aravind.seekhoapp.helper.CommonMethods
import com.aravind.seekhoapp.helper.Enums.REQ_ANIME_DETAILS
import com.aravind.seekhoapp.helper.Enums.REQ_ANIME
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.interfaces.ApiService
import com.aravind.seekhoapp.network.ApiExceptionHandler
import com.aravind.seekhoapp.network.ApiResponseHandler
import com.aravind.seekhoapp.network.NetworkResult
import com.aravind.seekhoapp.repository.CommonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class CommonViewModel : ViewModel() {
    var scope = CoroutineScope(Dispatchers.Default)
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var repository: CommonRepository

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiExceptionHandler: ApiExceptionHandler

    var apiResponseHandler: ApiResponseHandler? = null

    var responseCode: Int? = null
    var liveDataResponse = MutableLiveData<NetworkResult<Any>>()

    @Inject
    lateinit var context: Context

    init {
        AppController.appComponent?.inject(this)
    }

    fun apiRequest(
        requestCode: Int,
        hashMap: HashMap<String, String>,
        requestBody: RequestBody?,
    ) {
        if (commonMethods.isOnline(context)) {
            liveDataResponse.value = NetworkResult.Loading<Any>(true, requestCode = requestCode)
            scope.launch(Dispatchers.IO) {

                val response: Response<out Any> = when (requestCode) {

                    REQ_ANIME -> {
                        repository.anime(hashMap)
                    }

                    REQ_ANIME_DETAILS -> {
                        repository.animeDetails(hashMap)
                    }

                    else -> {
                        return@launch
                    }
                }

                withContext(Dispatchers.Main) {
                    apiResponseHandler =
                        apiExceptionHandler.exceptionHandler(response as Response<Any>, context)
                    responseCode = requestCode

                    if (apiResponseHandler!!.isSuccess) {
                        liveDataResponse.value = response!!.body()
                            ?.let { NetworkResult.Success<Any>(it, requestCode) }


                    } else {
                        liveDataResponse.value =
                            NetworkResult.Error(
                                apiResponseHandler!!.errorResonse,
                                data = response.body(),
                                requestCode = requestCode
                            )
                    }

                    liveDataResponse.value =
                        NetworkResult.Loading<Any>(false, requestCode = requestCode)

                }
            }
        } else {
            Toast.makeText(context,context.resources.getString(R.string.please_check_internet_connection),Toast.LENGTH_SHORT).show()
            android.os.Handler().postDelayed({
                liveDataResponse.value =
                    NetworkResult.Error(
                        context.getString(R.string.please_check_internet_connection),
                        requestCode = requestCode
                    )

            }, 1000)

        }
    }



}