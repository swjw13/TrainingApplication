package com.jw.iamstronger

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.jw.iamstronger.Classes.Routine_ALL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineSpecific : AppCompatActivity() {

    lateinit private var name: TextView
    lateinit private var startDate: TextView
    lateinit private var endDate: TextView
    lateinit private var workout: TextView
    lateinit private var weekday: TextView
    lateinit private var workoutContainer: RelativeLayout
    lateinit private var dialog: Dialog
    private var id: Int = 0

    private var specific: Routine_ALL? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_specific)

        id = intent.getIntExtra("id", 0)

        (application as GlobalApplication).service.getRoutineDetail(id).enqueue(object: Callback<Routine_ALL>{
            override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {

            }

            override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {
                specific = response.body()
            }
        })

        initview(this)

        setView(specific)
        workoutContainer.setOnClickListener {
            val intent = Intent(this, WorkoutList::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
        findViewById<Button>(R.id.updateRoutine).setOnClickListener {
            updateRoutine()
            setView(specific)
        }
        findViewById<Button>(R.id.routine_change).setOnClickListener {
            (application as GlobalApplication).service.DelRoutineDetail(id).enqueue(object: Callback<Void>{
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@RoutineSpecific, "삭제가 실패하였습니다", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(this@RoutineSpecific, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }

        addClick()
    }

    private fun addClick(){
        name.setOnClickListener {
            showDialog("routine_name")
        }
        startDate.setOnClickListener {
            showDialog("start_date")
        }
        endDate.setOnClickListener {
            showDialog("end_date")
        }
        weekday.setOnClickListener {
            showDialog("weekday")
        }
    }

    private fun showDialog(key: String){
        dialog = Dialog(this@RoutineSpecific)
        dialog.setContentView(R.layout.dialog_changeroutine)
        dialog.show()

        dialog.findViewById<Button>(R.id.changeRoutineCancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.changeRoutineSubmitButton).setOnClickListener {
            val change = dialog.findViewById<EditText>(R.id.changeRoutineEditText).text.toString()

            val map = mapOf(key to change)
            (application as GlobalApplication).service.putRoutineDetail(id, map).enqueue(object: Callback<Routine_ALL>{
                override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                    Toast.makeText(this@RoutineSpecific, "통신에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@RoutineSpecific, "통신에 성공하였습니다", Toast.LENGTH_SHORT).show()
                        specific = response.body()
                        setView(specific)
                    }
                }
            })
            dialog.dismiss()
        }
    }

    private fun updateRoutine(){
        (application as GlobalApplication).service.getRoutineDetail(id).enqueue(object: Callback<Routine_ALL>{
            override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {

            }

            override fun onResponse(call: Call<Routine_ALL>, response: Response<Routine_ALL>) {
                specific = response.body()
                setView(specific)
            }
        })
    }

    private fun setView(man: Routine_ALL?){
        if(man != null){
            name.setText(man.routine_name)
            startDate.setText(man.start_date)
            endDate.setText(man.end_date)
            weekday.setText(man.weekday)
        }

        workout.setText("workout보러가기")
    }

    private fun initview(activity: Activity){
        workoutContainer = activity.findViewById(R.id.routineDetailWorkout)
        name = activity.findViewById(R.id.specificRoutineName)
        startDate = activity.findViewById(R.id.specificStartDate)
        endDate = activity.findViewById(R.id.specificEndDate)
        workout = activity.findViewById(R.id.specificWorkout)
        weekday = activity.findViewById(R.id.specificWeekday)

    }
}