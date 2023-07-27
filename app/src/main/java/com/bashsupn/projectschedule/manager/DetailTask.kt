package com.bashsupn.projectschedule.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.Task
import com.bashsupn.projectschedule.models.TaskResponse
import kotlinx.android.synthetic.main.activity_detail_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTask : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            detilTask()
            swipeRefreshLayout.isRefreshing = false
        }

        detilTask()
    }

    private fun detilTask() {
        val id = intent.extras?.getString("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.getTask(it.toInt()) }

        callData?.enqueue(object : Callback<TaskResponse>{
            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful){
                    val task = response.body()?.data
                    showTask(task)

                }
            }

            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showTask(task: Task?) {
        val name = mTitle
        val description = et_description
        val duration = et_duration
        val dependency = et_dependency
        val es = et_ES
        val ef = et_EF
        val ls = et_LS
        val lf = et_LF
        val slack = et_Slack
        val start = et_start
        val end = et_end
        val status = status

        name.text = task?.name
        description.text = "Description   : "+if(task?.description.toString() == "null") "No Description" else task?.description.toString()
        duration.text = "Duration        : "+task?.duration.toString()
        dependency.text = "Dependency : "+task?.dependencies
        es.text = "ES : "+task?.early_start.toString()
        ef.text = "EF : "+task?.early_finish.toString()
        ls.text = "LS : "+task?.late_start.toString()
        lf.text = "LF : "+task?.late_finish.toString()
        slack.text = "Slack : "+task?.slack.toString()
        start.text = "Start Date : "+task?.start_date.toString()
        end.text = "End Date : "+task?.end_date.toString()
        status.text = "Status : "+task?.status.toString()

    }
}