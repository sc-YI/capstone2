package com.example.capstone.Oner

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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

    val binding by lazy { ActivityMenuFatchBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<PostFoodinfoViewmodel>()

    var photoUri: Uri? = null
    lateinit var cameraPermission: ActivityResultLauncher<String>
    lateinit var storagePermission: ActivityResultLauncher<String>

    lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    lateinit var galleryLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setViews()
            } else {
                Toast.makeText(baseContext, "외부저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(baseContext, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_LONG)
                    .show()
            }
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                binding.fatchMenuImg.setImageURI(photoUri)
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.fatchMenuImg.setImageURI(uri)
        }
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)



        binding.fatchSignUpBtn.setOnClickListener({
            val returnMyStoreintent = Intent(this, OnerMainActivity::class.java)
            startActivity(returnMyStoreintent)
        })

        binding.deletebutton.setOnClickListener({
            val id : Int = 25
            viewModel.deleteMenu(App.prefs.token!!, id)
        })

    }

    fun setViews() {
        binding.FatchCameraBtn.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
        binding.FatchGlyBtn.setOnClickListener {
            openGallery()
        }
    }

    fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )

        cameraLauncher.launch(photoUri)
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }


    private fun postmenu(){
        //val body = getMenuDetailBody()

        //viewModel.postFood(App.prefs.token!!,18,body)


    }

    /*private fun getMenuDetailBody() : Menu.Data.FoodListDto{
        return Menu.Data.FoodListDto(
            null,
            binding.menuNameIp.text.toString(),
            binding.costIp.text.toString(),
            binding.menuIntroIp.text.toString(),
            binding.tasteTextIp.text.toString(),
        )
    }
    */
}