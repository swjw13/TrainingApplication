package com.jw.iamstronger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BroadCast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.applicationContext as GlobalApplication).service.getDaystart().enqueue(object:
            Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(context, "실시간 업데이트 완료", Toast.LENGTH_SHORT).show()
            }
        })
    }
}