package com.bashsupn.projectschedule.manager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.FormResponse
import com.bashsupn.projectschedule.models.UsersResponse
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_project.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddProject : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private var selectedUserId: Int = -1
    private lateinit var datePickerLayout: TextInputLayout
    private lateinit var etDate: TextInputEditText
    private val calendar = Calendar.getInstance()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        prefManager = PrefManager(this)

        datePickerLayout = findViewById(R.id.datePickerLayout)
        etDate = findViewById(R.id.et_date)

        etDate.setOnClickListener { showDatePickerDialog() }

        setUserName()

        btn_tambah.setOnClickListener {
            if (selectedUserId != -1) {
                saveProject(selectedUserId, selectedDate)
            } else {
                Toast.makeText(this@AddProject, "Please select a user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserName() {
        val api = RClient.Create(this)
        val callData = api.getUsers()
        val dropdown = userInput

        callData.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>,
            ) {
                val userlist = response.body()
                if (userlist != null) {
                    val filterUser = userlist.filter { it.role_id == 2 }
                    val username = filterUser.map { it.name }.toTypedArray()
                    val adapter = ArrayAdapter(this@AddProject, android.R.layout.simple_spinner_dropdown_item, username)
                    dropdown.setAdapter(adapter)
                    dropdown.setOnItemClickListener { parent, view, position, id ->
                        val selectedUser = filterUser[position]
                        selectedUserId = selectedUser.id
                    }
                } else {
                    Toast.makeText(this@AddProject, "Failed to get user list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                Toast.makeText(this@AddProject, "Failed to get user list: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveProject(selectedUserId: Int, selectedDate: String?) {
        val etName = et_name_project
        val etClient = et_client_name
        val etAddress = et_address
        val etDesc = et_description

        val name = etName.text.toString()
        val client = etClient.text.toString()
        val address = etAddress.text.toString()
        val desc = etDesc.text.toString()


        if (name.isEmpty()) {
            etName.error = "Project name cannot be empty"
            return
        }

        if (client.isEmpty()) {
            etClient.error = "Client name cannot be empty"
            return
        }

        if (address.isEmpty()) {
            etAddress.error = "Address cannot be empty"
            return
        }

        val api = RClient.Create(this)
        val callData = api.addProject(selectedUserId, name, client, address,desc, selectedDate)

        callData.enqueue(object : Callback<FormResponse> {
            override fun onResponse(
                call: Call<FormResponse>,
                response: Response<FormResponse>,
            ) {
                val data = response.body()
                if (data?.status == true) {
                    Toast.makeText(this@AddProject, data.message, Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this@AddProject, "Failed to add project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                Toast.makeText(this@AddProject, "Failed to add project: ${t.message}", Toast.LENGTH_SHORT).show()
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

