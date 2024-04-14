package com.example.mobileapp.exercises.words

import com.example.mobileapp.exercises.GameManager

class WordGameManager : GameManager() {
    init {
        coef = 0.2
        streak = 0
    }

    override fun getPoints(): Double {
        streak++
        return if (streak > 1) {
            1.0 + coef * streak
        } else 1.0
    }
}