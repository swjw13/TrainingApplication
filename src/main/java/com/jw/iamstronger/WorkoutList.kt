package com.jw.iamstronger

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jw.iamstronger.Classes.Workout
import com.jw.iamstronger.Classes.Workout_post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutList : AppCompatActivity() {

    lateinit private var addButton: Button
    lateinit private var updateButton: Button
    lateinit private var workoutView: RecyclerView
    private lateinit var adapter: workoutAdapter

    private var list: ArrayList<Workout>? = null
    lateinit private var dialog: Dialog
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list)
        id = intent.getIntExtra("id",0)

        initview(this)
        adapter = workoutAdapter(arrayListOf(), LayoutInflater.from(this), this, id)
        workoutView.adapter = adapter
        workoutView.layoutManager = LinearLayoutManager(this)

        getList()

        updateButton.setOnClickListener {
            getList()
        }
        addButton.setOnClickListener {
            showDialog()
        }
    }

    private fun getList(){
        (application as GlobalApplication).service.getWorkoutList(id).enqueue(object: Callback<ArrayList<Workout>>{
            override fun onFailure(call: Call<ArrayList<Workout>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<Workout>>,
                response: Response<ArrayList<Workout>>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(this@WorkoutList, "통신에 성공하였스빈다! " + response.body()?.size, Toast.LENGTH_SHORT).show()
                    list = response.body()
                    if(list != null) {
                        adapter.replace(list!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun showDialog(){
        dialog.show()

        dialog.findViewById<Button>(R.id.dialog_workout_cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.dialog_workout_addButton).setOnClickListener {
            val name = dialog.findViewById<EditText>(R.id.addWorkoutName).text.toString()
            val weight = dialog.findViewById<EditText>(R.id.addWorkoutWeight).text.toString().toInt()
            val rep = dialog.findViewById<EditText>(R.id.addWorkoutRep).text.toString().toInt()
            val sets = dialog.findViewById<EditText>(R.id.addWorkoutSets).text.toString().toInt()

            val workout = Workout_post(workout_name = name, weight = weight, repitition = rep, sets = sets)

            (application as GlobalApplication).service.postWorkoutList(id, workout).enqueue(object: Callback<Workout>{
                override fun onFailure(call: Call<Workout>, t: Throwable) {

                }

                override fun onResponse(call: Call<Workout>, response: Response<Workout>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@WorkoutList, "등록에 성공하셨습니다!",Toast.LENGTH_LONG).show()
                        adapter.add(response.body()!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            })
            dialog.dismiss()
        }
    }

    private fun initview(activity: Activity){
        addButton = activity.findViewById(R.id.addWorkout)
        updateButton = activity.findViewById(R.id.viewWorkout)
        workoutView = activity.findViewById(R.id.workoutContainer)

        dialog = Dialog(this@WorkoutList)
        dialog.setContentView(R.layout.dialog_addworkout)
    }
}


private class workoutAdapter(
    var list: ArrayList<Workout>,
    val inflater: LayoutInflater,
    val context: Context,
    val routine_id: Int
): RecyclerView.Adapter<workoutAdapter.viewholder>(){
    inner class viewholder(item: View): RecyclerView.ViewHolder(item){
        val container = item.findViewById<LinearLayout>(R.id.workoutListItem)
        val text = item.findViewById<TextView>(R.id.workout_box)
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
            intent.putExtra("routine_id",routine_id)
            intent.putExtra("workout_id",list[position].id)
            context.startActivity(intent)
        }
    }
    fun replace(item: ArrayList<Workout>){
        list = item
        notifyDataSetChanged()
    }
    fun add(item: Workout){
        list.add(item)
    }
}