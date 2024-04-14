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
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class UserProfileActivity : BaseActivity<ActivityUserProfileBinding>() {

    override val screenBinding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        screenBinding.btnChangeImage.setOnClickListener {
            startActivity(Intent(this, ChangeImageActivity::class.java))
        }


        setContentView(screenBinding.root)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = LanguageApplication.supabaseClient.auth.currentUserOrNull()
            val id = user?.id ?: ""
            val userInfo = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                filter { eq("id", id) }
            }.decodeSingle<UserInfo>()

            screenBinding.tvUserWelcome.text = getString(R.string.profile_welcome, userInfo?.firstName)

            screenBinding.ivUserPhoto.load(userInfo?.userPhotoUrl) {
                fallback(R.drawable.default_user_photo)
                transformations(CircleCropTransformation())
            }
        }
    }

}