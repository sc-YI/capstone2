package com.example.capstone.Image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.capstone.R

class ImageFragment : Fragment() {

    private lateinit var imageView: ImageView
    private var image = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            image = arguments!!.getString(IMAGE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        imageView = view.findViewById(R.id.iv_image)
        Glide.with(this)
            .load(image)
            .into(imageView)

        return view
    }
    companion object{

        private const val IMAGE = "image"

        fun newInstance(image : String): Fragment{
            val args = Bundle()
            args.putString(IMAGE,image)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}