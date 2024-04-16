package com.example.mobileapp.exercises.audition

import com.example.mobileapp.exercises.GameManager

class AuditionGameManager : GameManager() {

    init {
        coef = 2.0
        streak = 0
    }

    override fun getPoints(): Double {
        streak++
        return if (streak > 1) {
            1.0 + coef * streak
        } else 1.0
    }
}