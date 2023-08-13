package com.bashsupn.projectschedule.executive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_update_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        btn_update.setOnClickListener {
            updatePass()
        }
    }

    private fun updatePass() {
        val oldpass = etOldpassword.text.toString()
        val newpass = etPassword.text.toString()
        val api = RClient.Create(this)

        if (oldpass.isEmpty()) {
            etOldpassword.error = "Please enter your old password"
            etOldpassword.requestFocus()
            return
        }

        if (newpass.isEmpty()) {
            etPassword.error = "Please enter your new password"
            etPassword.requestFocus()
            return
        }

        if (oldpass == newpass) {
            etPassword.error = "New password cannot be the same as old password"
            etPassword.requestFocus()
            return
        }

        val callData = api.updatePassword(oldpass, newpass)
        callData.enqueue(object : Callback<FormResponse> {
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                Log.e("UpdatePassword", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UpdatePassword,
                        "Berhasil update password",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                else{
                    Toast.makeText(
                        this@UpdatePassword,
                        "Gagal update password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@UpdatePassword, "Gagal update password", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}