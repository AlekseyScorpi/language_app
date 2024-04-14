package com.example.mobileapp.exercises

abstract class GameManager {
    protected var streak: Int = 0
    protected var coef: Double = 0.2
    abstract fun getPoints(correct: Boolean): Double
}