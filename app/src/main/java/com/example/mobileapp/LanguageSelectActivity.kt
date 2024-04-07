package com.example.mobileapp

import RvAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.databinding.ActivityLanguageSelectBinding

class LanguageSelectActivity : AppCompatActivity() {

    private val languageSelectBinding: ActivityLanguageSelectBinding by lazy {
        ActivityLanguageSelectBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(languageSelectBinding.root)

        val itemList = ArrayList<LanguageItem>()
        itemList.add(LanguageItem("Russian"))
        itemList.add(LanguageItem("English"))
        itemList.add(LanguageItem("Poland"))
        itemList.add(LanguageItem("German"))
        itemList.add(LanguageItem("Chinese"))

        languageSelectBinding.rvLanguageButtons.layoutManager = LinearLayoutManager(this)
        languageSelectBinding.rvLanguageButtons.adapter = RvAdapter(itemList) { position ->
            itemList.forEachIndexed { index, item ->
                item.isSelectActivity = index == position
            }
            languageSelectBinding.rvLanguageButtons.adapter?.notifyDataSetChanged()
        }

        languageSelectBinding.btnChooseLanguage.setOnClickListener {
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
                    "Russian" -> "ru"
                    "English" -> "en"
                    //"Poland" -> "pl"
                    //"German" -> "de"
                    //"Chinese" -> "zh"
                    else -> "en"
                }
                setLocale(selectedLocale, this)
                recreate()
            }
        }

    }

}