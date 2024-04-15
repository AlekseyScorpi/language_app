package com.example.mobileapp.exercises.words

import android.os.Bundle
import com.example.mobileapp.BaseActivity
import com.example.mobileapp.databinding.ActivityWordExerciseBinding

class WordExerciseActivity : BaseActivity<ActivityWordExerciseBinding>() {

    override val screenBinding: ActivityWordExerciseBinding by lazy {
        ActivityWordExerciseBinding.inflate(layoutInflater)
    }

    private val isNext = false

    private val wordList: MutableList<WordItem> = mutableListOf()

    private val gameManager = WordGameManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)
    }

    private fun bind() {
        screenBinding.btnAction.setOnClickListener {
            updateUI()
        }
    }

    private fun updateUI() {

    }

    private suspend fun getNewExercise() {

    }

    private suspend fun updatePoints() {

    }
}