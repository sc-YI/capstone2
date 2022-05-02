package com.example.capstone.Retrofit

import com.example.capstone.data.MemberResponse
import com.example.capstone.data.Menu
import com.example.capstone.data.MenuPostBody
import com.example.capstone.data.StoreData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitMenu {

    @GET("store/{id}")
    fun requestFoodData(@Path("id") id: Int): Call<Menu>

    @GET("store")
    fun getFoodData(@Header("X-AUTH-TOKEN") token: String): Call<Menu>


    @Multipart
    @POST("/user/store/photo")
    fun postStorePhoto(
        @Header("X-AUTH-TOKEN") token: String,
        @Part files : List<MultipartBody.Part>) : Call<Int>

    @PATCH("store")
    fun patchStore(
        @Header("X-AUTH-TOKEN") token: String,
        @Body jsonparams: StoreData,
    ) : Call<StoreData>

    @PATCH("food/{id}")
    fun patch_foods(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id:Int,
        @Body jsonparams: Menu.Data.FoodListDto
    ): Call<Menu.Data.FoodListDto>

    @PATCH("food/{id}/soldout")
    fun patch_soldout(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id:Int,
    ): Call<Menu.Data.FoodListDto>

    @PATCH("food/{id}/onsale")
    fun patch_onsale(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id:Int,
    ): Call<Menu.Data.FoodListDto>


    @POST("food")
    fun post_foods(
        @Header("X-AUTH-TOKEN") token: String,
        //@Path("id") id:Int,
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