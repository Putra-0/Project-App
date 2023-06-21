package com.bashsupn.projectschedule.manager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.UsersResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_update_project.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UpdateProject : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var dropdown: MaterialAutoCompleteTextView
    private var selectedUserId: Int = 0
    private lateinit var datePickerLayout: TextInputLayout
    private lateinit var etDate: TextInputEditText
    private val calendar = Calendar.getInstance()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_project)

        prefManager = PrefManager(this)

        val btnUpdate = btn_tambah
        btnUpdate.setOnClickListener {
            updateProject(selectedUserId, selectedDate)
        }

        detailProject()
        setUserName()

        datePickerLayout = findViewById(R.id.datePickerLayout)
        etDate = findViewById(R.id.et_date)

        etDate.setOnClickListener { showDatePickerDialog() }
    }

    private fun detailProject() {
        val etUser: MaterialAutoCompleteTextView = userInput
        val etName: TextInputEditText = et_name_project
        val etClient: TextInputEditText = et_client_name
        val etAddress: TextInputEditText = et_address
        val etStatus: MaterialAutoCompleteTextView? = et_status
        val etDate: TextInputEditText = et_date

        etUser.setText(intent.extras?.getString("user"))
        etName?.setText(intent.getStringExtra("name"))
        etClient?.setText(intent.getStringExtra("client"))
        etAddress?.setText(intent.getStringExtra("address"))
        etStatus?.setText(intent.getStringExtra("status"))
        etDate?.setText(intent.getStringExtra("start_date"))
    }

    private fun setUserName() {
        val api = RClient.Create(this)
        val callData = api.getUsers()
        dropdown = userInput

        callData.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>,
            ) {
                val userlist = response.body()
                if (userlist != null) {
                    val filterUser = userlist.filter { it.role_id == 2 }
                    val username = filterUser.map { it.name }
                    val userIds = filterUser.map { it.id }
                    val adapter = ArrayAdapter(
                        this@UpdateProject,
                        android.R.layout.simple_spinner_dropdown_item,
                        username
                    )
                    dropdown.setAdapter(adapter)
                    dropdown.setOnItemClickListener { parent, view, position, id ->
                        selectedUserId = userIds[position]
                    }
                } else {
                    Log.e("ERROR", "Userlist is null")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }

    private fun updateProject(selectedUserId: Int, selectedDate: String?) {
        val id = intent.extras?.getInt("id")
        val name = et_name_project.text.toString()
        val client = et_client_name.text.toString()
        val address = et_address.text.toString()
        val status = et_status.text.toString()

        if (id == null) {
            Toast.makeText(this, "Invalid project ID", Toast.LENGTH_SHORT).show()
            return
        }

        val api = RClient.Create(this)
        val callData = api.updateProject(id, selectedUserId, name, client, address, status, selectedDate)

        callData.enqueue(object : Callback<FormResponse> {
            override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                Log.d("TAG", "onResponse: ${response.body()}")
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    onBackPressed()
                } else {
                    Toast.makeText(this@UpdateProject, "Gagal update Project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@UpdateProject, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                etDate.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(selectedDate.time)
    }
}
