package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bashsupn.projectschedule.LoginActivity
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_dashboardm.*

class DashboardmActivity : AppCompatActivity() {

    lateinit var prefManager: PrefManager
    private val TIME_INTERVAL = 3000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboardm)

        users.setOnClickListener{
            val Intent = Intent(this,UsersActivity::class.java)
            startActivity(Intent)
        }

        projects.setOnClickListener{
            val Intent = Intent(this,ProjectsActivity::class.java)
            startActivity(Intent)
        }

        schedules.setOnClickListener{
            val Intent = Intent(this,Schedules::class.java)
            startActivity(Intent)
        }

        prefManager = PrefManager(this)
        val token = prefManager.fetchAccessToken()
        val name = prefManager.getUserName()
        textView.text = "Welcome Back, $name"

        if (token != null) {
        } else {
            toLogin()
        }
        logout.setOnClickListener {
            toLogin()
        }

    }

    fun toLogin() {
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
            Toast.makeText(baseContext, "Ketuk sekali lagi untuk Keluar", Toast.LENGTH_SHORT)
                .show()
        }
        mBackPressed = System.currentTimeMillis()
    }
}