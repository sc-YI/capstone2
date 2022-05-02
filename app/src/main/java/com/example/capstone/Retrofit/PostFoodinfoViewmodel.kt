package com.example.capstone.Retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.Login.MemberServiceCreator
import com.example.capstone.Retrofit.PostFoodinfoServiceCreator
import com.example.capstone.data.MemberInfo
import com.example.capstone.data.Menu
import com.example.capstone.data.MenuPostBody
import okhttp3.MultipartBody

class PostFoodinfoViewmodel: ViewModel()  {

    val foodClient : PostFoodinfoServiceCreator
    lateinit var foodpostContent : LiveData<Int>
    lateinit var foodpatchContent : LiveData<Int>

    lateinit var postedImagesId : LiveData<List<Int>>


    init {
        foodClient = PostFoodinfoServiceCreator()
    }


    fun postFood(token:String, foodBody: Menu.Data.FoodListDto){
        Log.d("food post", "실행")
        foodpostContent = foodClient.requestMenuInfo(token,foodBody)
    }

   fun patchFood(token:String, id:Int ,foodBody: Menu.Data.FoodListDto)
    {
        Log.d("food patch", "실행")
        foodpatchContent = foodClient.patchMenu(token,id,foodBody)
    }

    fun postFoodPhotos(token: String, id:Int, files:List<MultipartBody.Part>){
        postedImagesId = foodClient.postPhotos(token, id, files)
    }

    fun deleteMenu(token:String, id:Int){
      foodClient.deleteMenu(token, id)

    }
}