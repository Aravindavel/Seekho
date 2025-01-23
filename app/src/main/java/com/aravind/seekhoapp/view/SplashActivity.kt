package com.aravind.seekhoapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.databinding.ActivitySplashBinding
import com.aravind.seekhoapp.network.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    override fun onSuccess(networkResult: NetworkResult<Any>) {
        
    }

    override fun onFailure(networkResult: NetworkResult<Any>) {
        
    }

    override fun onLoading(networkResult: NetworkResult<Any>) {
        
    }
}