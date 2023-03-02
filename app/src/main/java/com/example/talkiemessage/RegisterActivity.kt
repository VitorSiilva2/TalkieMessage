package com.example.talkiemessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editLogin : EditText
    private lateinit var editName : EditText
    private lateinit var editPassword : EditText
    private lateinit var buttonRegisterActivity : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegisterActivity = findViewById(R.id.register_btn_enter_register)
        editLogin = findViewById(R.id.nickname_edit_input_register)
        editName = findViewById(R.id.name_edit_input_register)
        editPassword = findViewById(R.id.password_edit_input_register)

        buttonRegisterActivity.setOnClickListener {
            val nickname = editLogin.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()

            if (password.isEmpty() || name.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "Complete o formulario!", Toast.LENGTH_SHORT).show()
         } else {
             FirebaseAuth.getInstance().createUserWithEmailAndPassword(nickname, password)
                 .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        val user = FirebaseAuth.getInstance().currentUser
                    } else {
                        Toast.makeText(this, "error : Revise seus dados!", Toast.LENGTH_SHORT).show()
                        val message = task.exception?.message
                        //Trate o erro de acordo com sua necessidade
                    }
                }
         }

        }



    }
}