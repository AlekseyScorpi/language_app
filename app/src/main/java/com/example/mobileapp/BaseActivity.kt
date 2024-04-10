package com.example.mobileapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.mobileapp.databinding.ActivityNoConnectionBinding

abstract class BaseActivity<T: ViewBinding> : AppCompatActivity(), NetworkStateListener {

    private val networkChangeReceiver = NetworkChangeReceiver()

    private var isInternetAvailable = true

    private val noConnectionBinding: ActivityNoConnectionBinding by lazy {
        ActivityNoConnectionBinding.inflate(layoutInflater)
    }

    protected abstract val screenBinding:T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkChangeReceiver.addListener(this)
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        noConnectionBinding.btnCheckConnection.setOnClickListener {
            if (isInternetAvailable) setContentView(screenBinding.root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onNetworkConnected() {
        isInternetAvailable = true
    }

    override fun onNetworkDisconnected() {
        isInternetAvailable = false
        setContentView(noConnectionBinding.root)
    }

}