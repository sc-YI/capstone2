package com.example.capstone.Retrofit

import com.example.capstone.data.MemberResponse
import com.example.capstone.data.Menu
import com.example.capstone.data.MenuPostBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitMenu {

    @GET("store/{id}")
    fun requestFoodData(@Path("id") id: Int): Call<Menu>


    @POST("food/{id}")
    fun post_foods(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id:Int,
        @Body jsonparams: Menu.Data.FoodListDto
    ): Call<Menu.Data.FoodListDto>



    @Multipart
    @POST("/user/store/{id}/photo")
    fun foodPhotoPost(
        @Header("X-AUTH-TOKEN") token : String,
        @Path("id") id :Int,
        @Part photo : List<MultipartBody.Part>,
    ): Call<List<Int>>

    @DELETE("food/{id}")
    fun deleteFood(@Header("X-AUTH-TOKEN") token : String,
                   @Path("id") foodid:Int) : Call<Void>

}