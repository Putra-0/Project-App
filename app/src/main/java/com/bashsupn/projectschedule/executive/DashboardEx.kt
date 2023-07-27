package com.bashsupn.projectschedule.executive

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bashsupn.projectschedule.LoginActivity
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.manager.Schedules
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_dashboard_ex.projects
import kotlinx.android.synthetic.main.activity_dashboardm.logout
import kotlinx.android.synthetic.main.activity_dashboardm.schedules

class DashboardEx : AppCompatActivity() {

    lateinit var prefManager: PrefManager
    private val TIME_INTERVAL = 3000
    private var mBackPressed: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_ex)

        prefManager = PrefManager(this)
        val token = prefManager.fetchAccessToken()
        if (token != null) {
        } else {
            toLogin()
        }
        logout.setOnClickListener {
            toLogin()
        }

        projects.setOnClickListener{
            val Intent = Intent(this, ProjectsEx::class.java)
            startActivity(Intent)
        }

        schedules.setOnClickListener{
            val Intent = Intent(this, Schedules::class.java)
            startActivity(Intent)
        }


    }

    private fun toLogin() {
        prefManager.deleteAccessToken()

        Intent(this, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Press back again to exit",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
        mBackPressed = System.currentTimeMillis()
    }
}