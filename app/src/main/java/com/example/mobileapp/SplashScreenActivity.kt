package com.example.mobileapp


import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.databinding.ActivitySplashScreenBinding
import com.example.mobileapp.login.LoginActivity
import com.example.mobileapp.main.MainActivity
import com.example.mobileapp.onboarding.OnboardingActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override val screenBinding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    private var currentFragment: Int = 0

    override fun onNetworkConnected() {
    }

    override fun onNetworkDisconnected() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        currentFragment = LanguageApplication.localStorage.getInt("OnboardingFragment")

        if (currentFragment != -1) {
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            finish()
        } else {
            lifecycleScope.launch {
                delay(1000)
                val hasSession = (application as LanguageApplication).hasSavedSession()
                if (hasSession) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}