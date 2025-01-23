package com.aravind.seekhoapp.interfaces


import com.aravind.seekhoapp.configs.AppContainerModule
import com.aravind.seekhoapp.configs.ApplicationModule
import com.aravind.seekhoapp.configs.NetworkModule
import com.aravind.seekhoapp.helper.CommonMethods
import com.aravind.seekhoapp.helper.SessionManager
import com.aravind.seekhoapp.network.ApiExceptionHandler
import com.aravind.seekhoapp.repository.CommonRepository
import com.aravind.seekhoapp.view.BaseActivity
import com.aravind.seekhoapp.viewmodel.CommonViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, ApplicationModule::class, AppContainerModule::class])
interface AppComponent {

    fun inject(baseActivity: BaseActivity)

    fun inject(commonMethods: CommonMethods)

    fun inject(sessionManager: SessionManager)

    fun inject(commonRepository: CommonRepository)

    fun inject(commonViewModel: CommonViewModel)

    fun inject(apiExceptionHandler: ApiExceptionHandler)


}