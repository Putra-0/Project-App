package com.bashsupn.projectschedule.executive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.adapter.AdapterProject
import com.bashsupn.projectschedule.adapter.AdapterProjectEx
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.ProjectExResponse
import com.bashsupn.projectschedule.models.ProjectsResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectsEx : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapterProject: AdapterProjectEx
    private lateinit var rvProjects: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_ex)

        prefManager = PrefManager(this)
        getProjects()
        swipeRefreshLayout = findViewById(R.id.swipe)
        rvProjects = findViewById(R.id.rv_user)

        setupRecyclerView()
        swipeRefreshLayout.setOnRefreshListener {
            getProjects()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        adapterProject = AdapterProjectEx(ArrayList())
        rvProjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProjectsEx)
        }
        rvProjects.adapter = adapterProject
    }

    private fun getProjects() {
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
                        adapterProject.updateData(project)
                        Log.e("Data", project.toString())
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
}