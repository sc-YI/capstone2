package com.example.capstone.data

data class Menu(
    val `data`: Data
) {
    data class Data(
        val foodListDtoList: List<FoodListDto>,
        val foodOrigin: String,
        val name: String,
        val phoneNumber: String
    )
    {
        data class FoodListDto(
            val id: Int?,
            val name: String,
            val price: String,
            val introduce: String,
            val status: String,
        )
    }
}