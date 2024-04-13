package com.example.mobileapp.database

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo (
    val id: String,
    val first_name: String,
    val second_name: String,
    val user_email: String,
)