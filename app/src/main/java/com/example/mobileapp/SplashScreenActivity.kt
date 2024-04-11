package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.example.mobileapp.databinding.ActivitySplashScreenBinding
import com.example.mobileapp.onboarding.OnboardingActivity


class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val screenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val savedLanguage = loadLanguagePreference(this)
        savedLanguage?.let { language ->
            setLocale(language, this)
        }

        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }

    private fun loadLanguagePreference(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("language", null)
    }
}