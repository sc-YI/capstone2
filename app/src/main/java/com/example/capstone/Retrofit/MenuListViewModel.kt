package com.example.capstone.Retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.Menu

class MenuListViewModel : ViewModel() {
    val client : StoreContentServiceCreator

    lateinit var MenuList : LiveData<Menu.Data>

    init {
        client = StoreContentServiceCreator()
    }

    fun getStoreInfo(token:String){
        MenuList = client.getMentContent(token)
    }
}