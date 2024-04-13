package com.example.mobileapp.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.databinding.ActivityMainBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val screenBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var userItems: MutableList<UserItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val user = LanguageApplication.supabaseClient.auth.currentUserOrNull()
            val id = user?.id ?: ""
            val userInfo = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                filter { eq("id", id) }
            }.decodeSingle<UserInfo>()

            screenBinding.tvUserWelcome.text = getString(R.string.main_activity_welcome, userInfo.firstName)

            screenBinding.ivUserPhoto.load(userInfo.userPhotoUrl) {
                fallback(R.drawable.default_user_photo)
                transformations(CircleCropTransformation())
            }

            val topUsers = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                order("points", Order.DESCENDING)
                range(0, 3)
            }.decodeList<UserInfo>()

            topUsers.forEach { it ->
                userItems.add(UserItem(it.userPhotoUrl, it.firstName, it.secondName, it.points))
            }

            screenBinding.rvLeaderBoard.layoutManager = LinearLayoutManager(this@MainActivity)
            screenBinding.rvLeaderBoard.adapter = LeaderBoardRvAdapter(userItems)


            setContentView(screenBinding.root)
        }
    }
}