package com.jw.iamstronger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jw.iamstronger.Classes.Routine_ALL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllRoutine : AppCompatActivity() {

    private var list : ArrayList<Routine_ALL>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_routine)

        (application as GlobalApplication).service.getAllRoutines().enqueue(object:
            Callback<ArrayList<Routine_ALL>>{
            override fun onFailure(call: Call<ArrayList<Routine_ALL>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<Routine_ALL>>,
                response: Response<ArrayList<Routine_ALL>>
            ) {
                list = response.body()
                findViewById<TextView>(R.id.all_text).text = "" + list?.size
            }
        })

    }
}