package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityNoConnectionBinding

class NoConnectionActivity : AppCompatActivity() {

    private val noConnectionBinding: ActivityNoConnectionBinding by lazy {
        ActivityNoConnectionBinding.inflate(layoutInflater)
    }

    private val networkManager: NetworkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(noConnectionBinding.root)

        noConnectionBinding.btnCheckConnection.setOnClickListener {
            if (networkManager.isInternetAvailable(this)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}