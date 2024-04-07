package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val networkManager: NetworkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(splashScreenBinding.root)

        val savedLanguage = loadLanguagePreference(this)
        savedLanguage?.let { language ->
            setLocale(language, this)
        }

        val delayMillis: Long = 1500

        Handler().postDelayed({
            if (!networkManager.isInternetAvailable(this)) {
                startActivity(Intent(this, NoConnectionActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }, delayMillis)
    }

    private fun loadLanguagePreference(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("language", null)
    }
}