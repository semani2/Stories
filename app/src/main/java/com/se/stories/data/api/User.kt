package com.se.stories.data.api

import com.google.gson.annotations.SerializedName

/**
 * Data model representing the user api model
 */
data class User(val name: String, val avatar: String, @SerializedName("fullname") val fullName: String)
