package com.bashsupn.projectschedule.manager

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bashsupn.projectschedule.R
import com.bashsupn.projectschedule.api.RClient
import com.bashsupn.projectschedule.models.ProjectResponse
import com.bashsupn.projectschedule.models.Task
import com.bashsupn.projectschedule.sharedpreferences.PrefManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleProject : AppCompatActivity() {

    private lateinit var chart: BarChart
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_project)
        prefManager = PrefManager(this)

        chart = findViewById(R.id.ganttChart)
        fetchTaskData()
    }

    private fun fetchTaskData() {
        val id = intent.getStringExtra("id")
        val api = RClient.Create(this)
        val callData = id?.let { api.getProject(it.toInt()) }

        callData?.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    projectResponse?.let {
                        val project = it.data
                        val tasks = project.type.tasks
                        populateChartWithTasks(tasks)
                    }
                } else {
                    Log.e("Response Error", response.message())
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }

    private fun populateChartWithTasks(tasks: List<Task>) {
        chart.description.isEnabled = false
        chart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis?.granularity = 1f
        chart.xAxis?.setDrawGridLines(false) // Remove vertical grid lines
        chart.xAxis?.spaceMin = 0.5f // Adjust the space before the first bar

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.spaceTop = 35f
        chart.axisLeft.setDrawGridLines(true)

        chart.axisRight.isEnabled = false

        val legend = chart.legend
        legend.isEnabled = true
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(true)

        val kasus = ArrayList<CustomBarEntry>()

        tasks.forEachIndexed { index, task ->
            // Calculate the task's date range in days
            val startDate = task.start_date.toString() // Assuming start_date is a String representation of the date
            val endDate = task.end_date.toString() // Assuming end_date is a String representation of the date

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateMillis = dateFormat.parse(startDate)?.time ?: 0L
            val endDateMillis = dateFormat.parse(endDate)?.time ?: 0L
            val dateRangeInDays = (endDateMillis - startDateMillis) / (1000 * 60 * 60 * 24)

            // Create a custom BarEntry with the start_date and task name as extra data (of type String)
            val customBarEntry = CustomBarEntry(index.toFloat(), dateRangeInDays.toFloat(), startDate, task.name)
            kasus.add(customBarEntry)
        }

        val kasusBarDataSet = BarDataSet(kasus as List<BarEntry>?, "Task")
        kasusBarDataSet.color = Color.GREEN

        val taskLabels = tasks.map { it.name + " (" + dateRangeToString(it.start_date as String,
            it.end_date as String
        ) + ")" }.toTypedArray()
        val tanggal = AxisDateFormatter(taskLabels)
        chart.xAxis?.setValueFormatter(tanggal)

        val barWidth = 0.8f
        val barData = BarData(kasusBarDataSet)
        barData.barWidth = barWidth
        chart.data = barData
        chart.animateXY(100, 500)
    }

    // Define a custom BarEntry class that extends BarEntry
    class CustomBarEntry(
        x: Float,
        y: Float,
        val startDate: String,
        val taskName: String
    ) : BarEntry(x, y) {
        override fun getData(): Any? {
            return startDate
        }
    }

    // Define the AxisDateFormatter class
    class AxisDateFormatter(private val mValues: Array<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return if (value >= 0) {
                if (mValues.isNotEmpty()) {
                    val index = value.toInt()
                    if (index < mValues.size) {
                        val taskInfo = mValues[index].split(" (")
                        val taskName = taskInfo[0]
                        val startDate = taskInfo[1].removeSuffix(")")
                        "$taskName($startDate)"
                    } else {
                        "" // Jika index melebihi ukuran mValues, maka kembalikan string kosong.
                    }
                } else {
                    "" // Jika mValues kosong, maka kembalikan string kosong.
                }
            } else {
                "" // Jika value < 0, maka kembalikan string kosong.
            }
        }


    }

    // Function to convert date range to the desired format
    private fun dateRangeToString(startDate: String, endDate: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        val startDateMillis = inputDateFormat.parse(startDate)?.time ?: 0L
        val endDateMillis = inputDateFormat.parse(endDate)?.time ?: 0L

        return "${outputDateFormat.format(startDateMillis)} - ${outputDateFormat.format(endDateMillis)}"
    }

}
