package com.example.capstone.Retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.Menu
import okhttp3.MultipartBody

class PostStoreinfoViewmodel: ViewModel() {

    val client : PostStoreinfoServiceCreator

    init {
        client = PostStoreinfoServiceCreator()
    }

    lateinit var StoreInfo : LiveData<Menu.Data>

    lateinit var StoreImageId : LiveData<Int>
    lateinit var PathcStoreId : LiveData<Int>


    fun postStorePhoto(token: String, files:List<MultipartBody.Part>){
        StoreImageId = client.postStoreImage(token, files)
    }

    fun patchStoreInfo(token: String){
      // StoreInfo = client.patchStoreInfo((token, body: Sto)
    }
}