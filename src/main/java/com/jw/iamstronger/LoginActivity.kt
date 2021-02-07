package com.jw.iamstronger

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.jw.iamstronger.Classes.getToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit private var email: EditText
    lateinit private var password: EditText
    lateinit private var registerButton: Button
    lateinit private var loginButton: Button

    override fun onStart() {
        super.onStart()
        getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView(this)

        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val id = email.text.toString()
            val password = password.text.toString()

            (application as GlobalApplication).service.login(id, password).enqueue(object: Callback<getToken> {
                override fun onFailure(call: Call<getToken>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "로그인에 실패하셨습니다",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<getToken>, response: Response<getToken>) {
                    if(response.isSuccessful){
                        val token = response.body()?.Token!!

                        val sp = getSharedPreferences("user", Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        editor.putString("loginToken", token)
                        editor.putString("loginId", id)
                        editor.apply()

                        (application as GlobalApplication).createRetrofit()

                        Toast.makeText(this@LoginActivity, "로그인에 성공하셨습니다",Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }

    }
    fun initView(activity: Activity){
        email = activity.findViewById(R.id.login_id)
        password = activity.findViewById(R.id.login_password)
        registerButton = activity.findViewById(R.id.registerButton)
        loginButton = activity.findViewById(R.id.loginButton)
    }
}