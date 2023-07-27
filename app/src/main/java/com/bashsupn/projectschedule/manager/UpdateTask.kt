package com.bashsupn.projectschedule.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_update_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTask : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)
        prefManager = PrefManager(this)
        detailTask()
        val btnUpdate = btn_update
        btnUpdate.setOnClickListener {
            updateTask()
        }
    }

    private fun updateTask() {
        val id = intent.extras?.getInt("id")
        val idP = intent.extras?.getInt("idP")
        val nama = et_nama_Activity.text.toString()
        val duration = et_duration.text.toString().toInt()
        val dependency = et_dependency?.text?.toString() ?: ""
        val description = et_description?.text?.toString() ?: ""

        if (idP == null) {
            Toast.makeText(this, "Invalid project ID", Toast.LENGTH_SHORT).show()
            return
        }

        val api = RClient.Create(this)
        val callData = id?.let { api.updateTask(it, idP, nama, duration, dependency, description)}

        callData?.enqueue(object : Callback<FormResponse>{
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateTask, "Berhasil update task", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this@UpdateTask, "Failed update task", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun detailTask() {
        val etName: TextInputEditText = et_nama_Activity
        val etDuration: TextInputEditText = et_duration
        val etDependency: TextInputEditText = et_dependency
        val etDesc : TextInputEditText = et_description

        etName.setText(intent.extras?.getString("name"))
        etDuration.setText(intent.extras?.getString("duration"))
        etDependency.setText(intent.extras?.getString("dependencies"))
        etDesc.setText(intent.extras?.getString("description"))
    }
}