package com.jw.iamstronger

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.jw.iamstronger.Classes.Routine
import com.jw.iamstronger.Classes.Routine_ALL
import com.jw.iamstronger.Classes.Workout
import com.jw.iamstronger.databinding.ActivityAddRoutineBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class addRoutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = arrayListOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items)
        binding.routineWeekdayList.adapter = adapter

        autoTextSet()

        binding.addRoutineButton.setOnClickListener {
            val routine_name = binding.routineName.text.toString()
            val start_date = binding.routineStartDate.text.toString()
            val end_date = binding.routineEndDate.text.toString()

            var weekday = ""
            val sparsearray = binding.routineWeekdayList.checkedItemPositions

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
            val userId = getSharedPreferences("user", Context.MODE_PRIVATE).getString("loginId", "null")

            val madeRoutine = Routine(
                routine_user = userId,
                routine_name = routine_name,
                start_date = start_date,
                end_date = end_date,
                workout = arrayListOf(),
                weekday = routine_week ,
                done = false
            )

            (application as GlobalApplication).service.postRoutines(madeRoutine)
                .enqueue(object : Callback<Routine_ALL> {
                    override fun onFailure(call: Call<Routine_ALL>, t: Throwable) {
                        Toast.makeText(this@addRoutineActivity, "등록에 실패하셨습니다!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<Routine_ALL>,
                        response: Response<Routine_ALL>
                    ) {
                        println(routine_week)
                        if(response.isSuccessful) {
                            Toast.makeText(
                                this@addRoutineActivity,
                                "등록에 성공하셨습니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    }
                })
        }
    }

    private fun autoTextSet() {
        binding.routineStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 4 && before == 0) {
                    binding.routineStartDate.append("-")
                } else if (s?.length == 7 && before == 0) {
                    binding.routineStartDate.append("-")
                }
            }
        })

        binding.routineEndDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 4 && before == 0) {
                    binding.routineEndDate.append("-")
                } else if (s?.length == 7 && before == 0) {
                    binding.routineEndDate.append("-")
                }
            }
        })
    }
}