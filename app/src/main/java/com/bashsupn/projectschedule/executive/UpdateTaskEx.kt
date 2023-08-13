package com.bashsupn.projectschedule.executive

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.Task
import com.bashsupn.projectschedule.models.TaskResponse
import kotlinx.android.synthetic.main.activity_update_task_ex.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTaskEx : AppCompatActivity() {

    private var task: Task? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task_ex)

        btn_update.setOnClickListener {
            updateProgress()
        }

        getTask()
    }

    private fun getTask(){
        val idP = intent.extras?.getInt("idP")
        val api = RClient.Create(this)
        val callData = idP?.let { api.getTask(it) }

        callData?.enqueue(object : Callback<TaskResponse>{
            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful){
                    task = response.body()?.data
                }
            }

            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateProgress(){
        val id = intent.extras?.getInt("id")
        val progress = etProgress.text.toString().toInt()

        val api = RClient.Create(this)
        val callData = id?.let { api.updateProgress(it, progress)}
        callData?.enqueue(object : Callback<FormResponse> {
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateTaskEx, "Berhasil update progress", Toast.LENGTH_SHORT).show()
                    updateStatus()
                    onBackPressed()
                } else {
                    Toast.makeText(this@UpdateTaskEx, "Failed update progress", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@UpdateTaskEx, "Failed update progress", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateStatus(){
        val id = intent.extras?.getInt("id")
        val api = RClient.Create(this)
        val progress = task?.progress
        var status = ""

        if(progress!! < 100 ){
            status = "In Progress"
        }else if(progress == 100){
            status = "Completed"
        }

        val callData = id?.let { api.updateTask(it, status) }

        callData?.enqueue(object : Callback<FormResponse>{
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                if (response.isSuccessful){
                    Log.d("Update Status", "Success")
                }else{
                    Toast.makeText(this@UpdateTaskEx, "Failed update status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@UpdateTaskEx, "Failed update status", Toast.LENGTH_SHORT).show()
            }

        })
    }
}

