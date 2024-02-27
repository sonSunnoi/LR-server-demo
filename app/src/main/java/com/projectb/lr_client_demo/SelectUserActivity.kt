package com.projectb.lr_client_demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectb.lr_client_demo.databinding.ActivitySelectUserBinding
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


class SelectUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectUserBinding
    private lateinit var handler: Handler
    private val client = OkHttpClient()
    val userListTypeToken = object : TypeToken<List<User>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)
        binding = ActivitySelectUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())

        val bundle = intent.extras  // getting the bundle back from the android
        val user_type = bundle!!.getInt("user_type", 0) // getting the string back

        binding.createUserButton.setText("Create new ${UserType.entries[user_type]} user")
        binding.createUserButton.setTextSize((resources.displayMetrics.widthPixels * 0.02).toFloat())
        binding.createUserButton.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (resources.displayMetrics.heightPixels * 0.1).toInt())
        binding.createUserButton.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val getUserThread = Thread{
            val request = Request.Builder()
                .url("${getString(R.string.hostname)}/users?user_type=${user_type}")
                .get()
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.d("projectb", "Failure")
                    Log.d("projectb", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("projectb", "Parsing Response ...")
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val userListJsonString = response.body?.string().toString()
                        Log.d("projectb", userListJsonString)

                        val gson = Gson()
                        val userList = gson.fromJson<List<User>>(userListJsonString, userListTypeToken)
                        for(user in userList){
                            Log.d("projectb", user.name.toString())
                        }
                        val runnable = object : Runnable {
                            override fun run() {
                                set_user_layout(userList)
                            }
                        }
                        handler.post(runnable)
                    }
                }
            })
        }
        getUserThread.start()
    }

    fun set_user_layout(users: List<User>){
        val users_layout = findViewById<View>(R.id.userLayout) as LinearLayout
        for (user in users){
            val user_layout = LinearLayout(this)
            user_layout.isClickable = true
            user_layout.setOnClickListener {
                val intent = if(user.user_type == 1) Intent(this, AdminEntryActivity::class.java)
                             else if(user.user_type == 2) Intent(this, ForemanOrderActivity::class.java)
                             else if(user.user_type == 3) Intent(this, CheckerOrderActivity::class.java)
                             else Intent(this, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("user_id", user.id)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            user_layout.orientation = LinearLayout.HORIZONTAL
            user_layout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // User profile image
            val user_imageview = ImageView(this)
            user_imageview.layoutParams = LinearLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.25).toInt(), // 20% of screen width
                (resources.displayMetrics.heightPixels * 0.1).toInt()
            )
//            Picasso.get()
//                .load("")
//                .into(user_imageview)

//            val selectUserTextView = findViewById<TextView>(R.id.selectUserTextView)
//            selectUserTextView.setTextSize((resources.displayMetrics.widthPixels * 0.03).toFloat())
//            selectUserTextView.layoutParams = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                (resources.displayMetrics.heightPixels * 0.1).toInt()
//            )
//            selectUserTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER

            val user_textview = TextView(this)
            user_textview.text = user.name.toString()
            user_textview.setTextSize((resources.displayMetrics.widthPixels * 0.02).toFloat())

            user_textview.layoutParams = LinearLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.75).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            user_layout.addView(user_imageview)
            user_layout.addView(user_textview)

            users_layout.addView(user_layout)
        }
    }
}


//fun parseJsonToUserDataList(jsonData: String): List<UserData> {
//    val gson = Gson()
//    return gson.fromJson(jsonData, Array<UserData>::class.java).toList()
//}
data class User(
    val id: Int,
    val name: String,
    val user_type: Int,
    val uuid: String?,  // Assuming uuid can be nullable based on the JSON response
    val is_active: Boolean
)

enum class UserType{
    Invalid,
    Admin,
    Foreman,
    Checker,
    Forklift
}
