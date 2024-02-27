package com.projectb.lr_client_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectb.lr_client_demo.databinding.ActivityAdminEntryBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AdminEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEntryBinding
    private lateinit var handler: Handler
    private val client = OkHttpClient()
//    val entryListTypeToken = object : TypeToken<List<User>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_entry)
        binding = ActivityAdminEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())

        val bundle = intent.extras  // getting the bundle back from the android
        val user_id = bundle!!.getInt("user_id", 0) // getting the string back

        binding.createEntryButton.setOnClickListener{
            val intent = Intent(this, AdminEntryCreateActivity::class.java)
            startActivity(intent)
        }

        val getUserThread = Thread{
            val request = Request.Builder()
                .url("${getString(R.string.hostname)}/user/admin/entries?user_id=${user_id}")
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
                        val data = gson.fromJson(userListJsonString, AdminEntryRespond::class.java)
                        val runnable = object : Runnable {
                            override fun run() {
                                set_entries_layout(data)
                            }
                        }
                        handler.post(runnable)
                    }
                }
            })
        }
        getUserThread.start()




    }

    fun set_entries_layout(respond: AdminEntryRespond){
        val entries_layout = findViewById<View>(R.id.entriesLayout) as LinearLayout
        for (pending_entry in respond.entries_without_order){
            val entry_layout = LinearLayout(this)
            entry_layout.isClickable = true
            entry_layout.setOnClickListener {// Create order from entry

//                val intent = if(user.user_type == 1) Intent(this, AdminEntryActivity::class.java)
//                else if(user.user_type == 2) Intent(this, ForemanOrderActivity::class.java)
//                else if(user.user_type == 3) Intent(this, CheckerOrderActivity::class.java)
//                else Intent(this, MainActivity::class.java)
//                val bundle = Bundle()
//                bundle.putInt("user_id", user.id)
//                intent.putExtras(bundle)
//                startActivity(intent)
            }
            entry_layout.orientation = LinearLayout.HORIZONTAL
            entry_layout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val entry_textview = TextView(this)
            entry_textview.text = "${pending_entry.gate_name}"+
                            "\n${pending_entry.customer_name}" +
                            "\n${pending_entry.vehicle_name}" +
                    "\nPending..." +
                            "\n------------------------------------------------"
            entry_textview.setTextSize((resources.displayMetrics.widthPixels * 0.02).toFloat())
            entry_textview.layoutParams = LinearLayout.LayoutParams((resources.displayMetrics.widthPixels * 0.75).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
            entry_layout.addView(entry_textview)

            entries_layout.addView(entry_layout)
        }
        for (executing_entry in respond.entries_with_order){
            val entry_layout = LinearLayout(this)
            entry_layout.isClickable = true
            entry_layout.setOnClickListener {// Approve finished order and archive entry
//                val intent = if(user.user_type == 1) Intent(this, AdminEntryActivity::class.java)
//                else if(user.user_type == 2) Intent(this, ForemanOrderActivity::class.java)
//                else if(user.user_type == 3) Intent(this, CheckerOrderActivity::class.java)
//                else Intent(this, MainActivity::class.java)
//                val bundle = Bundle()
//                bundle.putInt("user_id", user.id)
//                intent.putExtras(bundle)
//                startActivity(intent)
            }
            entry_layout.orientation = LinearLayout.HORIZONTAL
            entry_layout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val entry_textview = TextView(this)
            entry_textview.text = "${executing_entry.gate_name}"+
                    "\n${executing_entry.customer_name}" +
                    "\n${executing_entry.vehicle_name}" +
                    "\nExecuting..." +
                    "\n------------------------------------------------"
            entry_textview.setTextSize((resources.displayMetrics.widthPixels * 0.02).toFloat())
            entry_textview.layoutParams = LinearLayout.LayoutParams((resources.displayMetrics.widthPixels * 0.75).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
            entry_layout.addView(entry_textview)

            entries_layout.addView(entry_layout)
        }
    }
}

//data class Entry(
//    val id: Int,
//    val customer_id: Int,
//    val customer_name: String,
//    val gate_id: Int,
//    val gate_name: String,
//    val vehicle_id: Int,
//    val vehicle_name: String
//)
//
data class AdminEntryRespond(
    val entries_without_order: List<Entry>,
    val entries_with_order: List<Entry>
)