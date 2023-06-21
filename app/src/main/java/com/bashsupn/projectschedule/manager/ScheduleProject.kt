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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleProject : AppCompatActivity() {

    private lateinit var chart: BarChart
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_project)
        prefManager = PrefManager(this)

        chart = findViewById(R.id.ganttChart)
        setupChart()
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
                        val tasks = project.tasks
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

    private fun setupChart() {
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter()

        val yAxisLeft: YAxis = chart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.setDrawLabels(true)

        val yAxisRight: YAxis = chart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.setDrawAxisLine(false)
        yAxisRight.setDrawLabels(false)
    }

    private fun populateChartWithTasks(tasks: List<Task>) {
        // Clear existing data sets
        chart.clear()

        // Create a list of BarEntry objects to hold the task data
        val entries = ArrayList<BarEntry>()

        // Create a list of labels to display on the y-axis (task names)
        val labels = ArrayList<String>()

        // Create a list of colors for the tasks
        val colors = ArrayList<Int>()
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        // Add more colors as needed for additional tasks

        // Format for parsing and formatting date strings
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Iterate over the tasks and add them to the entries list
        tasks.forEachIndexed { index, task ->
            try {
                // Parse the start and end dates of the task
                val startDate = dateFormat.parse(task.start_date as String)
                val endDate = dateFormat.parse(task.end_date as String)

                // Calculate the start and end date indices
                val startIndex = calculateDateIndex(startDate)
                val endIndex = calculateDateIndex(endDate)

                // Create a BarEntry object with the start date index and the index as the y-value
                val entry = BarEntry(startIndex.toFloat(), index.toFloat(), floatArrayOf(endIndex.toFloat() - startIndex.toFloat()))

                // Add the entry to the entries list
                entries.add(entry)

                // Add the task name as the label for the corresponding y-value
                val taskName = task.name
                labels.add(taskName)
            } catch (e: Exception) {
                Log.e("Date Parsing Error", e.message.toString())
            }
        }

        // Create a BarDataSet with the entries list and a label
        val dataSet = BarDataSet(entries, "Task Duration")

        // Set the colors for the bars using the colors list
        dataSet.colors = colors

        // Create a BarData object and set it to the chart
        val data = BarData(dataSet)
        chart.data = data

        // Customize the rest of the chart as needed

        // Set the labels on the y-axis (task names)
        chart.axisLeft.valueFormatter = IndexAxisValueFormatter(labels)

        // Set the labels on the x-axis
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                return if (index >= 0 && index < labels.size) {
                    labels[index]
                } else {
                    ""
                }
            }
        }

        // Refresh the chart to display the updated data
        chart.invalidate()
    }

    private fun calculateDateIndex(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}
