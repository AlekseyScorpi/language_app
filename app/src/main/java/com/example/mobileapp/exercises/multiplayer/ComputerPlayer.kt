package com.example.mobileapp.exercises.multiplayer

import kotlin.random.Random

class ComputerPlayer {
    fun chooseWordIndex(wordItems: List<WordMultiplayerItem>, correctWord: String): Int {
        val words = mutableListOf<String>()
        wordItems.forEach {
            words.add(it.word)
        }
        return if (Random.nextInt(100) < 20) {
            words.indexOf(correctWord)
        } else {
            val remainingIndices = words.indices.filter { words[it] != correctWord }
            remainingIndices[Random.nextInt(remainingIndices.size)]
        }
    }
}