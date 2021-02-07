package com.jw.iamstronger

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Routine_ALL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DailyRoutine : AppCompatActivity() {

    private lateinit var list : ArrayList<Routine_ALL>
    private lateinit var today: String
    private lateinit var dayOfWeek: TextView
    private lateinit var dailyView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_routine)

        initview(this@DailyRoutine)

        today = getDayofWeek()

        getList()

        val date_kor = getRealWeekofDay(today)
        dayOfWeek.setText("오늘은 $date_kor 입니다" )

    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    private fun getList(){
        (application as GlobalApplication).service
            .getDailyRoutine(today)
            .enqueue(object: Callback<ArrayList<Routine_ALL>>{
                override fun onFailure(call: Call<ArrayList<Routine_ALL>>, t: Throwable) {
                    Toast.makeText(this@DailyRoutine, "일별 루틴 접속에 실패하셨습니다!",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<Routine_ALL>>,
                    response: Response<ArrayList<Routine_ALL>>
                ) {
                    if(response.isSuccessful){
                        val postList = response.body()
                        Toast.makeText(this@DailyRoutine, today + "요일 루틴 접속에 성공하셨습니다!" + postList?.size,Toast.LENGTH_LONG).show()
                        list = response.body()!!

                        val container = findViewById<RecyclerView>(R.id.daily_recyclerview)
                        val adapter = DailyAdapter(postList, LayoutInflater.from(this@DailyRoutine), this@DailyRoutine)
                        container.adapter = adapter
                        container.layoutManager = LinearLayoutManager(this@DailyRoutine)
                    }
                }
            })
    }

    private fun initview(activity: Activity){
        dayOfWeek = activity.findViewById(R.id.daily_weekday)
        dailyView = activity.findViewById(R.id.daily_recyclerview)
    }

    private fun getRealWeekofDay(eng: String): String{
        val today = when(eng){
            "Mon" -> "월요일"
            "Tue" -> "화요일"
            "Wed" -> "수요일"
            "Thu" -> "목요일"
            "Fri" -> "금요일"
            "Sat" -> "토요일"
            else -> "일요일"
        }
        return today
    }

    private fun getDayofWeek(): String{
        val cal = Calendar.getInstance()
        var today = ""

        today = when(cal.get(Calendar.DAY_OF_WEEK)){
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "Sun"
        }

        return today
    }
}

private class DailyAdapter(
    val list: ArrayList<Routine_ALL>?,
    val inflater: LayoutInflater,
    val context: Context
): RecyclerView.Adapter<DailyAdapter.viewHolder>(){
    inner class viewHolder(item: View): RecyclerView.ViewHolder(item){
        val routineName = item.findViewById<TextView>(R.id.daily_routineName)
        val routineTime = item.findViewById<TextView>(R.id.daily_time)
        val routineWeekday = item.findViewById<TextView>(R.id.daily_weekday)
        val container = item.findViewById<LinearLayout>(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(inflater.inflate(R.layout.daily_item, parent, false))
    }

    override fun getItemCount(): Int {
        if (list != null) {
            return list.size
        }
        else
            return 0
    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if(list != null){
            holder.routineName.setText(list[position].routine_name)
            val day = list[position].start_date + " ~ " + list[position].end_date
            holder.routineTime.setText(day)

            val day_list = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            val routinedate = list[position].weekday
            var date_tmp = ""
            for (i in day_list) {
                if (routinedate != null) {
                    if (i in routinedate)
                        date_tmp = date_tmp + i + " "
                }

            }
            holder.routineWeekday.setText(date_tmp)
            holder.container.setOnClickListener {
                val id = list[position].id

                val intent = Intent(context, RoutineSpecific::class.java)
                intent.putExtra("id",id)
                context.startActivity(intent)
            }

        } else{
            holder.routineName.setText("None")
            holder.routineTime.setText("None")
            holder.routineWeekday.setText("None")
        }
    }
}