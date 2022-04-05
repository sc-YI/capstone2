package com.example.capstone.Retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.Login.MemberServiceCreator
import com.example.capstone.Retrofit.PostFoodinfoServiceCreator
import com.example.capstone.data.MemberInfo
import com.example.capstone.data.Menu
import com.example.capstone.data.MenuPostBody

class PostFoodinfoViewmodel: ViewModel()  {

    val foodClient : PostFoodinfoServiceCreator

    lateinit var foodId : LiveData<Menu.Data.FoodListDto>
    lateinit var foodContent : LiveData<Menu.Data.FoodListDto>




    init {
        foodClient = PostFoodinfoServiceCreator()
    }


    fun postFood(token:String, id:Int, foodBody: Menu.Data.FoodListDto){
        Log.d("food post", "실행")
        foodContent = foodClient.requestMenuInfo(token,id,foodBody)
    }

    fun deleteMenu(token:String, id:Int){
      foodClient.deleteMenu(token, id)

    }
}