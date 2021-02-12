package com.jw.iamstronger

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jw.iamstronger.Classes.Workout
import com.jw.iamstronger.databinding.ActivityWorkoutSpecificBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutSpecific : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutSpecificBinding
    private var routine_id: Int? = 0
    private var workout_id: Int? = 0
    private var base: Workout? = null
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutSpecificBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        routine_id = intent.getIntExtra("routine_id", 0)
        workout_id = intent.getIntExtra("workout_id", 0)

        binding.workoutDeleteButton.setOnClickListener {
            delWorkout()
        }

        addClick()
    }

    override fun onResume() {
        super.onResume()
        getWorkout()
    }

    private fun addClick() {
        binding.workoutName.setOnClickListener {
            changeWorkout("workout_name")
        }
        binding.workoutWeight.setOnClickListener {
            changeWorkout("weight")
        }
        binding.workoutRep.setOnClickListener {
            changeWorkout("repetition")
        }
        binding.workoutSet.setOnClickListener {
            changeWorkout("sets")
        }
    }

    private fun changeWorkout(key: String) {
        dialog = Dialog(this@WorkoutSpecific)
        dialog.setContentView(R.layout.dialog_changeroutine)
        dialog.show()
        if(key != "workout_name"){
            dialog.findViewById<EditText>(R.id.changeRoutineEditText).inputType = InputType.TYPE_CLASS_NUMBER
        } else{
            dialog.findViewById<EditText>(R.id.changeRoutineEditText).inputType = InputType.TYPE_CLASS_TEXT
        }

        dialog.findViewById<Button>(R.id.changeRoutineCancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.changeRoutineSubmitButton).setOnClickListener {
            val value = dialog.findViewById<EditText>(R.id.changeRoutineEditText).text.toString()

            when (key) {
                "workout_name" -> base?.workout_name = value
                "weight" -> base?.weight = value.toInt()
                "repetition" -> base?.repetition = value.toInt()
                "sets" -> base?.sets = value.toInt()
            }

            (application as GlobalApplication).service.putWorkoutDetail(
                routine_id!!,
                workout_id!!,
                base!!
            ).enqueue(object : Callback<Workout> {
                override fun onFailure(call: Call<Workout>, t: Throwable) {

                }

                override fun onResponse(call: Call<Workout>, response: Response<Workout>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WorkoutSpecific, "통신 성공", Toast.LENGTH_SHORT).show()
                        response.body()?.apply {
                            base = this
                        }
                        setView()
                    }
                }
            })
            dialog.dismiss()
        }
    }

    private fun delWorkout() {
        (application as GlobalApplication).service.delWorkoutDetail(routine_id!!, workout_id!!)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(this@WorkoutSpecific, "삭제 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }

    private fun getWorkout() {
        (application as GlobalApplication).service.getWorkoutDetail(routine_id!!, workout_id!!)
            .enqueue(object : Callback<Workout> {
                override fun onFailure(call: Call<Workout>, t: Throwable) {
                    Toast.makeText(this@WorkoutSpecific, "workout가져오기 실패", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<Workout>, response: Response<Workout>) {
                    Toast.makeText(this@WorkoutSpecific, "workout가져오기 성공", Toast.LENGTH_SHORT)
                        .show()
                    base = response.body()
                    setView()
                }
            })
    }

    private fun setView() {
        binding.workoutName.text = base?.workout_name
        binding.workoutWeight.text = base?.weight.toString()
        binding.workoutRep.text = base?.repetition.toString()
        binding.workoutSet.text = base?.sets.toString()
    }

}