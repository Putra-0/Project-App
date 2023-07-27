package com.bashsupn.projectschedule.manager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.adapter.AdapterTask
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.CPMResponse
import com.bashsupn.projectschedule.models.Project
import com.bashsupn.projectschedule.models.ProjectResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_detail_project.add_task
import kotlinx.android.synthetic.main.activity_detail_project.swipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailProject : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapterTask: AdapterTask
    private lateinit var rvProjects: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_project)

        prefManager = PrefManager(this)
        detailProject()
        cpm()
        swipeRefreshLayout = swipe
        rvProjects = findViewById(R.id.rv_user)
        setupRecyclerView()
        swipeRefreshLayout.setOnRefreshListener {
            detailProject()
            cpm()
            swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun cpm() {
        val id = intent.extras?.getString("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.getCPM(it.toInt()) }

        callData?.enqueue(object : Callback<CPMResponse> {
            override fun onResponse(
                call: Call<CPMResponse>,
                response: Response<CPMResponse>
            ) {
                if (response.isSuccessful) {
                    val cpmResponse = response.body()
                    cpmResponse?.let {
                        val cpm = it.critical_path
                        val total = it.total_duration_critical_path
                        val etCPM : TextView = findViewById(R.id.cpm)
                        val etTot: TextView = findViewById(R.id.total)
                        etCPM.text = "Critical Path : "+ cpm
                        etTot.text = "Total Duration : "+ total
                    }

                } else {
                    Log.e("Response Error", response.message())
                }
            }

            override fun onFailure(call: Call<CPMResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }

    private fun setupRecyclerView() {
        adapterTask = AdapterTask(ArrayList())
        rvProjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailProject)
        }
        rvProjects.adapter = adapterTask
    }

    private fun detailProject() {
        val id = intent.extras?.getString("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.getProject(it.toInt()) }

        callData?.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    projectResponse?.let {
                        val project = it.data
                        adapterTask.updateData(project.tasks)
                        showProjectData(project)
                    }
                } else {
                    Log.e("Response Error", response.message())
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
        if (prefManager.getrole() == 2){
            add_task.visibility = android.view.View.GONE
        }
        add_task.setOnClickListener{
            val intent = Intent(this, AddTask::class.java)
            intent.putExtra("id", id)
            startActivity(intent)

            Log.d("TAG", "showProjectData: $id")

        }
    }

    @SuppressLint("SetTextI18n")
    private fun showProjectData(project: Project) {
        val etProject: TextView = findViewById(R.id.mTitle)
        val etEx: TextView = findViewById(R.id.user)
        val etStart: TextView = findViewById(R.id.start)
        val etEnd: TextView = findViewById(R.id.end)
        val etDesc: TextView = findViewById(R.id.description)
        val etStatus: TextView = findViewById(R.id.status)
        val etClient: TextView = findViewById(R.id.client)
        val etAddress: TextView = findViewById(R.id.address)

        etProject.text =  project.name
      etEx.text = "Executive : "+ project.user.name
        etStart.text = "Start Date : "+ if (project.start_date == null){
            ""
        } else {
            project.start_date.toString()
        }
        etEnd.text = "End Date : " + if (project.end_date == null){
            ""
        }else{
            project.end_date.toString()
        }
        etDesc.text = "Description : "+ if(project.description == null){
            ""
        }else{project.description.toString()}
        etStatus.text = "Status : "+project.status
        etClient.text = "Client : "+project.client_name
        etAddress.text = "Address : "+ project.address

    }
}
