package com.bashsupn.projectschedule.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.manager.DetailProject
import com.bashsupn.projectschedule.models.Project
import com.bashsupn.projectschedule.models.Projects

class AdapterProjectEx(private val dataList: ArrayList<Project>) : RecyclerView.Adapter<AdapterProjectEx.ViewHolderData>(){


    class ViewHolderData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.mTitle)
        val user: TextView = itemView.findViewById(R.id.mTgl)
        val client: TextView = itemView.findViewById(R.id.mClient)
        val status: TextView = itemView.findViewById(R.id.mStatus)
        val mMenu : ImageView = itemView.findViewById(R.id.mMenus)
        val context: Context = itemView.context

        init {
            mMenu.visibility = View.GONE
            user.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_project, parent, false)
        return ViewHolderData(layout)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        val item = dataList[position]
        holder.nama.text = item.name
        holder.client.text = "Client: " + item.client_name
        holder.status.text = "Status: " + item.status

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, DetailProject::class.java)
            intent.putExtra("id", item.id.toString())

            holder.context.startActivity(intent)
        }
    }

    fun updateData(newData: List<Project>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

}