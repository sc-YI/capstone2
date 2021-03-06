package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class MemberInfo(
    @SerializedName("id") val id :Int,
    @SerializedName("nickName") val nickname: String,
    @SerializedName("email") val email: String,
)
