package com.example.capstone.Oner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Login.App
import com.example.capstone.Retrofit.PostFoodinfoViewmodel
import com.example.capstone.data.Menu
import com.example.capstone.databinding.ActivityFoodRegisterBinding
import java.io.File
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image

class FoodRegisterActivity : AppCompatActivity(){

    val binding by lazy { ActivityFoodRegisterBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<PostFoodinfoViewmodel>()

    private val profileImageUrls = arrayListOf<String>()
    private val profileImage = arrayListOf<Image>()
    private lateinit var profileRecyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       /* profileRecyclerView
        profileRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        profileImageAdapter = PreviewImageAdapter(0,this)
        profileRecyclerView.adapter = profileImageAdapter

        binding.MenuCameraBtn.setOnClickListener{

            if(profileImageAdapter.itemsSize()==0){
                profilePickerLauncher.launch(profilePickerConfig)
            }

        }*/

        binding.menuSignUpBtn.setOnClickListener({
            postmenu()
            val returnMyStoreintent = Intent(this, OnerMainActivity::class.java)
            startActivity(returnMyStoreintent)
        })
    }


    private fun postmenu(){
        val body = getMenuDetailBody()

        viewModel.postFood(App.prefs.token!!,18,body)


    }

    private fun getMenuDetailBody() :Menu.Data.FoodListDto{
        return Menu.Data.FoodListDto(
            null,
            binding.menuNameIp.text.toString(),
            binding.costIp.text.toString(),
            binding.menuIntroIp.text.toString(),
            binding.tasteTextIp.text.toString(),
        )
    }

    private val profilePickerConfig = ImagePickerConfig{
        isShowCamera = false
        isFolderMode = true
        savePath = ImagePickerSavePath("Camera")
        savePath = ImagePickerSavePath(
            Environment.getExternalStorageDirectory().path, isRelative = false)
        limit = 1
    }
/*
    private val profilePickerLauncher = registerImagePicker { result: List<Image> ->
        profileImageUrls.clear()
        result.forEach { image ->
            println(image)
            profileImageAdapter.addImage(image)
            profileImage.clear()
            profileImage.addAll(result)
        }
    }

    override fun startProfileImageViewer(curIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun deletePosterImage(position: Int) {
        TODO("Not yet implemented")
    }
*/
}

