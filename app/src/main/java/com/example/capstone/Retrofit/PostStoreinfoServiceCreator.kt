package com.example.capstone.Retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.data.Menu
import com.example.capstone.data.StoreData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PostStoreinfoServiceCreator {
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

    fun postStoreImage(token:String, photo :List<MultipartBody.Part>) : LiveData<Int>{
        val livedata = MutableLiveData<Int>()
        val call = client.postStorePhoto(token, photo)

        call.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("error", "${t.message}")
            }

            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if(response.isSuccessful){
                    Log.d("setProfileImage 응답 성공!", "onResponse is Successful!")
                    val body = response.body()
                    livedata.value = body!!
                }
                else {
                    Log.e("응답 실패", "response is not Successful")
                    Log.e("errorBody() is ", response.errorBody()!!.string())
                    livedata.value = -1
                }
            }
        })
        return livedata
    }


    fun patchStoreInfo(token:String,menuBody: StoreData): LiveData<StoreData> {

        val liveData = MutableLiveData<StoreData>()
        val call = client.patchStore(token, menuBody)

        call.enqueue(object: Callback<StoreData> {
            override fun onFailure(call: Call<StoreData>, t: Throwable) {
                Log.e("foodlist error", "${t.message}")
            }
            override fun onResponse(
                call: Call<StoreData>,
                response: Response<StoreData>
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
}
