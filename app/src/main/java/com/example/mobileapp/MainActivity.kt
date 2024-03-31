package com.example.mobileapp


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityNoConnectionBinding
import com.example.mobileapp.databinding.ActivitySplashScreenBinding


class MainActivity : AppCompatActivity() {

    private val splashScreenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val networkManager: NetworkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(splashScreenBinding.root)


        if (!networkManager.isInternetAvailable(this)) {
            startActivity(Intent(this, NoConnectionActivity::class.java))
            finish()
        }
    }
}