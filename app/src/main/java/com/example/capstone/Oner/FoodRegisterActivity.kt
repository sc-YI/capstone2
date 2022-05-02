package com.example.capstone.Oner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Login.App
import com.example.capstone.Retrofit.PostFoodinfoViewmodel
import com.example.capstone.data.Menu
import com.example.capstone.databinding.ActivityFoodRegisterBinding
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.capstone.Image.OnPreviewImageClick2
import com.example.capstone.Image.SlideImageViewer
import com.example.capstone.R
import com.example.capstone.adapter.StoreImageAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class FoodRegisterActivity : AppCompatActivity(), OnPreviewImageClick2 {

    //음식 등록시 사진 등록 포함.
    val binding by lazy { ActivityFoodRegisterBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<PostFoodinfoViewmodel>()
    private var photo = ArrayList<MultipartBody.Part>()
    private var files  = ArrayList<MultipartBody.Part>()

    private val foodImage = arrayListOf<Image>()
    private val foodImageUrls = arrayListOf<String>()

    private lateinit var foodImageAdapter : StoreImageAdapter
    private lateinit var foodRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        foodRecyclerView = findViewById(R.id.recycler_food_image)
        foodRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        foodImageAdapter = StoreImageAdapter(0,this)
        foodRecyclerView.adapter = foodImageAdapter

        binding.MenuGlyBtn.setOnClickListener({
            if(foodImageAdapter.itemsSize() == 0){
                foodPickerLauncher.launch(foodPickerConfig)
            }
        })

        binding.menuSignUpBtn.setOnClickListener({
            //postmenu()
            postPhoto(23)
            val returnMyStoreintent = Intent(this, OnerMainActivity::class.java)
            startActivity(returnMyStoreintent)
        })
    }


    private fun postmenu(){
        val body = getMenuDetailBody()
        viewModel.postFood(App.prefs.token!!,body)
        viewModel.foodpostContent.observe(
            this,
            {
                if (it == -1){
                    showToastMsg("등록 실패")
                }
                else {
                    Log.d("post photo 실행", "")
                    postPhoto(it)
                }
            }
        )
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



    private val foodPickerConfig = ImagePickerConfig{
        isShowCamera = false
        isFolderMode = true
        savePath = ImagePickerSavePath("Camera")
        savePath = ImagePickerSavePath(
            Environment.getExternalStorageDirectory().path, isRelative = false)
    }

    private val foodPickerLauncher = registerImagePicker { result: List<Image> ->
        foodImageUrls.clear()
        result.forEach { image ->
            println(image)


            foodImageAdapter.addImage(image)
            foodImage.clear()
            foodImage.addAll(result)
        }
    }
    override fun startStoreSlideImageView(curIndex: Int) {

    }

    override fun startfoodSlideImageView(curIndex: Int) {
        SlideImageViewer.start(this, foodImageUrls)
    }


    private fun postPhoto(id : Int ) {
        Log.d("post photo 실행", "")

        if(foodImage.isEmpty()) {
            showToastMsg("음식이 등록되었습니다.")
            setResult(RESULT_OK)
            finish()
        } else {
            addImagesToFiles(foodImage)
            viewModel.postFoodPhotos(App.prefs.token!!, id, files)
            viewModel.postedImagesId.observe(
                this, {
                    if(it[0] == -1) {
                        showToastMsg("사진을 업로드하지 못했습니다.")
                    } else {
                        showToastMsg("사진이 등록되었습니다.")
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            )
        }
    }

    private fun addImagesToFiles(images: ArrayList<Image>) {

        images.forEach {
            files.add(getCompressedFile(it.path))
        }
    }

    private fun getCompressedFile(photoPath: String) : MultipartBody.Part{


        val bitmap = getScaledBitmap(photoPath,this)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        val byteArray = stream.toByteArray()
        val bitmapBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(),byteArray)
        val body = MultipartBody.Part.createFormData("files",photoPath,bitmapBody)
        return body
    }


    fun getScaledBitmap(path: String, activity: Activity): Bitmap {
        val size = Point()

        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getSize(size)

        return getScaledBitmap(path, size.x, size.y)
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
        // 이미지 파일의 크기를 읽는다
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        // 크기를 얼마나 줄일지 파악한다
        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            val sampleScale = if (heightScale > widthScale) {
                heightScale
            } else {
                widthScale
            }
            inSampleSize = Math.round(sampleScale)
        }

        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        // 최종 Bitmap을 생성한다
        return BitmapFactory.decodeFile(path, options)
    }

    fun showToastMsg(msg:String){ Toast.makeText(this,msg, Toast.LENGTH_SHORT).show() }


}

