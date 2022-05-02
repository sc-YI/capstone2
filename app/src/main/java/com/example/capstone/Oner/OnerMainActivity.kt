package com.example.capstone.Oner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.capstone.Login.App
import com.example.capstone.Login.LoginViewModel
import com.example.capstone.R
import com.example.capstone.Retrofit.PostFoodinfoViewmodel
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.adapter.FoodAdapter
import com.example.capstone.adapter.MenuAdapter
import com.example.capstone.adapter.ReviewAdapter
import com.example.capstone.data.Menu
import com.example.capstone.data.review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.capstone.databinding.ActivityOnerMainBinding
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OnerMainActivity : AppCompatActivity() {
    val binding by lazy { ActivityOnerMainBinding.inflate(layoutInflater)}
    private val onerloginviewModel by viewModels<LoginViewModel>()
    var foodValue: Menu.Data? = null

    // 스토어 정보 및 사진 가져오기
    // 음식 사진 리사이클러뷰에 반영
    // 음식 클릭해서 메뉴 세부 사항 표시하기.
    // 품절 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mAdapter = FoodAdapter(this)
        binding.flist.adapter = mAdapter
        binding.flist.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitMenu::class.java)
        Log.d("메뉴 통신 시작", "통신중")
        retrofitService
            .getFoodData(App.prefs.token!!)
            .enqueue(object : Callback<Menu> {
                override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                    mAdapter.menuList = response.body()?.data?.foodListDtoList
                    binding.storeIntro2.text = response.body()?.data?.name
                    binding.storeIntroDetail.text = response.body()?.data?.phoneNumber
                    mAdapter.notifyDataSetChanged()
                    Log.d("현재토큰값", App.prefs.token!!.toString())
                    Log.d("메뉴 불러오기 성공!", response.body()?.data?.foodListDtoList.toString())
                }

                override fun onFailure(call: Call<Menu>, t: Throwable) {
                    Log.d("메뉴 통신 실패", "${t.message.toString()}")
                }
            })

            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.camera)

            Glide.with(this)
                .load("https://473d-125-180-55-163.ngrok.io:443/circles/view/photo/52")
                .fitCenter()
                .into(binding.imageView)



        binding.menuSingBtn1.setOnClickListener{
            val menuintent = Intent(this, FoodRegisterActivity::class.java)
            startActivity(menuintent)
        }


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


}


