package com.bashsupn.projectschedule.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUser : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        prefManager = PrefManager(this)
        detailUser()

        val btnUpdate = btn_update_user
        btnUpdate.setOnClickListener {
            updateUser()
        }

        val btnDelete = btn_delete_user
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus User")
                .setMessage("Apakah anda yakin ingin menghapus user ini?")
                .setPositiveButton("Ya") { dialog, which ->
                    deleteUser()
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun deleteUser() {
        val id = intent.extras?.getString("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.deleteUser(it.toInt()) }
        callData?.enqueue(object : Callback<FormResponse> {
            override fun onResponse(
                call: Call<FormResponse>,
                response: Response<FormResponse>
            ) {
                Log.d("TAG", "onResponse: ${response.body()}")
                val data = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateUser, data?.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateUser, UsersActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@UpdateUser, "Gagal menghapus User", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@UpdateUser, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUser() {
        val etNama = et_nama_executive.text.toString()
        val etEmail = et_email.text.toString()
        val etPassword = et_password.text.toString()
        val etPasswordConfirm = et_confirm_password.text.toString()

        val id = intent.extras?.getString("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.updateUser(it.toInt(), etNama, etEmail, etPassword) }

        if (etPassword == etPasswordConfirm){
            callData?.enqueue(object : Callback<FormResponse> {
                override fun onResponse(
                    call: Call<FormResponse>,
                    response: Response<FormResponse>
                ) {
                    Log.d("TAG", "onResponse: ${response.body()}")
                    val data = response.body()
                    if (response.isSuccessful) {
                        Toast.makeText(this@UpdateUser, data?.message, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this@UpdateUser, "Gagal Update User", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                    Toast.makeText(this@UpdateUser, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }


    }

    private fun detailUser() {
        val etNama: TextInputEditText = et_nama_executive
        val etEmail: TextInputEditText = et_email
        val etRole: TextInputEditText = et_role

        etNama.setText(intent.getStringExtra("name"))
        etEmail.setText(intent.getStringExtra("email"))
        etRole.setText(intent.getStringExtra("role"))
    }
}