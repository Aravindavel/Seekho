package com.aravind.seekhoapp.configs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.helper.CommonMethods
import com.aravind.seekhoapp.helper.JsonResponse
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.network.ApiExceptionHandler
import com.aravind.seekhoapp.repository.CommonRepository
import com.aravind.seekhoapp.viewmodel.CommonViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import java.util.ArrayList


@Module(includes = [ApplicationModule::class])
class AppContainerModule {
    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(application.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesSessionManager(): SessionManager {
        return SessionManager()
    }
    @Provides
    @Singleton
    fun providesCommonMethods(): CommonMethods {
        return CommonMethods()
    }

    @Provides
    @Singleton
    fun jsonResponse(): JsonResponse {
        return JsonResponse()
    }

    @Provides
    @Singleton
    fun repository(): CommonRepository {
        return CommonRepository()
    }


    @Provides
    @Singleton
    fun viewModel(): CommonViewModel {
        return CommonViewModel()
    }

    @Provides
    @Singleton
    fun apiExceptionHandler(): ApiExceptionHandler {
        return ApiExceptionHandler()
    }


    
    @Provides
    @Singleton
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesStringArrayList(): ArrayList<String> {
        return ArrayList()
    }

}