package com.example.capstone.Retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.Login.App
import com.example.capstone.data.Menu
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class StoreContentServiceCreator {

    private val BASE_URL = "https://473d-125-180-55-163.ngrok.io/"
    private val client : RetrofitMenu



    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        client = retrofit.create(RetrofitMenu::class.java)
    }

    fun getMentContent(token: String) : LiveData<Menu.Data> {

        val livedata: MutableLiveData<Menu.Data> = MutableLiveData()
        val call = client.getFoodData(token)

        call.enqueue(object : Callback<Menu> {
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Log.e("error", "${t.message}")
            }

            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if(response.isSuccessful){
                    Log.d("응답 성공!", "onResponse is Successful!")
                    val body = response.body()
                    livedata.value = response.body()?.data
                }
                else {
                    Log.e("응답 실패", "response is not Successful")
                }
            }
        })
        return  livedata
    }
}