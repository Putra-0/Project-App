package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.adapter.AdapterProject
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.ProjectsResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_projects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectsActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapterProject: AdapterProject
    private lateinit var rvProjects: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        prefManager = PrefManager(this)
        getProjects()
        swipeRefreshLayout = findViewById(R.id.swipe)
        rvProjects = findViewById(R.id.rv_user)

        setupRecyclerView()
        swipeRefreshLayout.setOnRefreshListener {
            getProjects()
            swipeRefreshLayout.isRefreshing = false
        }




        val btnAdd = addingBtn
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddProject::class.java)
            startActivity(intent)
        }
    }


    private fun setupRecyclerView() {
        adapterProject = AdapterProject(ArrayList())
        rvProjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProjectsActivity)
        }
        rvProjects.adapter = adapterProject
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
                        adapterProject.updateData(projects)
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