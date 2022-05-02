package com.example.capstone.Oner

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.example.capstone.Login.App
import com.example.capstone.R
import com.example.capstone.Retrofit.PostFoodinfoViewmodel
import com.example.capstone.data.Menu
import com.example.capstone.databinding.ActivityMenuFatchBinding
import java.io.File

class MenuFatchActivity : AppCompatActivity() {

    //음식 사진 패치

    val binding by lazy { ActivityMenuFatchBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<PostFoodinfoViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val foodId = intent?.getStringExtra("foodId")?.toInt()
        Log.d("음식 아이디", "${foodId}")

        binding.fatchSignUpBtn.setOnClickListener({

            if (foodId != null) {
                patchmenu(foodId)
                showToastMsg("음식 정보가 수정되었습니다.")
            }
            val returnMyStoreintent = Intent(this, OnerMainActivity::class.java)
            startActivity(returnMyStoreintent)
        })

        binding.deletebutton.setOnClickListener({
            if (foodId != null) {
                viewModel.deleteMenu(App.prefs.token!!, foodId)
                showToastMsg("음식이 삭제되었습니다.")
            }
            val returnMyStoreintent = Intent(this, OnerMainActivity::class.java)
            startActivity(returnMyStoreintent)
        })

    }


    private fun patchmenu(id : Int){
        val body = getMenuDetailBody()
        viewModel.patchFood(App.prefs.token!!,id,body)
    }

    private fun getMenuDetailBody() :Menu.Data.FoodListDto{
        return Menu.Data.FoodListDto(
            null,
            binding.fatchNameIp.text.toString(),
            binding.fatchCostIp.text.toString(),
            binding.fatchIntroIp.text.toString(),
            binding.fatchTasteTextIp.text.toString(),
        )
    }
    private fun showToastMsg(msg:String){ Toast.makeText(this,msg,Toast.LENGTH_SHORT).show() }

}