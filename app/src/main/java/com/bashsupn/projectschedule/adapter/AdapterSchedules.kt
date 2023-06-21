package com.bashsupn.projectschedule.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.manager.ScheduleProject
import com.bashsupn.projectschedule.models.Projects

class AdapterSchedules(private val dataList: ArrayList<Projects>) : RecyclerView.Adapter<AdapterSchedules.ViewHolderData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user, parent, false)
        return ViewHolderData(layout)
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        val item = dataList[position]
        holder.nama.text = item.name
        holder.user.text = "Executive: " + item.user.name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, ScheduleProject::class.java)
            intent.putExtra("id", item.id.toString())

            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newData: List<Projects>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolderData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.mTitle)
        val user: TextView = itemView.findViewById(R.id.mTgl)
        val context: Context = itemView.context
    }
}
