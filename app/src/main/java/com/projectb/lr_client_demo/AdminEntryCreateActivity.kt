package com.projectb.lr_client_demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.projectb.lr_client_demo.databinding.ActivityAdminEntryCreateBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONException
import org.json.JSONObject


class AdminEntryCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEntryCreateBinding
    private lateinit var handler: Handler
    private val client = OkHttpClient()
    private val refreshIntervalMillis: Long = 5000 // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_entry_create)
        binding = ActivityAdminEntryCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())
//        val bundle = intent.extras  // getting the bundle back from the android
//        val user_id = bundle!!.getInt("user_id", 0) // getting the string back
        createEntryGet()
    }

    private fun createEntryGet() {
        val getUserThread = Thread{
            val request = Request.Builder()
                .url("${getString(R.string.hostname)}/user/admin/create_entry")
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
                        val data = gson.fromJson(userListJsonString, CreateEntryGetRespond::class.java)

                        val runnable = object : Runnable {
                            override fun run() {
                                set_gateSpinner(data)
                                set_customerSpinner(data)
                                set_createEntryButton(data)
                            }
                        }
                        handler.post(runnable)
                    }
                }
            })
        }
        getUserThread.start()
    }

    fun set_gateSpinner(data: CreateEntryGetRespond){
        val gate_names: Array<String> = data.gates.map { it.name }.toTypedArray()
        val gateSpinner = binding.gateSpinner
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, gate_names)
        gateSpinner.adapter = adapter

        gateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected item
//                bundle!!.putInt("gate_id", position)
                Log.d("projectb", "Select: ${data.gates[position].name}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }

    }

    fun set_customerSpinner(data: CreateEntryGetRespond){
        val customer_names: Array<String> = data.customers.map { it.name }.toTypedArray()
        val customerSpinner = binding.customerSpinner
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, customer_names)
        customerSpinner.adapter = adapter

        customerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected item
//                bundle!!.putInt("customer_id", position)
                Log.d("projectb", "Select: ${data.customers[position].name}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }
    }

    fun set_createEntryButton(data: CreateEntryGetRespond){
        val createEntryButton = binding.createEntryConfirmButton
        createEntryButton.setOnClickListener{
            createEntryPost(data)
        }
    }

    fun createEntryPost(data: CreateEntryGetRespond){
        val gateSpinner = binding.gateSpinner
        val gate_id = data.gates[gateSpinner.getSelectedItemPosition()].id
        val customerSpinner = binding.customerSpinner
        val customer_id = data.customers[customerSpinner.getSelectedItemPosition()].id
        val vehicleNameInputText = binding.vehicleNameInputText
        val vehicle_name = vehicleNameInputText.text
        val getUserThread = Thread{
//            startActivity(intent)   // Go back to AdminEntryActivity
            val jsonObject = JSONObject()
            try {
//                jsonObject.put("gate_id", gate_id)
//                jsonObject.put("customer_id", customer_id)
//                jsonObject.put("vehicle_name", "9 aa 1234")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonObject.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("${getString(R.string.hostname)}/user/admin/create_entry?gate_id=${gate_id}&customer_id=${customer_id}&vehicle_name=${vehicle_name}")
                .post(body)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.d("projectb", "Failure")
                    Log.d("projectb", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {

//                    Log.d("projectb", "Parsing Response ...")
//                    response.use {
//                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
//                        val userListJsonString = response.body?.string().toString()
//                        Log.d("projectb", userListJsonString)
//
//                        val gson = Gson()
//                        val data = gson.fromJson(userListJsonString, CreateEntryGetRespond::class.java)
//
//                        val runnable = object : Runnable {
//                            override fun run() {
//                                val intent = Intent(this@AdminEntryCreateActivity, AdminEntryActivity::class.java)
//                                startActivity(intent)   // Go back to AdminEntryActivity
//                            }
//                        }
//                        handler.post(runnable)
//                    }
                }
            })

        }
        getUserThread.start()
        val intent = Intent(this@AdminEntryCreateActivity, AdminEntryActivity::class.java)
        startActivity(intent)   // Go back to AdminEntryActivity
    }
}

data class Gate(
    val id: Int,
    val name: String
)

data class Customer(
    val id: Int,
    val name: String
)
data class CreateEntryGetRespond(
    val gates: List<Gate>,
    val customers: List<Customer>
)
