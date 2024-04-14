package com.example.mobileapp.exercises.animals

import com.example.mobileapp.exercises.GameManager

class AnimalGameManager : GameManager() {

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