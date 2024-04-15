package com.example.mobileapp.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordExercise (
    val id: Int,
    @SerialName("english_variant")
    val englishVariant: String,
    @SerialName("transcription")
    val transcription: String,
    @SerialName("variant_1")
    val variant1: String,
    @SerialName("variant_2")
    val variant2: String,
    @SerialName("variant_3")
    val variant3: String,
    @SerialName("variant_4")
    val variant4: String,
    @SerialName("correct")
    val correct: String,
)