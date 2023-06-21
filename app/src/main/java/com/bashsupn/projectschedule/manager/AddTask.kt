package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_add_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTask : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        prefManager = PrefManager(this)

        saveTask()
    }

    private fun saveTask() {
        val etNama = et_nama_Activity
        val etDuration = et_duration
        val etDependency = et_dependency
        val btnSave = btn_tambah

        btnSave.setOnClickListener{
            val nama = etNama.text.toString()
            val duration = etDuration.text.toString().toInt()
            val dependency = etDependency.text.toString()
            val id = intent.getStringExtra("id")
            val api = RClient.Create(this)
            Log.e("id", id.toString())
                val callData = id?.let { api.addTask(it.toInt(), nama, duration, dependency) }
                callData?.enqueue(object : Callback<FormResponse> {
                    override fun onResponse(
                        call: Call<FormResponse>,
                        response: Response<FormResponse>
                    ) {
                        Log.e("response", response.body().toString())
                        val data = response.body()
                        if (data?.status == true) {
                            onBackPressed()
                        } else {
                            Toast.makeText(this@AddTask, "Failed add Activity", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                        Toast.makeText(this@AddTask, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }
}
