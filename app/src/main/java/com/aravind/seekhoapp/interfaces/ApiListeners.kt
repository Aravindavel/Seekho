package com.aravind.seekhoapp.interfaces

import com.aravind.seekhoapp.network.NetworkResult


interface ApiListeners {
    fun onSuccess(networkResult: NetworkResult<Any>)
    fun onFailure(networkResult: NetworkResult<Any>)
    fun onLoading(networkResult: NetworkResult<Any>)
}

