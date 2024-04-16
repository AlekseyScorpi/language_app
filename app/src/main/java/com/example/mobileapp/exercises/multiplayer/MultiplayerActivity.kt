package com.example.mobileapp.exercises.multiplayer

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.LanguageApplication
import com.example.mobileapp.R
import com.example.mobileapp.database.WordExercise
import com.example.mobileapp.databinding.ActivityMultiplayerBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class MultiplayerActivity : BaseActivity<ActivityMultiplayerBinding>() {

    override val screenBinding: ActivityMultiplayerBinding by lazy {
        ActivityMultiplayerBinding.inflate(layoutInflater)
    }

    private var isNext = false

    private var wordList: MutableList<WordMultiplayerItem> = mutableListOf()

    private var correct: String = ""

    private val secondPlayer = ComputerPlayer()
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
                    val computerChoose = secondPlayer.chooseWordIndex(wordList, correct)

                    wordList.forEachIndexed { idx, wordItem ->
                        if (computerChoose == idx) {
                            wordItem.isOpponentAnswer = true
                        }
                        if (wordItem.word == correct) {
                            wordItem.isCorrect = true
                        }
                        if ((wordItem.isSelected || wordItem.isOpponentAnswer) && (wordItem.word != correct)) {
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
        screenBinding.rvWords.adapter = WordMultiplayerRvAdapter(wordList) { position ->
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
        wordList.add(WordMultiplayerItem(wordExercise.variant1))
        wordList.add(WordMultiplayerItem(wordExercise.variant2))
        wordList.add(WordMultiplayerItem(wordExercise.variant3))
        wordList.add(WordMultiplayerItem(wordExercise.variant4))
        correct = wordExercise.correct

        screenBinding.tvEnglishVariant.text = wordExercise.englishVariant
        screenBinding.tvTranscription.text = wordExercise.transcription

        screenBinding.rvWords.adapter?.notifyDataSetChanged()
    }
}