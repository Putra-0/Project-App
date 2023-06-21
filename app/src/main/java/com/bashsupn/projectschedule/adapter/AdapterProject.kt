package com.bashsupn.projectschedule.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.manager.DetailProject
import com.bashsupn.projectschedule.manager.ProjectsActivity
import com.bashsupn.projectschedule.manager.UpdateProject
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.Projects
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterProject(private val dataList: ArrayList<Projects>) : RecyclerView.Adapter<AdapterProject.ViewHolderData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderData {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_project, parent, false)
        return ViewHolderData(layout)
    }

    override fun onBindViewHolder(holder: ViewHolderData, position: Int) {
        val item = dataList[position]
        holder.nama.text = item.name
        holder.user.text = "Executive: " + item.user.name
        holder.client.text = "Client: " + item.client_name
        holder.status.text = "Status: " + item.status

        holder.showPopupMenu.setOnClickListener {
            showPopupMenu(holder.showPopupMenu, position)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, DetailProject::class.java)
            intent.putExtra("id", item.id.toString())

            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataList.size

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.show_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.editText -> {

                    val intent = Intent(view.context, UpdateProject::class.java)
                    val project = dataList[position]
                    intent.putExtra("id", project.id)
                    intent.putExtra("name", project.name)
                    intent.putExtra("client", project.client_name)
                    intent.putExtra("address", project.address)
                    intent.putExtra("user", project.user.name)
                    intent.putExtra("status", project.status)
                    intent.putExtra("start_date", project.start_date.toString())
                    startActivity(view.context, intent, null) // Menggunakan startActivity dari ContextCompat
                    true
                }
                R.id.deletep -> {
                    AlertDialog.Builder(view.context)
                        .setTitle("Delete Project")
                        .setMessage("Are you sure you want to delete this project?")
                        .setPositiveButton("Yes") { dialog, which ->
                            val project = dataList[position]
                            val idP = project.id
                            deleteProject(view.context, idP)
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

    private fun deleteProject(context: Context, idP: Int) {
        val api = RClient.Create(context)
        val callData = api.deleteProject(idP)
        callData.enqueue(object : Callback<FormResponse> {
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                Log.d("TAG", "onResponse: ${response.body()}")
                val data = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Berhasil menghapus Project", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ProjectsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Gagal menghapus Project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateData(newData: List<Projects>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolderData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.mTitle)
        val user: TextView = itemView.findViewById(R.id.mTgl)
        val client: TextView = itemView.findViewById(R.id.mClient)
        val status: TextView = itemView.findViewById(R.id.mStatus)
        val context: Context = itemView.context
        val showPopupMenu: ImageView = itemView.findViewById(R.id.mMenus)
    }
}
