package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_add_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUser : AppCompatActivity() {

    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        prefManager = PrefManager(this)
        saveUser()
    }

    private fun saveUser() {

        val etNama = et_nama_executive
        val etEmail = et_email
        val etPassword = et_password
        val etPasswordConfirm = et_confirm_password
        val btnSave = btn_tambah_executive

        btnSave.setOnClickListener{
            val nama = etNama.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val passwordConfirm = etPasswordConfirm.text.toString()

            val api = RClient.Create(this)
            val callData = api.addUser(nama, email, password)

            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
            }
            else if (email.isEmpty()) {
                etEmail.error = "Email tidak boleh kosong"
            }
            else if (password.isEmpty()) {
                etPassword.error = "Password tidak boleh kosong"

            } else {
                callData.enqueue(object : Callback<FormResponse> {
                    override fun onResponse(
                        call: Call<FormResponse>,
                        response: Response<FormResponse>,
                    ) {
                        val data = response.body()
                        if (data?.status == true) {
                            Toast.makeText(this@AddUser, data.message, Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(this@AddUser, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                        Toast.makeText(this@AddUser, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }
}