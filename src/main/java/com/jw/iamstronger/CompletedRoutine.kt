package com.jw.iamstronger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Routine_ALL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedRoutine : AppCompatActivity() {

    lateinit private var adapter: Adapter
    lateinit private var container: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_routine)

        container = findViewById(R.id.completed_recyclerview)

        (application as GlobalApplication).service.getCompletedRoutines().enqueue(object:
            Callback<ArrayList<Routine_ALL>>{
            override fun onFailure(call: Call<ArrayList<Routine_ALL>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<Routine_ALL>>,
                response: Response<ArrayList<Routine_ALL>>
            ) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    adapter = Adapter(list, LayoutInflater.from(this@CompletedRoutine))
                }
            }
        })

        container.layoutManager = LinearLayoutManager(this)
    }
}

private class Adapter(
    var list: ArrayList<Routine_ALL>,
    val inflater: LayoutInflater
) : RecyclerView.Adapter<Adapter.viewHolder>() {
    inner class viewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val routineName = itemview.findViewById<TextView>(R.id.completeitem_routineName)
        val routineDay = itemview.findViewById<TextView>(R.id.completeitem_time)
        val routineWeek = itemview.findViewById<TextView>(R.id.completeitem_weekday)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = inflater.inflate(R.layout.completed_item, parent, false)
        return viewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.routineName.setText(list[position].routine_name)

        val day = list[position].start_date + " ~ " + list[position].end_date
        holder.routineDay.setText(day)

        val day_list = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val routinedate = list[position].weekday
        var date_tmp = ""
        for (i in day_list) {
            if (routinedate != null) {
                if (i in routinedate)
                    date_tmp = date_tmp + i + " "
            }

        }
        holder.routineWeek.setText(date_tmp)
    }
}