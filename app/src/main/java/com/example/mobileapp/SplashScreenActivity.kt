package com.example.mobileapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.databinding.ActivitySplashScreenBinding
import com.example.mobileapp.language_select.LanguageSelectActivity
import com.example.mobileapp.main.MainActivity
import com.example.mobileapp.onboarding.OnboardingActivity
import kotlinx.coroutines.launch


class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val screenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val savedLanguage = loadLanguagePreference(this@SplashScreenActivity)
        setLocale(savedLanguage, this@SplashScreenActivity)

        currentFragment = LanguageApplication.localStorage.getInt("OnboardingFragment")

        if (currentFragment != -1) {
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
        } else {
            lifecycleScope.launch {
                val hasSession = (application as LanguageApplication).hasSavedSession()
                if (hasSession) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashScreenActivity, LanguageSelectActivity::class.java))
                }
            }
        }
        finish()
    }

    private fun loadLanguagePreference(context: Context): String {
        return LanguageApplication.localStorage.getString("language")
    }
}