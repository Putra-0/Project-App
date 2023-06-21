package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.adapter.AdapterUser
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.UsersResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        getUser()

        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            getUser()
            swipeRefreshLayout.isRefreshing=false
        }

        val btnAdd = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.adBtnPelaksana)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
        }

    }

    private fun getUser() {
        prefManager = PrefManager(this)
        val api = RClient.Create(this)
        val listData = ArrayList<UsersResponse>()
        val rvuser = findViewById<RecyclerView>(R.id.rv_user)
        rvuser.setHasFixedSize(true)
        rvuser.layoutManager = LinearLayoutManager(this)
        val callData = api.getUsers()

        callData.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>,
            ) {
                val data = response.body()
                data?.let { listData.addAll(it) }
                val adapterData = AdapterUser(
                    listData
                )
                rvuser.adapter = adapterData
                Log.e("Data", data.toString())

            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }

        })
    }
}