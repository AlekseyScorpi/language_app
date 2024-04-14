package com.example.mobileapp.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Animal (
    val id: Int,
    @SerialName ("image_url")
    val imageUrl: String,
    @SerialName ("correct_answer")
    val correctAnswer: String,
)