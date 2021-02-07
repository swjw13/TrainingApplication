package com.jw.iamstronger

import android.app.Activity
import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jw.iamstronger.Classes.Main
import com.jw.iamstronger.Classes.Routine
import com.jw.iamstronger.Classes.Routine_ALL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit private var completedRoutine: Button
    lateinit private var dailyButton: Button
    lateinit private var addButton: Button

    lateinit private var dialog: Dialog
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initview(this@MainActivity)

        completedRoutine.setOnClickListener {
            val intent = Intent(this, CompletedRoutine::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener {
            showDialog()
        }

        dailyButton.setOnClickListener {
            val intent = Intent(this, DailyRoutine::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.getAllRoutineButton).setOnClickListener {
            startActivity(Intent(this, AllRoutine::class.java))
        }

        setDailyStart()

    }

    private fun setDailyStart(){
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, BroadCast::class.java)
        val pIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 1)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, INTERVAL_DAY, pIntent)

    }

    private fun showDialog(){
        dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.dialog_addroutine)
        dialog.show()

        dialog.findViewById<Button>(R.id.popup_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.popup_submit).setOnClickListener {
            val routine_name = dialog.findViewById<EditText>(R.id.popup_routine).text.toString()
            val routine_startDate = dialog.findViewById<EditText>(R.id.popup_startDate).text.toString()
            val routine_endDate = dialog.findViewById<EditText>(R.id.popup_endDate).text.toString()
            val routine_week = dialog.findViewById<EditText>(R.id.popup_weekday).text.toString()

            val userId = getSharedPreferences("user", Context.MODE_PRIVATE).getString("loginId", "null")
            Log.d("loginId", userId!!)
            val madeRoutine = Routine(userId, routine_name, routine_startDate, routine_endDate, arrayListOf(), routine_week, false)

            (application as GlobalApplication).service.postRoutines(madeRoutine)
                .enqueue(object: Callback<Routine_ALL>{
                override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "등록에 실패하셨습니다!",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@MainActivity, "등록에 성공하셨습니다!" + userId,Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            })
        }
    }

    private fun initview(activity: Activity){
        completedRoutine = activity.findViewById(R.id.getCompletedRoutine)
        dailyButton = activity.findViewById(R.id.getDailyRoutine)
        addButton = activity.findViewById(R.id.addRoutine)
    }
}