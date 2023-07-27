package com.bashsupn.projectschedule

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // delay 2s

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Cek koneksi internet sebelum melanjutkan ke LoginActivity
            if (isConnected()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // Tampilkan pesan atau tindakan jika tidak ada koneksi internet
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
            finish()
        }, SPLASH_TIME_OUT)
    }

    // Fungsi untuk memeriksa koneksi internet
    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
