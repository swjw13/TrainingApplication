package com.jw.iamstronger

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Routine
import com.jw.iamstronger.Classes.Routine_ALL
import com.jw.iamstronger.databinding.FragmentFrag2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class frag2 : Fragment() {

    private lateinit var binding: FragmentFrag2Binding
    private lateinit var adapter: frag2Adapter
    private lateinit var today: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_frag2,
            container,
            false
        )

        today = getDayofWeek()

        binding.addRoutine.setOnClickListener {
            val intent = Intent(getContext(), addRoutineActivity::class.java)
            startActivity(intent)
        }

        initRec()

        return binding.root
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

    private fun initRec(){
        adapter = frag2Adapter(arrayListOf(), context!!)
        binding.dailyRoutineView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity?.application as GlobalApplication).service.getDailyRoutine(today).enqueue(object: Callback<ArrayList<Routine_ALL>>{
            override fun onFailure(call: Call<ArrayList<Routine_ALL>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<Routine_ALL>>,
                response: Response<ArrayList<Routine_ALL>>
            ) {
                response.body()?.apply {
                    adapter.replace(this)
                }
            }
        })
    }
}

private class frag2Adapter(
    var list: ArrayList<Routine_ALL>,
    val context: Context
): RecyclerView.Adapter<frag2Adapter.viewHolder>(){

    inner class viewHolder(item: View): RecyclerView.ViewHolder(item){
        val name = item.findViewById<TextView>(R.id.dailyRoutineName)
        val time = item.findViewById<TextView>(R.id.dailyRoutineTime)
        val container = item.findViewById<RelativeLayout>(R.id.dailyRoutineContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return viewHolder(inflater.inflate(R.layout.daily_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = list[position]
        holder.name.setText(item.routine_name)
        holder.time.setText(item.start_date + " ~ " + item.end_date)
        holder.container.setOnClickListener{
            val intent = Intent(context, RoutineSpecific::class.java)
            intent.putExtra("id", item.id)
            context.startActivity(intent)
        }
    }

    fun replace(item: ArrayList<Routine_ALL>){
        list = item
        notifyDataSetChanged()
    }
}