package com.jw.iamstronger

import android.app.Application
import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class GlobalApplication: Application() {

    lateinit var service: retrofitTest

    override fun onCreate() {
        super.onCreate()

        createRetrofit()
    }

    fun createRetrofit(){
        val header = Interceptor{
            val original = it.request()

            if(checkIsLogin()){

                val token = getUserToken()
                val request = original.newBuilder().header("Authorization", "Token " + token).build()
                it.proceed(request)
            } else{
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(header)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://yngsng.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(retrofitTest::class.java)
    }
    // sp에 저장된 값으로 로그인 여부를 체크
    fun checkIsLogin(): Boolean{
        val sp = getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = sp.getString("loginToken", "null")
        if(token != "null") return true
        else return false
    }

    fun getUserToken(): String?{
        val token = getSharedPreferences("user", Context.MODE_PRIVATE).getString("loginToken",null)
        if(token == "null") return null
        else return token
    }
}