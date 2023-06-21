package com.bashsupn.projectschedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PrefManager(this)

        val token = prefManager.fetchAccessToken()

        // Cek token di session manager, jika tidak ada tampilkan login activity
        if(token == null) {
            toLogin()
        }

        btnLogout.setOnClickListener {
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
}