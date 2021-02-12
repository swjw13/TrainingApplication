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
import android.view.MenuItem
import android.view.Window
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
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


    private lateinit var alarmManager: AlarmManager
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setDailyStart()

        val f1 = frag1()
        val f2 = frag2()
        val f3 = frag3()

        navView = findViewById(R.id.mainNavView)
        navView.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.action_btn1 ->{
                        supportFragmentManager.beginTransaction().replace(R.id.usercontainer, f1).commit()
                        return true
                    }
                    R.id.action_btn2 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.usercontainer, f2).commit()
                        return true
                    }
                    R.id.action_btn3 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.usercontainer, f3).commit()
                        return true
                    }
                }
                return false
            }
        })

        supportFragmentManager.beginTransaction().replace(R.id.usercontainer, f2).commit()

    }

    private fun setDailyStart() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, BroadCast::class.java)
        val pIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val calendar = Calendar.getInstance(Locale.KOREA)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 1)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            INTERVAL_DAY,
            pIntent
        )

    }

}