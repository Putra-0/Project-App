package com.bashsupn.projectschedule.manager

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_add_task.btn_tambah
import kotlinx.android.synthetic.main.activity_add_task.et_dependency
import kotlinx.android.synthetic.main.activity_add_task.et_description
import kotlinx.android.synthetic.main.activity_add_task.et_duration
import kotlinx.android.synthetic.main.activity_add_task.et_nama_Activity
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
        val etdescription = et_description
        val btnSave = btn_tambah

        btnSave.setOnClickListener{
            val nama = etNama.text.toString()
            val duration = etDuration.text.toString().toInt()
            val dependency = etDependency?.text?.toString() ?: ""
            val description = etdescription?.text?.toString() ?: ""

            if(nama.isEmpty()){
                etNama.error = "Nama Activity tidak boleh kosong"
                etNama.requestFocus()
                return@setOnClickListener
            }
            if(duration.toString().isEmpty()){
                etDuration.error = "Duration tidak boleh kosong"
                etDuration.requestFocus()
                return@setOnClickListener
            }


            val id = intent.getStringExtra("id")
            val api = RClient.Create(this)
            Log.e("id", id.toString())
                val callData = id?.let { api.addTask(it.toInt(), nama, duration, dependency, description) }
                callData?.enqueue(object : Callback<FormResponse> {
                    override fun onResponse(
                        call: Call<FormResponse>,
                        response: Response<FormResponse>
                    ) {
                        Log.e("response", response.body().toString())
                        val data = response.body()
                        if (data?.status == true) {
                            Toast.makeText(this@AddTask, "Berhasil menambahkan Activity", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(this@AddTask, "Gagal menambahkan Activity", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                        Toast.makeText(this@AddTask, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }
}
