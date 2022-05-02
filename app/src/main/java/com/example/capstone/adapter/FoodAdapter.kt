package com.example.capstone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.AllReviewActivity
import com.example.capstone.Login.App
import com.example.capstone.Oner.CustomDialog
import com.example.capstone.Oner.MenuFatchActivity
import com.example.capstone.Oner.StoreSettingActivity
import com.example.capstone.R
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.data.Menu
import com.example.capstone.databinding.FoodListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodAdapter(private val context: Context): RecyclerView.Adapter<foodHolder>(){

   var menuList : List<Menu.Data.FoodListDto>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return foodHolder(binding)
    }





    override fun onBindViewHolder(holder: foodHolder, position: Int) {


        var menu = menuList?.get(position)
        holder.setMenu(menu)



        holder.itemView.setOnClickListener(){
            val foodId = menu?.id
            Log.d("음식 아이디", "${foodId}")
            val dialog =  CustomDialog(context)
            dialog.showDialog()
            dialog.setButtonListener(object: CustomDialog.OnButtonClickListener {
                override fun foodsettingClicked() {
                    val intent = Intent(holder.itemView?.context, MenuFatchActivity::class.java)
                    intent.putExtra("foodId", foodId.toString())
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }

                override fun reviewReadClicked() {
                    val intent = Intent(holder.itemView?.context, AllReviewActivity::class.java)
                    intent.putExtra("foodId", foodId.toString())
                    Log.d("food id", "${menu?.id}")
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
                override fun soldoutClicked() {
                    Log.d("품절 처리", "${menu?.id}")

                       val retrofit = Retrofit.Builder()
                           .baseUrl("https://473d-125-180-55-163.ngrok.io/")
                           .addConverterFactory(GsonConverterFactory.create())
                           .build()
                       val retrofitService = retrofit.create(RetrofitMenu::class.java)
                       if (menu!!.status == "SOLDOUT") {
                          Log.d("품절 처리", "${menu?.id}")
                          if (foodId != null) {
                              retrofitService
                                  .patch_onsale(App.prefs.token!!, foodId)
                                  .enqueue(object : Callback<Menu.Data.FoodListDto> {
                                      override fun onResponse(
                                          call: Call<Menu.Data.FoodListDto>,
                                          response: Response<Menu.Data.FoodListDto>
                                      ) {
                                          //  FoodAdapter.notifyDataSetChanged()
                                          Log.d("메뉴 통신 성공", "${foodId}")
                                      }

                                      override fun onFailure(
                                          call: Call<Menu.Data.FoodListDto>,
                                          t: Throwable
                                      ) {
                                          Log.d("메뉴 통신 실패", "${t.message.toString()}")
                                      }
                                  })
                          }
                      }
                      else{
                          if (foodId != null) {
                              retrofitService
                                  .patch_soldout(App.prefs.token!!, foodId)
                                  .enqueue(object : Callback<Menu.Data.FoodListDto> {
                                      override fun onResponse(
                                          call: Call<Menu.Data.FoodListDto>,
                                          response: Response<Menu.Data.FoodListDto>
                                      ) {
                                          //  FoodAdapter.notifyDataSetChanged()
                                          Log.d("메뉴 통신 성공", "${foodId}")
                                      }

                                      override fun onFailure(
                                          call: Call<Menu.Data.FoodListDto>,
                                          t: Throwable
                                      ) {
                                          Log.d("메뉴 통신 실패", "${t.message.toString()}")
                                      }
                                  })
                          }

                       }

                }
            })
        }


    }

    override fun getItemCount(): Int {
        return menuList?.size?: 0
    }
}

class foodHolder(val binding: FoodListBinding): RecyclerView.ViewHolder(binding.root)
{
    init {
        itemView.setOnClickListener(){

        }
    }
    fun setMenu(menu: Menu.Data.FoodListDto?){
        menu?.let{
            binding.menuName.text = menu.id.toString()
            binding.menuPrice.text = menu.price
            binding.rating.text = "3.0"
            if (menu.status == "SOLDOUT") {
                binding.soldout.setImageResource(R.drawable.soldout)
            }
            else {
                binding.soldout.setImageResource(R.drawable.onsale)
            }
            binding.ratingImg.setImageResource(R.drawable.star)
            Glide.with(itemView.context)
                .load("https://473d-125-180-55-163.ngrok.io:443/circles/view/photo/${menu.id}")
                .fitCenter()
                .into(binding.menuImg)

        }

    }


}