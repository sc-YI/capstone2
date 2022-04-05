package com.example.capstone.Retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.data.MemberResponse
import com.example.capstone.data.Menu
import com.example.capstone.data.MenuPostBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PostFoodinfoServiceCreator {

    private val BASE_URL = "https://473d-125-180-55-163.ngrok.io/"
    private val client : RetrofitMenu

    val httpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    init {
        client = retrofit.create(RetrofitMenu::class.java)
    }

    fun getMentContent(id:Int) : LiveData<Menu.Data> {

        val liveData = MutableLiveData<Menu.Data>()
        val call = client.requestFoodData(id)

        call.enqueue(object : Callback<Menu> {
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Log.e("error", "${t.message}")
            }

            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if(response.isSuccessful){
                    Log.d("응답 성공!", "onResponse is Successful!")
                    val body = response.body()
                    liveData.value = body?.data
                }
                else {
                    Log.e("응답 실패", "response is not Successful")
                }
            }
        })
        return  liveData
    }



    fun requestMenuInfo(token:String, id:Int,menuBody:Menu.Data.FoodListDto): LiveData<Menu.Data.FoodListDto> {

        val liveData = MutableLiveData<Menu.Data.FoodListDto>()
        val call = client.post_foods(token, id, menuBody)

        call.enqueue(object: Callback<Menu.Data.FoodListDto> {
            override fun onFailure(call: Call<Menu.Data.FoodListDto>, t: Throwable) {
                Log.e("foodlist error", "${t.message}")
            }
            override fun onResponse(
                call: Call<Menu.Data.FoodListDto>,
                response: Response<Menu.Data.FoodListDto>
            ) {
                if(response.isSuccessful){
                    Log.d("foodlist success!", "onResponse is Successful!")
                    val body = response.body()
                    liveData.value = body!!
                    Log.d("내 닉네임은 ", "${liveData.value}")
                }
                else {
                    Log.e("foodlist fail", "response is not Successful")
                }
            }
        })
        return liveData

    }

    fun deleteMenu(token:String, id:Int) {
        val call = client.deleteFood(token, id)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("deletePhotop 성공!", "onResponse is Successful!")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("error", "${t.message}")
            }
        })

    }

    fun postPhotos(token:String,id:Int, files:List<MultipartBody.Part>) : LiveData<List<Int>>{
        val liveData = MutableLiveData<List<Int>>()
        val call = client.foodPhotoPost(token,id,files)

        call.enqueue(object: Callback<List<Int>> {
            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("error", "${t.message}")
            }

            override fun onResponse(
                call: Call<List<Int>>,
                response: Response<List<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("postPhotos 응답 성공!", "onResponse is Successful!")

                    val body = response.body()
                    liveData.value = body!!
                }
                else {
                    Log.e("응답 실패", "response is not Successful")
                    liveData.value = listOf(-1)
                    Log.e("errorBody() is ", response.errorBody()!!.string())
                }
            }
        })
        return liveData
    }
}

