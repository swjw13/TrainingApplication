package com.jw.iamstronger

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jw.iamstronger.Classes.getToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit private var email: EditText
    lateinit private var password: EditText
    lateinit private var nickname: EditText
    lateinit private var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView(this@RegisterActivity)

        button.setOnClickListener {
            val id = email.text.toString()
            val password = password.text.toString()
            val nickname = nickname.text.toString()

            (application as GlobalApplication).service.signin(id, password, nickname).enqueue(object: Callback<getToken> {
                override fun onFailure(call: Call<getToken>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<getToken>, response: Response<getToken>) {
                    val token = response.body()?.Token

                    val editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    editor.putString("loginToken", token).apply()
                    editor.putString("loginId", id).apply()
                    Toast.makeText(this@RegisterActivity, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            })
        }
    }

    fun initView(activity: Activity){
        email = activity.findViewById(R.id.register_email)
        password = activity.findViewById(R.id.register_password)
        nickname = activity.findViewById(R.id.register_nickname)
        button = activity.findViewById(R.id.register_submitButton)
    }
}