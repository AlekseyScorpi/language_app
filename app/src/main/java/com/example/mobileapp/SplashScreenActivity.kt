package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.mobileapp.databinding.ActivitySplashScreenBinding
import com.example.mobileapp.language_select.LanguageSelectActivity
import com.example.mobileapp.onboarding.OnboardingActivity


class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val screenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val savedLanguage = loadLanguagePreference(this)
        savedLanguage?.let { language ->
            setLocale(language, this)
        }

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        currentFragment = sharedPref.getInt("OnboardingFragment", 0)

        if (currentFragment != -1) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        else {
            startActivity(Intent(this, LanguageSelectActivity::class.java))
        }
        finish()
    }

    private fun loadLanguagePreference(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("language", null)
    }
}