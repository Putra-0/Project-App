package com.bashsupn.projectschedule.executive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.databinding.ActivityUpdateTaskExBinding
import com.bashsupn.projectschedule.models.FormResponse
import kotlinx.android.synthetic.main.activity_update_task_ex.autoCompleteTextView
import kotlinx.android.synthetic.main.activity_update_task_ex.btn_update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTaskEx : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskExBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskExBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stat = intent?.extras?.getString("status")
        stat?.let {
            binding.autoCompleteTextView.setText(it)
        }

        val status = resources.getStringArray(R.array.dropdown_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.status_down, status)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedValue = parent.getItemAtPosition(position).toString()

            binding.btnUpdate.setOnClickListener {
                val id = intent?.extras?.getInt("id")
                val api = RClient.Create(this)
                val callData = id?.let { api.updateTask(it.toInt(), selectedValue) }
                callData?.enqueue(object : Callback<FormResponse> {
                    override fun onResponse(
                        call: Call<FormResponse>,
                        response: Response<FormResponse>,
                    ) {
                        if (response.isSuccessful) {
                            onBackPressed()
                            Toast.makeText(this@UpdateTaskEx, "Status Updated", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }
}

