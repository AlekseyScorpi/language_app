package com.example.mobileapp.user_profile

import android.content.res.Configuration
import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.R
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivityUserProfileBinding
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


        setContentView(screenBinding.root)
    }
}