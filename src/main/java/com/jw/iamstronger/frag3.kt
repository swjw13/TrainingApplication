package com.jw.iamstronger

import android.app.Activity
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Routine
import com.jw.iamstronger.Classes.Routine_ALL
import com.jw.iamstronger.databinding.FragmentFrag3Binding
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//fragment for All Routines

class frag3 : Fragment() {

    private lateinit var binding: FragmentFrag3Binding
    private lateinit var adapter: frag3Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_frag3,
            container,
            false
        )

        initrecView()

        return binding.root
    }
    private fun initrecView(){
        adapter = frag3Adapter(arrayListOf(), activity!!)
        binding.allRoutineView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity?.application as GlobalApplication).service.getAllRoutines().enqueue(object: Callback<ArrayList<Routine_ALL>>{
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

private class frag3Adapter(
    var list: ArrayList<Routine_ALL>,
    val activity: FragmentActivity
): RecyclerView.Adapter<frag3Adapter.viewholder>(){

    inner class viewholder(item: View): RecyclerView.ViewHolder(item){
        val name = item.findViewById<TextView>(R.id.completeitem_routineName)
        val day = item.findViewById<TextView>(R.id.completeitem_time)
        val weekday = item.findViewById<TextView>(R.id.completeitem_weekday)
        val container = item.findViewById<LinearLayout>(R.id.completeitem_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.completed_item, parent, false)
        return viewholder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val routine = list[position]
        holder.name.setText(routine.routine_name)
        holder.day.setText(routine.start_date + " ~ " + routine.end_date)
        holder.weekday.setText(routine.weekday)
        holder.container.setOnClickListener {
            (activity.application as GlobalApplication).service.DelRoutineDetail(routine.id!!).enqueue(object: Callback<Void>{
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(activity.applicationContext, "삭제 완료", Toast.LENGTH_SHORT).show()
                    remove(routine)
                }
            })
        }
    }

    fun replace(item: ArrayList<Routine_ALL>){
        list = item
        notifyDataSetChanged()
    }
    fun remove(item: Routine_ALL?){
        item?.apply {
            list.remove(this)
            notifyDataSetChanged()
        }
    }
}