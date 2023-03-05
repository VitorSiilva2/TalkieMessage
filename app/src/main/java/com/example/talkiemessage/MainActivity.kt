package com.example.talkiemessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.talkiemessage.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtnEnter.setOnClickListener{
            val email = binding.loginEditInput.text.toString()
            val password = binding.passwordEditInput.text.toString()

            if (validate()) {
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authentication ->
                if(authentication.isSuccessful) {
                    mainScreen()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }

        binding.registerTextEnter.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        }

    }

    private fun validate(): Boolean {
        return binding.loginEditInput.text.isEmpty() ||
                binding.passwordEditInput.text.isEmpty()
    }

    private fun mainScreen(){

        val intent = Intent(this, ClientScreen::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser // user atual o currentUser

        if(user != null) {
            mainScreen()
        }
    }

}