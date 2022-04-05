package com.example.capstone.data

data class MenuContent(
    val foodListDtoList: Array<Menu.Data.FoodListDto>,
    val foodOrigin: String,
    val name: String,
    val phoneNumber: String
)
