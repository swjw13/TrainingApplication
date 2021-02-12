package com.jw.iamstronger

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Routine_ALL
import com.jw.iamstronger.Classes.Workout
import com.jw.iamstronger.Classes.Workout_post
import com.jw.iamstronger.databinding.ActivityRoutineSpecificBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class RoutineSpecific : AppCompatActivity() {

    private lateinit var dialog: Dialog
    private lateinit var dialog2: Dialog
    private var id : Int = 0
    private var specific: Routine_ALL? = null
    private lateinit var binding: ActivityRoutineSpecificBinding
    private lateinit var adapter: workoutAdapter
    private var list: ArrayList<Workout>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoutineSpecificBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initRecyclerView()
        specific = Routine_ALL()

        id = intent.getIntExtra("id", 0)
        // add listener
        changeRoutineInfo()

        binding.routineChange.setOnClickListener {
            (application as GlobalApplication).service.DelRoutineDetail(id)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@RoutineSpecific, "삭제가 실패하였습니다", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Toast.makeText(this@RoutineSpecific, "삭제가 완료되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                })
        }

        binding.routineCompleteButton.setOnClickListener {
            val map = mapOf("completed" to true.toString())
            (application as GlobalApplication).service.putRoutineDetail(id, map)
                .enqueue(object : Callback<Routine_ALL> {
                    override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<Routine_ALL>,
                        response: Response<Routine_ALL>
                    ) {
                        Toast.makeText(
                            this@RoutineSpecific,
                            "축하합니다 운동 완료하셨습니다!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
        }

        binding.workoutAdd.setOnClickListener {
            addWork()
        }
    }

    private fun initRecyclerView() {
        adapter = workoutAdapter(arrayListOf(), LayoutInflater.from(this), this, id)
        binding.workoutListView.layoutManager = LinearLayoutManager(this)
        binding.workoutListView.adapter = adapter
    }

    private fun addWork() {
        dialog2 = Dialog(this@RoutineSpecific)
        dialog2.setContentView(R.layout.dialog2_addworkout)

        dialog2.show()

        dialog2.findViewById<Button>(R.id.dialog_workout_cancelButton2).setOnClickListener {
            dialog2.dismiss()
        }

        dialog2.findViewById<Button>(R.id.dialog_workout_addButton2).setOnClickListener {
            val name = dialog2.findViewById<EditText>(R.id.addWorkoutName2).text.toString()
            val weight =
                dialog2.findViewById<EditText>(R.id.addWorkoutWeight2).text.toString().toInt()
            val rep = dialog2.findViewById<EditText>(R.id.addWorkoutRep2).text.toString().toInt()
            val sets = dialog2.findViewById<EditText>(R.id.addWorkoutSets2).text.toString().toInt()

            val workout =
                Workout_post(workout_name = name, weight = weight, repitition = rep, sets = sets)

            (application as GlobalApplication).service.postWorkoutList(id, workout)
                .enqueue(object : Callback<Workout> {
                    override fun onFailure(call: Call<Workout>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<Workout>, response: Response<Workout>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RoutineSpecific, "등록에 성공하셨습니다!", Toast.LENGTH_LONG)
                                .show()
                            adapter.add(response.body()!!)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
            dialog2.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        getRoutine()
        getList()
    }

    private fun getRoutine() {
        (application as GlobalApplication).service.getRoutineDetail(id)
            .enqueue(object : Callback<Routine_ALL> {
                override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {

                }

                override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {

                    if(response.isSuccessful){
                        response.body()?.apply {
                            specific = this
                            setView()
                        }
                    }
                }
            })
    }

    private fun getList() {
        (application as GlobalApplication).service.getWorkoutList(id)
            .enqueue(object : Callback<ArrayList<Workout>> {
                override fun onFailure(call: Call<ArrayList<Workout>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<ArrayList<Workout>>,
                    response: Response<ArrayList<Workout>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RoutineSpecific, "통신에 성공하였습니다! ", Toast.LENGTH_SHORT)
                            .show()
                        list = response.body()?.apply {
                            adapter.replace(this)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    private fun changeRoutineInfo() {
        binding.specificRoutineName.setOnClickListener {
            showDialog("routine_name")
        }
        binding.routineStartDate.setOnClickListener {
            showDialog("start_date")
        }
        binding.routineEndDate.setOnClickListener {
            showDialog("end_date")
        }
        binding.routineWeekday.setOnClickListener {
            showWeekdayDialog()
        }
    }

    private fun showWeekdayDialog(){
        dialog = Dialog(this@RoutineSpecific)
        dialog.setContentView(R.layout.dialog_changeroutineweekday)
        dialog.show()

        val items = arrayListOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items)

        val list = dialog.findViewById<ListView>(R.id.changeRoutineWeekday)
        list.adapter = adapter

        dialog.findViewById<Button>(R.id.routineWeekdayChangeButton).setOnClickListener {
            var weekday = ""
            val sparsearray = list.checkedItemPositions
            for (i in 0..6) {
                if (sparsearray[i]) {
                    when (i) {
                        0 -> weekday += "Mon "
                        1 -> weekday += "Tue "
                        2 -> weekday += "Wed "
                        3 -> weekday += "Thu "
                        4 -> weekday += "Fri "
                        5 -> weekday += "Sat "
                        6 -> weekday += "Sun "
                    }
                }
            }
            weekday = weekday.substring(0, weekday.length - 1)
            val routine_week  = weekday

            (application as GlobalApplication).service.putRoutineDetail(id, mapOf("weekday" to routine_week)).enqueue(object: Callback<Routine_ALL>{
                override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                }

                override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {
                    if(response.isSuccessful){
                        response.body()?.apply{
                            specific = this
                            setView()
                        }
                    }
                    dialog.dismiss()
                }
            })
        }
    }

    private fun showDialog(key: String) {
        dialog = Dialog(this@RoutineSpecific)
        dialog.setContentView(R.layout.dialog_changeroutine)
        dialog.show()

        if(key == "routine_name"){
            dialog.findViewById<EditText>(R.id.changeRoutineEditText).inputType = InputType.TYPE_CLASS_TEXT
        } else if(key == "start_date" || key == "end_date"){
            dialog.findViewById<EditText>(R.id.changeRoutineEditText).inputType = InputType.TYPE_CLASS_DATETIME
        }

        dialog.findViewById<Button>(R.id.changeRoutineCancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.changeRoutineSubmitButton).setOnClickListener {
            val change = dialog.findViewById<EditText>(R.id.changeRoutineEditText).text.toString()

            val map = mapOf(key to change)
            (application as GlobalApplication).service.putRoutineDetail(id, map)
                .enqueue(object : Callback<Routine_ALL> {
                    override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                        Toast.makeText(this@RoutineSpecific, "통신에 실패하였습니다", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<Routine_ALL>,
                        response: Response<Routine_ALL>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RoutineSpecific, "통신에 성공하였습니다", Toast.LENGTH_SHORT).show()
                            specific = response.body()!!
                            setView()
                        }
                    }
                })
            dialog.dismiss()
        }
    }

    private fun setView() {
        binding.specificRoutineName.text = specific?.routine_name
        binding.routineStartDate.text = "시작일: " + specific?.start_date
        binding.routineEndDate.text = "종료일: " + specific?.end_date
        binding.routineWeekday.text = "요일" + specific?.weekday
    }
}

private class workoutAdapter(
    var list: ArrayList<Workout>,
    val inflater: LayoutInflater,
    val context: Context,
    val routine_id: Int
) : RecyclerView.Adapter<workoutAdapter.viewholder>() {
    inner class viewholder(item: View) : RecyclerView.ViewHolder(item) {
        val container = item.findViewById<RelativeLayout>(R.id.workoutListItem)
        val text = item.findViewById<TextView>(R.id.workoutName_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(inflater.inflate(R.layout.workout_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.text.setText(list[position].workout_name)
        holder.container.setOnClickListener {
            val intent = Intent(context, WorkoutSpecific::class.java)
            intent.putExtra("routine_id", routine_id)
            intent.putExtra("workout_id", list[position].id)
            context.startActivity(intent)
        }
    }

    fun replace(item: ArrayList<Workout>) {
        list = item
        notifyDataSetChanged()
    }

    fun add(item: Workout) {
        list.add(item)
    }
}