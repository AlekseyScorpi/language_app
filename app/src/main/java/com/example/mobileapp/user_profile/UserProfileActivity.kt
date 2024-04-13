package com.example.mobileapp.user_profile

import android.os.Bundle
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.databinding.ActivityUserProfileBinding

class UserProfileActivity : BaseActivity<ActivityUserProfileBinding>() {

    override val screenBinding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)
    }
}