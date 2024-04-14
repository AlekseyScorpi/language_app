package com.example.mobileapp.user_profile

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivityUserProfileBinding
import com.example.mobileapp.language_select.LanguageSelectActivity
import com.example.mobileapp.login.LoginActivity
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class UserProfileActivity : BaseActivity<ActivityUserProfileBinding>() {

    override val screenBinding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedDataJson = intent.getStringExtra("UserInfo")

        val userInfo = receivedDataJson?.let { Json.decodeFromString<UserInfo>(it) }

        screenBinding.tvUserWelcome.text = getString(R.string.profile_welcome, userInfo?.firstName)

        screenBinding.ivUserPhoto.load(userInfo?.userPhotoUrl) {
            fallback(R.drawable.default_user_photo)
            transformations(CircleCropTransformation())
        }

        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        screenBinding.btnSwitchTheme.text = if (isDarkTheme) getString(R.string.profile_switch_to_light) else getString(R.string.profile_switch_to_dark)

        screenBinding.btnSwitchTheme.setOnClickListener {
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        screenBinding.btnChangeLanguage.setOnClickListener {
            val intent = Intent(this, LanguageSelectActivity::class.java)
            intent.putExtra("ProfileChange", true)
            startActivity(intent)
            recreate()
        }

        screenBinding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                LanguageApplication.supabaseClient.auth.clearSession()
                LanguageApplication.localStorage.saveString("SessionAccessToken", "")
                LanguageApplication.localStorage.saveString("SessionRefreshToken", "")
                val intent = Intent(this@UserProfileActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finishAffinity()
            }
        }


        setContentView(screenBinding.root)
    }
}