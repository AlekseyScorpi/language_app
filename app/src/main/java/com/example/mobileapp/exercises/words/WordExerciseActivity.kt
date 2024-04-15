package com.example.mobileapp.exercises.words

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.UserInfo
import com.example.mobileapp.database.WordExercise
import com.example.mobileapp.databinding.ActivityWordExerciseBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class WordExerciseActivity : BaseActivity<ActivityWordExerciseBinding>() {

    override val screenBinding: ActivityWordExerciseBinding by lazy {
        ActivityWordExerciseBinding.inflate(layoutInflater)
    }

    private var isNext = false

    private var wordList: MutableList<WordItem> = mutableListOf()

    private var correct: String = ""

    private val gameManager = WordGameManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            getNewExercise()
            bind()
        }
        setContentView(screenBinding.root)
    }

    private fun bind() {
        screenBinding.btnAction.setOnClickListener {
            lifecycleScope.launch {

                if (isNext) {
                    getNewExercise()
                    screenBinding.btnAction.setText(R.string.word_exercise_check)
                    isNext = false
                }
                else {
                    var noSkip = false
                    wordList.forEach { wordItem ->
                        noSkip = noSkip || wordItem.isSelected
                    }
                    if (!noSkip) return@launch

                    wordList.forEach { wordItem ->
                        if (wordItem.word == correct) {
                            if (wordItem.isSelected) updatePoints() else gameManager.resetStreak()
                            wordItem.isCorrect = true
                        }
                        if (wordItem.isSelected && (wordItem.word != correct)) {
                            wordItem.isWrong = true
                        }
                    }
                    isNext = true
                    screenBinding.btnAction.setText(R.string.word_exercise_next)
                    screenBinding.rvWords.adapter?.notifyDataSetChanged()
                }
            }
        }

        screenBinding.ivBack.setOnClickListener {
            finish()
        }

        screenBinding.rvWords.layoutManager = LinearLayoutManager(this)
        screenBinding.rvWords.adapter = WordsRvAdapter(wordList) { position ->
            wordList.forEachIndexed { index, item ->
                item.isSelected = index == position
            }
            screenBinding.rvWords.adapter?.notifyDataSetChanged()
        }
    }

    private suspend fun getNewExercise() {
        wordList.clear()
        val wordExercise = LanguageApplication.supabaseClient.postgrest.from("random_guess_word").select {
            limit(1)
        }.decodeSingle<WordExercise>()
        wordList.add(WordItem(wordExercise.variant1, false, false, false))
        wordList.add(WordItem(wordExercise.variant2, false, false, false))
        wordList.add(WordItem(wordExercise.variant3, false, false, false))
        wordList.add(WordItem(wordExercise.variant4, false, false, false))
        correct = wordExercise.correct

        screenBinding.tvEnglishVariant.text = wordExercise.englishVariant
        screenBinding.tvTranscription.text = wordExercise.transcription

        screenBinding.rvWords.adapter?.notifyDataSetChanged()
    }

    private suspend fun updatePoints() {
        val user = LanguageApplication.supabaseClient.auth.currentUserOrNull()
        val userInfo = LanguageApplication.supabaseClient.postgrest.from("user_info").select {
            filter { eq("id", user?.id ?: "") }
        }.decodeSingle<UserInfo>()

        val newPoints: Double = userInfo.points + gameManager.getPoints()

        LanguageApplication.supabaseClient.from("user_info").update(
            {
                set("points", newPoints)
            }
        ) {
            filter {
                eq("id", userInfo.id)
            }
        }
    }
}