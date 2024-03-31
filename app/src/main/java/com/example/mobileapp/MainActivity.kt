package com.example.mobileapp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityNoConnectionBinding
import com.example.mobileapp.databinding.ActivitySplashScreenBinding


class MainActivity : AppCompatActivity() {
    private val noConnectionBinding: ActivityNoConnectionBinding by lazy {
        ActivityNoConnectionBinding.inflate(layoutInflater)
    }

    private val splashScreenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val networkManager: NetworkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(splashScreenBinding.root)

        noConnectionBinding.btnCheckConnection.setOnClickListener {
            if (networkManager.isInternetAvailable(this)) {
                setContentView(splashScreenBinding.root)
            }
        }

        if (!networkManager.isInternetAvailable(this)) {
            setContentView(noConnectionBinding.root)
        }
    }
}