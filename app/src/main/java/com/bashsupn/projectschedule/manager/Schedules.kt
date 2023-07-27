package com.bashsupn.projectschedule.manager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.adapter.AdapterSchedules
import com.bashsupn.projectschedule.adapter.AdapterSchedulesEx
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.ProjectExResponse
import com.bashsupn.projectschedule.models.ProjectsResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Schedules : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var AdapterSchedules: AdapterSchedules
    private lateinit var AdapterSchedulesEx: AdapterSchedulesEx
    private lateinit var rvProjects: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)

        prefManager = PrefManager(this)
        if (prefManager.getrole() == 1){
            getProjects()
        }else{
            getProject()
        }
        swipeRefreshLayout = findViewById(R.id.swipe)
        rvProjects = findViewById(R.id.rv_users)

        setupRecyclerView()
        swipeRefreshLayout.setOnRefreshListener {
            getProjects()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getProject() {
        val api = RClient.Create(this)
        val id = prefManager.getUserId()
        val callData = id.let { api.getProjects(it) }

        callData.enqueue(object : Callback<ProjectExResponse> {
            override fun onResponse(
                call: Call<ProjectExResponse>,
                response: Response<ProjectExResponse>,
            ) {
                if (response.isSuccessful) {
                    val projectsResponse = response.body()
                    projectsResponse?.let {
                        val project = it.data
                        AdapterSchedulesEx.updateDatas(project)
                    }
                } else {
                    Log.e("Response Error", response.message())
                }
            }

            override fun onFailure(call: Call<ProjectExResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }


    private fun setupRecyclerView() {
        AdapterSchedules = AdapterSchedules(ArrayList())
        AdapterSchedulesEx = AdapterSchedulesEx(ArrayList())
        rvProjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@Schedules)
        }
        if (prefManager.getrole() == 1){
            rvProjects.adapter = AdapterSchedules
        }else{
            rvProjects.adapter = AdapterSchedulesEx
        }
    }

    private fun getProjects() {
        val api = RClient.Create(this)
        val callData = api.getProjects()

        callData.enqueue(object : Callback<ProjectsResponse> {
            override fun onResponse(
                call: Call<ProjectsResponse>,
                response: Response<ProjectsResponse>,
            ) {
                if (response.isSuccessful) {
                    val projectsResponse = response.body()
                    projectsResponse?.let {
                        val projects = it.data
                        AdapterSchedules.updateData(projects)
                        Log.e("Data", projects.toString())
                    }
                } else {
                    Log.e("Response Error", response.message())
                }
            }

            override fun onFailure(call: Call<ProjectsResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }
}