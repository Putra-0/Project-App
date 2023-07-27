package com.bashsupn.projectschedule.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.executive.UpdateTaskEx
import com.bashsupn.projectschedule.manager.DetailProject
import com.bashsupn.projectschedule.manager.DetailTask
import com.bashsupn.projectschedule.manager.UpdateTask
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.Task
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterTask(private val dataList: ArrayList<Task>) : RecyclerView.Adapter<AdapterTask.ViewHolderData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_activity, parent, false)
        return ViewHolderData(layout)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        val item = dataList[position]
        holder.nama.text = item.name
        holder.duration.text = "Duration : "+item.duration.toString()
        holder.dependencies.text = "Dependencies : "+item.dependencies.toString()

        holder.showPopupMenu.setOnClickListener {
            showPopupMenu(holder.showPopupMenu, position)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, DetailTask::class.java)
            intent.putExtra("id", item.id.toString())

            holder.context.startActivity(intent)

        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.show_menu, popupMenu.menu)
        val prefManager = PrefManager(view.context)
        if (prefManager.getrole() == 2) {
            popupMenu.menu.removeItem(R.id.deletep)
        }
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.editText -> {
                    if (prefManager.getrole() == 1){
                        val intent = Intent(view.context, UpdateTask::class.java)
                        val task = dataList[position]
                        intent.putExtra("id", task.id)
                        intent.putExtra("idP", task.job_id)
                        intent.putExtra("name", task.name)
                        intent.putExtra("duration", task.duration.toString())
                        intent.putExtra("dependencies", task.dependencies)
                        if(task.description != null){
                            intent.putExtra("description", task.description.toString())
                        }
                        startActivity(view.context, intent, null) // Menggunakan startActivity dari ContextCompat
                    }else if (prefManager.getrole() == 2){
                        val intent = Intent(view.context, UpdateTaskEx::class.java)
                        val task = dataList[position]
                        intent.putExtra("id", task.id)
                        intent.putExtra("status", task.status)
                        intent.putExtra("idP", task.job_id)

                        startActivity(view.context, intent, null) // Menggunakan startActivity dari ContextCompat
                    }
                    true
                }
                R.id.deletep -> {
                    AlertDialog.Builder(view.context)
                        .setTitle("Delete Project")
                        .setMessage("Are you sure you want to delete this project?")
                        .setPositiveButton("Yes") { dialog, which ->
                            val task = dataList[position]
                            val idP = task.id
                            deleteTask(view.context, idP)
                        }
                        .setNegativeButton("No", null)
                        .show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteTask(context: Context, idP: Int) {
        val api = RClient.Create(context)
        val call = api.deleteTask(idP)
        call.enqueue(object:Callback<FormResponse>{
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(context, "Berhasil menghapus Data", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(context, "Gagal menghapus Data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    fun updateData(tasks: List<Task>) {
        dataList.clear()
        dataList.addAll(tasks)
        notifyDataSetChanged()
    }

    class ViewHolderData(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nama: TextView =itemView.findViewById(R.id.mTitle)
        val duration: TextView =itemView.findViewById(R.id.duration)
        val dependencies: TextView =itemView.findViewById(R.id.dependency)
        val showPopupMenu: ImageView =itemView.findViewById(R.id.mMenus)
        val context = itemView.context
    }
}