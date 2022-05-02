package com.example.capstone.Retrofit

import com.example.capstone.data.MemberResponse
import com.example.capstone.data.Menu
import com.example.capstone.data.ReviewData
import com.example.capstone.data.StoreData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitStore {


    @Multipart
    @POST("/user/store/{id}/photo")
    fun postStorePhoto(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("id") id: Int,
        @Part files : List<MultipartBody.Part>) : Call<List<Int>>

    @PATCH("store")
    fun patchStore(
        @Header("X-AUTH-TOKEN") token: String,
        @Body jsonparams: StoreData,
    ) : Call<StoreData>

}