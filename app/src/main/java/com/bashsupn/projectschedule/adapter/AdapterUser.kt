package com.bashsupn.projectschedule.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.manager.UpdateUser
import com.bashsupn.projectschedule.models.UsersResponse

class AdapterUser(private val dataList: ArrayList<UsersResponse>): RecyclerView.Adapter<AdapterUser.ViewHolderData>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user,parent,false)

        return ViewHolderData(layout)
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {

        val item=dataList[position]
        holder.nama.text=item.name
        holder.email.text=item.role.role
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, UpdateUser::class.java)

            intent.putExtra("id", item.id.toString())
            intent.putExtra("name", item.name)
            intent.putExtra("email", item.email)
            intent.putExtra("role", item.role.role)
            holder.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolderData(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nama: TextView =itemView.findViewById(R.id.mTitle)
        val email: TextView =itemView.findViewById(R.id.mTgl)
        val context = itemView.context
    }

}