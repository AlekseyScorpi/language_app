package com.example.mobileapp.main

import android.content.Intent
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
import com.example.mobileapp.exercises.animals.AnimalExerciseActivity
import com.example.mobileapp.exercises.audition.AuditionExerciseActivity
import com.example.mobileapp.exercises.multiplayer.MultiplayerActivity
import com.example.mobileapp.exercises.words.WordExerciseActivity
import com.example.mobileapp.user_profile.UserProfileActivity
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val screenBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var userItems: MutableList<UserItem> = mutableListOf()

    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)

        screenBinding.clAnimal.setOnClickListener {
            val intent = Intent(this@MainActivity, AnimalExerciseActivity::class.java)
            startActivity(intent)
        }

        screenBinding.clWord.setOnClickListener {
            val intent = Intent(this@MainActivity, WordExerciseActivity::class.java)
            startActivity(intent)
        }

        screenBinding.clMultiplayer.setOnClickListener {
            val intent = Intent(this@MainActivity, MultiplayerActivity::class.java)
            startActivity(intent)
        }

        screenBinding.clAudition.setOnClickListener {
            val intent = Intent(this@MainActivity, AuditionExerciseActivity::class.java)
            startActivity(intent)
        }

        screenBinding.ivUserPhoto.setOnClickListener {
            val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        screenBinding.rvLeaderBoard.layoutManager = LinearLayoutManager(this@MainActivity)
        screenBinding.rvLeaderBoard.adapter = LeaderBoardRvAdapter(userItems)
    }

    override fun onStart() {
        super.onStart()
        if (!isShouldStart) return
        lifecycleScope.launch {
            val user = LanguageApplication.supabaseClient.auth.currentUserOrNull()
            val id = user?.id ?: ""
            userInfo = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                filter { eq("id", id) }
            }.decodeSingle<UserInfo>()

            screenBinding.tvUserWelcome.text = getString(R.string.main_activity_welcome, userInfo.firstName)

            screenBinding.ivUserPhoto.load(userInfo.userPhotoUrl) {
                fallback(R.drawable.default_user_photo)
                transformations(CircleCropTransformation())
            }


            val topUsers = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
                order("points", Order.DESCENDING)
                range(0, 2)
            }.decodeList<UserInfo>()

            userItems.clear()

            topUsers.forEach { it ->
                userItems.add(UserItem(it.userPhotoUrl, it.firstName, it.secondName, it.points))
            }

            screenBinding.rvLeaderBoard.adapter?.notifyDataSetChanged()
        }
    }

}