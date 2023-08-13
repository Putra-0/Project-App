package com.bashsupn.projectschedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.executive.DashboardEx
import com.bashsupn.projectschedule.manager.DashboardmActivity
import com.bashsupn.projectschedule.models.LoginResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PrefManager(this)

        checkLogin()
        Auth()
    }

    private fun Auth() {
        prefManager = PrefManager(this)

        btnLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                etEmail.error = "Email not empty"
                etPassword.error = "Password not empty"
                return@setOnClickListener
            }

            val api = RClient.Create(this)
            api.login(email,password).enqueue((object: Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>,
                ) {
                    val respon = response.body()
                    if (respon?.status  == "success" && respon.user.role_id == 1){
                        Toast.makeText(this@LoginActivity, respon.message, Toast.LENGTH_LONG).show()
                        prefManager.saveAccessToken(respon.token)
                        prefManager.setLoggin(true)
                        prefManager.setrole(1)
                        prefManager.setUserName(respon.user.name)
                        Intent(this@LoginActivity, DashboardmActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }
                    } else if (respon?.status == "success" && respon.user.role_id == 2){
                        Toast.makeText(this@LoginActivity, respon.message, Toast.LENGTH_LONG).show()
                        prefManager.saveAccessToken(respon.token)
                        prefManager.setUserId(respon.user.id)
                        prefManager.setUserName(respon.user.name)
                        prefManager.setLoggin(true)
                        prefManager.setrole(2)
                        Intent(this@LoginActivity, DashboardEx::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email or Password is wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }))
        }
    }

    private fun checkLogin() {
        val token = prefManager.fetchAccessToken()

        if (token != null) {
            if (prefManager.isLogin()!!  && prefManager.getrole() == 1 ){
                Intent(this, DashboardmActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    finish()
                }
            } else if (prefManager.isLogin()!! && prefManager.getrole() == 2) {
                Intent(this, DashboardEx::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    finish()
                }
            }
        }
    }
}