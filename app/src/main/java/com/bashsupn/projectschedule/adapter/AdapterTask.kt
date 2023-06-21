package com.bashsupn.projectschedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.models.Task

class AdapterTask(private val dataList: ArrayList<Task>) : RecyclerView.Adapter<AdapterTask.ViewHolderData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user, parent, false)
        return ViewHolderData(layout)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        val item = dataList[position]
        holder.nama.text = item.name
        holder.email.text = "Duration : "+item.duration.toString()
    }

    fun updateData(tasks: List<Task>) {
        dataList.clear()
        dataList.addAll(tasks)
        notifyDataSetChanged()
    }

    class ViewHolderData(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nama: TextView =itemView.findViewById(R.id.mTitle)
        val email: TextView =itemView.findViewById(R.id.mTgl)
        val context = itemView.context
    }
}