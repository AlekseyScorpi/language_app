package com.example.mobileapp.exercises.animals

import com.example.mobileapp.exercises.GameManager

class AnimalGameManager : GameManager() {

    init {
        coef = 0.2
        streak = 0
    }

    override fun getPoints(correct: Boolean): Double {
        return if (correct) {
            streak++
            if (streak > 1) {
                1.0 + 0.2 * coef
            } else 1.0
        } else {
            streak = 0
            0.0
        }
    }

}