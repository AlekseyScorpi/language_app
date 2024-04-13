package com.example.mobileapp.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo (
    val id: String,
    @SerialName ("first_name")
    val firstName: String,
    @SerialName ("second_name")
    val secondName: String,
    @SerialName ("user_name")
    val userEmail: String,
)