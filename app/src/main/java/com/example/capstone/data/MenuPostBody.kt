package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class MenuPostBody(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: String,
    @SerializedName("introduce") val introduce: String,
    @SerializedName("status") val status: String
)


