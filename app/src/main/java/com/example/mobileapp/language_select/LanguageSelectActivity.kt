package com.example.mobileapp.language_select

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.login.LoginActivity
import com.example.mobileapp.databinding.ActivityLanguageSelectBinding
import com.example.mobileapp.setLocale

class LanguageSelectActivity : BaseActivity<ActivityLanguageSelectBinding>() {

   override val screenBinding: ActivityLanguageSelectBinding by lazy {
        ActivityLanguageSelectBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val itemList = ArrayList<LanguageItem>()
        itemList.add(LanguageItem("Russian"))
        itemList.add(LanguageItem("English"))
        itemList.add(LanguageItem("Poland"))
        itemList.add(LanguageItem("German"))
        itemList.add(LanguageItem("Chinese"))

        screenBinding.rvLanguageButtons.layoutManager = LinearLayoutManager(this)
        screenBinding.rvLanguageButtons.adapter = LanguageSelectRvAdapter(itemList) { position ->
            itemList.forEachIndexed { index, item ->
                item.isSelectActivity = index == position
            }
            screenBinding.rvLanguageButtons.adapter?.notifyDataSetChanged()
        }

        screenBinding.btnChooseLanguage.setOnClickListener {
            var id = -1
            var name = ""
            itemList.forEachIndexed { index, item ->
                if (item.isSelectActivity) {
                    name = item.name
                    id = index
                }
            }
            if (id != -1) {
                val selectedLocale = when (name) {
                    "Russian" -> "ru-RU"
                    "English" -> "en-US"
                    //"Poland" -> "pl"
                    //"German" -> "de"
                    //"Chinese" -> "zh"
                    else -> "en-US"
                }
                setLocale(selectedLocale, this)
                recreate()

                val isProfileChange = intent.getBooleanExtra("ProfileChange", false)

                if (!isProfileChange) startActivity(Intent(this, LoginActivity::class.java))

                finish()
            }
        }

    }

}