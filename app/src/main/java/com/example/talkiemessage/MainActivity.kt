package com.example.talkiemessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var editLogin : EditText
    lateinit var editPassword : EditText
    lateinit var btnEnter : Button
    lateinit var btnRegister : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRegister = findViewById(R.id.register_text_enter)
        editLogin = findViewById(R.id.login_edit_input)
        editPassword = findViewById(R.id.password_edit_input)
        btnEnter = findViewById(R.id.login_btn_enter)

        btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnEnter.setOnClickListener {
            if(!validate()){
                Toast.makeText(this, "@string/userIncorrect", Toast.LENGTH_SHORT).show()
            }
            if (validadeLogin()) {
                val intent = Intent(this, AdminScreen::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ClientScreen::class.java)
                startActivity(intent)
            }


        }

    }

    private fun validate(): Boolean {
        return editLogin.text.toString().isNotEmpty()
                && editPassword.text.toString().isNotEmpty()
                && editPassword.length() >= 8 }

    private fun validadeLogin() : Boolean {
        return editLogin.text.toString() == "ADMIN"
    }
}