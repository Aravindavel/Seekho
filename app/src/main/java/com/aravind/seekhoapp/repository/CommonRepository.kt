package com.aravind.seekhoapp.repository


import com.aravind.seekhoapp.configs.AppController
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.interfaces.ApiService
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class CommonRepository {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var sessionManager: SessionManager


    init {
        AppController.appComponent!!.inject(this)
    }


    suspend fun anime(hashMap: HashMap<String, String>): Response<Any> {

        return apiService.anime(hashMap) as Response<Any>
    }

    suspend fun animeDetails(hashMap: HashMap<String, String>): Response<Any> {

        return apiService.animeDetails(hashMap) as Response<Any>
    }

}