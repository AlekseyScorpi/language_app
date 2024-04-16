package com.example.mobileapp.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuditionExercise (
    val id: Int,
    @SerialName("word")
    val word: String,
    @SerialName("transcription")
    val transcription: String,
)