package com.example.mobileapp.exercises.multiplayer

data class WordMultiplayerItem (
    val word: String,
    var isSelected: Boolean = false,
    var isWrong: Boolean = false,
    var isCorrect: Boolean = false,
    var isOpponentAnswer: Boolean = false,
)