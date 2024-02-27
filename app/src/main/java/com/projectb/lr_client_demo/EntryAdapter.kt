package com.projectb.lr_client_demo

import android.app.Activity
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//class EntryAdapter (var entryArrayList: ArrayList<Entry>, var context: Activity){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
//        val itemView
//    }
//}

//class MyViewHolder(itemViewHolder: View): RecyclerView.ViewHolder(itemView){
//    val hTitle = itemView.findViewById<TextView>(R.id.headingTitle)
//}

data class Entry(
    val id: Int,
    val customer_id: Int,
    val customer_name: String,
    val gate_id: Int,
    val gate_name: String,
    val vehicle_id: Int,
    val vehicle_name: String
)

