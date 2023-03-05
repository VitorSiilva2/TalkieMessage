package com.example.talkiemessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

        val auth = FirebaseAuth.getInstance()

        buttonRegisterActivity = findViewById(R.id.register_btn_enter_register)
        editLogin = findViewById(R.id.nickname_edit_input_register)
        editName = findViewById(R.id.name_edit_input_register)
        editPassword = findViewById(R.id.password_edit_input_register)

        buttonRegisterActivity.setOnClickListener {
            val email = editLogin.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()

            if (password.isEmpty() || name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Complete o formulario!", Toast.LENGTH_SHORT).show()
         } else {
             auth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener { cadastro ->
                     if (cadastro.isSuccessful) {
                         Toast.makeText(this, "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT)
                             .show()
                         editLogin.setText("")
                         editName.setText("")
                         editPassword.setText("")
                         val user = FirebaseAuth.getInstance().currentUser

                     } else {
                         val message = cadastro.exception?.message
                         Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                     }
                 }.addOnFailureListener { exception ->
                     val messageError = when(exception){
                         is FirebaseAuthUserCollisionException -> "Conta já cadastrada"
                         is FirebaseNetworkException -> "Verifique sua conexão com a internet "
                         else -> "Erro ao cadastrar usuario"
                     }
                     Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show()
                 }
         }

        }



    }
}