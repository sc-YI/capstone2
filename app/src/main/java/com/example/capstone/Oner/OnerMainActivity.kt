package com.example.capstone.Oner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.Login.App
import com.example.capstone.Login.LoginViewModel
import com.example.capstone.Oner.CustomDialogStore
import com.example.capstone.Oner.FoodRegisterActivity
import com.example.capstone.Oner.StoreSettingActivity
import com.example.capstone.Retrofit.PostFoodinfoViewmodel
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.RetrofitAPI1
import com.example.capstone.adapter.FoodAdapter
import com.example.capstone.data.Menu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.capstone.databinding.ActivityOnerMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnerMainActivity : AppCompatActivity() {
    val binding by lazy { ActivityOnerMainBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<PostFoodinfoViewmodel>()
    private val onerloginviewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = FoodAdapter(this)
        binding.flist.adapter = adapter
        binding.flist.layoutManager =  LinearLayoutManager(this)

        loginCheck()

        binding.menuSingBtn1.setOnClickListener{
            val menuintent = Intent(this, FoodRegisterActivity::class.java)
            startActivity(menuintent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitMenu::class.java)

        val retrofitService2 = retrofit.create(RetrofitAPI1::class.java)
        //가게 소개 get해오기



        retrofitService.requestFoodData(18).enqueue(object : Callback<Menu>{
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                adapter.menuList = response.body()?.data?.foodListDtoList
                adapter.notifyDataSetChanged()
                Log.d("foodlist", "${response.body()?.data?.foodListDtoList}")
            }

            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Log.d("foodlist", "통신 에러")
            }

        })
        binding.myStoreLayout.setOnClickListener(){
            val storeDialog = CustomDialogStore(this)
            storeDialog.showDialog()
            storeDialog.setButtonListener(object: CustomDialogStore.OnButtonClickListener {
            override fun okBtnClicked() {
                val intent = Intent(this@OnerMainActivity, StoreSettingActivity::class.java)
                startActivity(intent)
            }

            }) //화면 넘김
        }
    }


    private fun loginCheck() {
        if(App.nowLogin) {
            if(App.memberInfo == null) {
                onerloginviewModel.requestMemberInfo(App.prefs.token!!)
                onerloginviewModel.memberInfo.observe(this, {
                    App.memberInfo = it
                })
                Log.d("사장 로그인 정보", "${App.memberInfo}, ${App.nowLogin}")
            }
        }
        else{
            Log.d("사장 로그인 정보", "실패")

        }
    }

    private  fun getStoreInfo(storeId :Int){

    }

}
