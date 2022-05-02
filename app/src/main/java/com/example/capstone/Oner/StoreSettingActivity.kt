package com.example.capstone.Oner

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.example.capstone.R
import com.example.capstone.adapter.StoreImageAdapter
import com.example.capstone.databinding.ActivityStoreSettingBinding
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.capstone.Image.OnPreviewImageClick2
import com.example.capstone.Image.SlideImageViewer
import com.example.capstone.Login.App
import com.example.capstone.Retrofit.PostStoreinfoViewmodel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class StoreSettingActivity : AppCompatActivity(), OnPreviewImageClick2 {

    // 가게 정보 수정 및 가게 사진 패치

    private val viewModel by viewModels<PostStoreinfoViewmodel>()
    private var files  = ArrayList<MultipartBody.Part>()

    val binding by lazy { ActivityStoreSettingBinding.inflate(layoutInflater)}

    private val storeImageUrls = arrayListOf<String>()
    private val storeImage = arrayListOf<Image>()

    private lateinit var storeImageAdapter : StoreImageAdapter
    private lateinit var storeRecyclerView : RecyclerView
    private val postedPhotos = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        storeRecyclerView = findViewById(R.id.recycler_store_image)
        storeRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        storeImageAdapter = StoreImageAdapter(0,this)
        storeRecyclerView.adapter = storeImageAdapter

        binding.ReGlyBtn.setOnClickListener({
            Log.e("갤러리 응답 시작", "1")
            if(storeImageAdapter.itemsSize() == 0){
                Log.e("item size = 0", "1")
                storePickerLauncher.launch(storePickerConfig)
            }
        })

        binding.ResignUpBtn2.setOnClickListener({
                postPhoto()
        })
    }
    private val storePickerConfig = ImagePickerConfig{
        Log.e("이미지 피커 콘피그 시작", "1")
        Log.e("이미지 피커 경로 받아오기 시작", "1")

        isShowCamera = false
        isFolderMode = true
        savePath = ImagePickerSavePath("Camera")
        savePath = ImagePickerSavePath(Environment.getExternalStorageDirectory().path, isRelative = false)
        limit = 1
    }

    private val storePickerLauncher = registerImagePicker { result: List<Image> ->
        Log.e("이미지 피커 런처 시작", "1")
        storeImageUrls.clear()
        result.forEach { image ->
            println(image)

            storeImageAdapter.addImage(image)
            storeImage.clear()
            storeImage.addAll(result)
        }
    }

    fun requsetStoreInfo(){


    }

    override fun startStoreSlideImageView(curIndex: Int) {
        SlideImageViewer.start(this, storeImageUrls)
    }

    override fun startfoodSlideImageView(curIndex: Int) {}

    private fun postPhoto() {
        if(storeImage.isEmpty()) {
            showToastMsg("가게가 수정되었습니다.")
            setResult(RESULT_OK)
            finish()
        } else {
            addImagesToFiles(storeImage)
            viewModel.postStorePhoto(App.prefs.token!!, files)
            viewModel.StoreImageId.observe(
                this, {
                    if(it == -1) {
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
        val bitmapBody = RequestBody.create("image/*".toMediaTypeOrNull(),byteArray)
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